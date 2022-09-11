package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InviteManagerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getGuild().retrieveInvites().queue((invites -> {
                int index = 0;
                for (Invite i : invites) {
                    if (Objects.requireNonNull(i.getInviter()).getId().equals(event.getAuthor().getId()))
                        index += i.getUses();
                }
                event.getChannel().sendMessageEmbeds(Utility.embed("You have **" + index + "** invites!").build()).queue();
            }));
        } else if (!event.getMessage().getMentionedMembers().isEmpty()) {
            event.getGuild().retrieveInvites().queue((invites -> {
                int index = 0;
                for (Invite i : invites) {
                    if (Objects.requireNonNull(i.getInviter()).getId().equals(event.getMessage().getMentionedMembers().get(0).getId()))
                        index += i.getUses();
                }
                event.getChannel().sendMessageEmbeds(Utility.embed(event.getMessage().getMentionedMembers().get(0).getAsMention() + " has **" + index + "** invites!").build()).queue();
            }));
        } else if (!args.isEmpty() && event.getMessage().getMentionedMembers().isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            }
            event.getGuild().retrieveInvites().queue((invites -> {
                int index = 0;
                for (Invite i : invites) {
                    if (Objects.requireNonNull(i.getInviter()).getId().equals(targets.get(0).getId()))
                        index += i.getUses();
                }
                event.getChannel().sendMessageEmbeds(Utility.embed(targets.get(0).getAsMention() + " has **" + index + "** invites!").build()).queue();
            }));
        }
    }

    /*
    main.getGuild().retrieveInvites().queue((invites) ->
            {
                latestInvites.addAll(invites);
                ArrayList<Invite> missingElement = new ArrayList<>(CollectionUtils.subtract(invitesBefore, latestInvites));
                if (main.getverifiedMembers().containsKey(missingElement.get(0).getInviter())){
            He is the person that created the invite
                }
            });
     */
    @Override
    public String getHelp() {
        return "Returns the amount of people the user has invited to the server (not 100% accurate)\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "invites";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
