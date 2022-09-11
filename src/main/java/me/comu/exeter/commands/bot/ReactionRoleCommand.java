package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReactionRoleCommand implements ICommand {
    public static String messageID;
    public static String roleID;
    public static Emote emoji;

    // make this into an event waiter
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to create reaction roles.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to create reaction roles.").build()).queue();
            return;
        }
        if (args.size() != 3 || event.getMessage().getEmotes().isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a valid: message-id, role-id, and emoji (in that order).").build()).queue();
            return;
        }
        messageID = args.get(0);
        roleID = args.get(1);
        emoji = event.getMessage().getEmotes().get(0);
        event.getChannel().retrieveMessageById(args.get(0)).queue(success -> {
            success.clearReactions().queue();
            success.addReaction(event.getMessage().getEmotes().get(0)).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully set up your reaction role message!").build()).queue();
        }, error -> event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find that message, maybe it was deleted.").build()).queue());


    }


    @Override
    public String getHelp() {
        return "Creates a reaction role with a message\n`" + Core.PREFIX + getInvoke() + " [message-id] <role-id> (emoji)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "reactionrole";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"rr", "rradd", "reactionroleadd"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
