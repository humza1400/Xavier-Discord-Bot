package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AboutCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member selfMember = event.getGuild().getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage("I don't have permissions to embed messages `(Manage_Server Permission)`").queue();
            return;
        }


        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("lovestruck Bot");
        embedBuilder.setDescription("https://discord.gg/EgJEs5E :)");
        embedBuilder.addField("Author","Comu", false);
        embedBuilder.addField("Information", "Powered by IntelliJ IDEA & Gradle 5.6.2", false);
        embedBuilder.addField("Help", "`" + Core.PREFIX + "help" + '`', false);
        embedBuilder.setColor(0x521e8a);
        embedBuilder.setFooter("Requested By " + event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl());
        event.getChannel().sendTyping().queue();
        event.getChannel(). sendMessage(embedBuilder.build()).queue();
        embedBuilder.clear();

    }

    @Override
    public String getHelp() {
        return "Returns information about the bot\n `" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "about";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"ab"};
    }
}
