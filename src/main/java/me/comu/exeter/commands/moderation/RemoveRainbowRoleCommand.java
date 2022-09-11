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

public class RemoveRainbowRoleCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();


        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_SERVER) && (!member.hasPermission(Permission.MANAGE_ROLES)) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to remove the rainbow role").build()).queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) && (!selfMember.hasPermission(Permission.MANAGE_ROLES))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to remove the rainbow role").build()).queue();
            return;
        }

        if (!SetRainbowRoleCommand.isIsRainbowRoleSet()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Failed to halt rainbow-role because there is no rainbow-role set. try " + Core.PREFIX + "help " + getInvoke()).build()).queue();
        } else {
            String name = SetRainbowRoleCommand.getRainbowRole().getName();
            SetRainbowRoleCommand.nullifyRainbowRole();
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully nullified the current rainbow-role! (`" + name + "`)").build()).queue();
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
        return new String[]{"removerainbow", "remrainrole", "removerainbowrole", "delrainbow", "stoprainbow", "delrainbowrole", "stoprainbowrole", "nullifyrainbow", "nullifyrainbowrole"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
