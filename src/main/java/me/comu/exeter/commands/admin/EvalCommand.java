package me.comu.exeter.commands.admin;

import groovy.lang.GroovyShell;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
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
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Null Error 404 (malicious)?").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please supply some arguments to evaluate.").build()).queue();
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
            event.getChannel().sendMessageEmbeds(Utility.embed(out == null ? "Executed with error" : "`[DEBUG]:` " + out.toString().replaceAll("JDA","API")).build()).queue();
        } catch (Exception ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(ex.getMessage()).build()).queue();
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
        return new String[] {"evaluate"};
    }

   @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
