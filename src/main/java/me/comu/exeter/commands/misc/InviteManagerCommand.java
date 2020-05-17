package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InviteManagerCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
            if (args.isEmpty())
            {
                event.getGuild().retrieveInvites().queue((invites -> {
                    int index = 0;
                    for (Invite i : invites)
                    {
                        if (Objects.requireNonNull(i.getInviter()).getId().equals(event.getAuthor().getId()))
                        index += i.getUses();
                    }
                    event.getChannel().sendMessage("You have **" + index + "** invites!").queue();
                }));
            } else if (!event.getMessage().getMentionedMembers().isEmpty())
            {
                event.getGuild().retrieveInvites().queue((invites -> {
                    int index = 0;
                    for (Invite i : invites)
                    {
                        if (Objects.requireNonNull(i.getInviter()).getId().equals(event.getMessage().getMentionedMembers().get(0).getId()))
                            index += i.getUses();
                    }
                    event.getChannel().sendMessage(event.getMessage().getMentionedMembers().get(0).getAsMention() + " has **" + index + "** invites!").queue();
                }));
            } else if (!args.isEmpty() && event.getMessage().getMentionedMembers().isEmpty())
            {
                    List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
                    if (targets.isEmpty()) {
                        event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                        return;
                    } else if (targets.size() > 1) {
                        event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                        return;
                    }
                event.getGuild().retrieveInvites().queue((invites -> {
                    int index = 0;
                    for (Invite i : invites)
                    {
                        if (Objects.requireNonNull(i.getInviter()).getId().equals(targets.get(0).getId()))
                            index += i.getUses();
                    }
                    event.getChannel().sendMessage(targets.get(0).getAsMention() + " has **" + index + "** invites!").queue();
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
}
