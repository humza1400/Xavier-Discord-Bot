package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class RemoveRainbowRoleCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();


        if (!member.hasPermission(Permission.MANAGE_SERVER) && (!member.hasPermission(Permission.MANAGE_ROLES)) && member.getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to remove the rainbow role").queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) && (!selfMember.hasPermission(Permission.MANAGE_ROLES))) {
            channel.sendMessage("I don't have permissions to remove the rainbow role").queue();
            return;
        }

        if (!SetRainbowRoleCommand.isIsRainbowRoleSet())
        {
            channel.sendMessage("Failed to halt rainbow-role because there is no rainbow-role set. try " + Core.PREFIX + "help rainbowrole").queue();
            return;
        } else {
            String name = SetRainbowRoleCommand.getRainbowRole().getName();
            SetRainbowRoleCommand.nullifyRainbowRole();
            channel.sendMessage("Successfully nullified the current rainbow-role! (`" + name + "`)").queue();
        }

    }



    @Override
    public String getHelp() {
        return "Removes the currently set rainbow-role\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "remrainbow";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"removerainbow", "remrainrole","removerainbowrole","delrainbow", "stoprainbow","delrainbowrole","stoprainbowrole","nullifyrainbow","nullifyrainbowrole"};
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
