package me.comu.exeter.commands.admin;

import groovy.lang.GroovyShell;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class EvalCommand implements ICommand {
    private final GroovyShell engine;
    private final String imports;

    public EvalCommand() {
        this.engine = new GroovyShell();
        this.imports = "import java.io.*\n" +
                "import java.lang.*\n" +
                "import java.util.*\n" +
                "import java.util.concurrent.*\n" +
                "import net.dv8tion.jda.core.*\n" +
                "import net.dv8tion.jda.core.entities.*\n" +
                "import net.dv8tion.jda.core.entities.impl.*\n" +
                "import net.dv8tion.jda.core.managers.*\n" +
                "import net.dv8tion.jda.core.managers.impl.*\n" +
                "import net.dv8tion.jda.core.utils.*\n";
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("Null Error 404 (malicious)?").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please supply some arguments to evaluate").queue();
            return;
        }
        try {
            engine.setProperty("args", args);
            engine.setProperty("event", event);
            engine.setProperty("message", event.getMessage());
            engine.setProperty("channel", event.getChannel());
            engine.setProperty("api", event.getJDA());
            engine.setProperty("guild", event.getGuild());
            engine.setProperty("member", event.getMember());
            String script = imports + event.getMessage().getContentRaw().split("\\s+", 2)[1];
            Object out = engine.evaluate(script);
            event.getChannel().sendMessage(out == null ? "Executed with error" : "[DEBUG] " + out.toString().replaceAll("JDA","API")).queue();
        } catch (Exception ex) {
            event.getChannel().sendMessage(ex.getMessage()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Evaluates script code via groovy engine\n`" + Core.PREFIX + getInvoke() + " [arguments]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`" ;
    }

    @Override
    public String getInvoke() {
        return "eval";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"evaluate","debug"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }
}
