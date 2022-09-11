package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ServerNameCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        String preServerName = event.getGuild().getName();
        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_SERVER) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to change the server name").build()).queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to change the server name").build()).queue();
            return;
        }
        String msg = event.getMessage().getContentRaw();
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a server name").build()).queue();
            return;
        }
        msg = msg.replace(Core.PREFIX + "servername", "").replace(Core.PREFIX + "nameserver", "").replace("_", " ");
        if (msg.length() != 2) {
            event.getGuild().getManager().setName(msg).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully changed server name from `" + preServerName + "` to `" + msg.substring(1) + "`.").build()).queue();
        } else
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Server names must be at least two characters long.").build()).queue();
    }

    @Override
    public String getHelp() {
        return "Sets the server name\n`" + Core.PREFIX + getInvoke() + " [name]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "servername";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"nameserver"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
