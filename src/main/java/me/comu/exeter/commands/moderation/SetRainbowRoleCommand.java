package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.events.RainbowRoleEvent;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SetRainbowRoleCommand implements ICommand {

    private static boolean isRainbowRoleSet;
    private static Role role;
    private static long roleID;
    public static Guild guild;


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member selfMember = event.getGuild().getSelfMember();
        guild = event.getGuild();


        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Currently only the owner can set the rainbow role due to ratelimit handling.").build()).queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_SERVER) && (!selfMember.hasPermission(Permission.MANAGE_ROLES))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to set the rainbow role").build()).queue();
            return;
        }

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a role").build()).queue();
            return;
        }

        try {
            role = event.getGuild().getRoleById(Long.parseLong(args.get(0)));
            roleID = Objects.requireNonNull(event.getGuild().getRoleById(Long.parseLong(args.get(0)))).getIdLong();
            isRainbowRoleSet = true;
            event.getChannel().sendMessageEmbeds(Utility.embed("Rainbow role successfully set to `" + role.getName() + "`").build()).queue();
        } catch (NullPointerException | NumberFormatException ex) {
            List<Role> roles = event.getGuild().getRolesByName(args.get(0), true);
            if (roles.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find role `" + Utility.removeMentions(args.get(0)) + "`. Maybe try using the role ID instead.").build()).queue();
                return;
            }
            role = roles.get(0);
            isRainbowRoleSet = true;
            event.getChannel().sendMessageEmbeds(Utility.embed("Rainbow role successfully set to `" + role.getName() + "`").build()).queue();
        }

    }

    public static boolean isIsRainbowRoleSet() {
        return isRainbowRoleSet;
    }

    public static long getRoleID() {
        return roleID;
    }

    static Role getRainbowRole() {
        return role;
    }

    static void nullifyRainbowRole() {
        role = null;
        isRainbowRoleSet = false;
        Core.getInstance().getJDA().removeEventListener(new RainbowRoleEvent());
    }

    @Override
    public String getHelp() {
        return "Sets the rainbow role to the specified role\n`" + Core.PREFIX + getInvoke() + " [role]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rainbow";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"setrainbowrole", "rainbowrole", "setrainbow", "setuprainbow", "rolerainbow"};
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
