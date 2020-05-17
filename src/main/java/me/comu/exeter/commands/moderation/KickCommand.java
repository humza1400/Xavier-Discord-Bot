package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KickCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (args.isEmpty()) {
            channel.sendMessage("Please specify a valid user to kick").queue();
            return;
        }


        String reason = String.join(" ", args.subList(1, args.size()));

        if (!Objects.requireNonNull(member).hasPermission(Permission.KICK_MEMBERS) && Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            channel.sendMessage("You don't have permission to kick someone").queue();
            return;
        }


        if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I don't have permissions to kick that user").queue();
            return;
        }
        if (mentionedMembers.isEmpty() && !args.isEmpty())
        {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty())
            {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0).replaceAll("@everyone", "@\u200beveryone").replaceAll("@here","\u200bhere")).queue();
                return;
            } else if (targets.size() > 1)
            {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            Member target = targets.get(0);
            if (reason.equals("")) {
                try {
                    if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                        event.getChannel().sendMessage("You don't have permission to kick that user").queue();
                        return;
                    }
                event.getGuild().kick(target, String.format("Kicked by %#s", event.getAuthor())).queue();
                } catch (HierarchyException ex)
                {
                    event.getChannel().sendMessage("My role is not high enough to kick that user!").queue();
                    return;
                }
                channel.sendMessage(String.format("Kicked %#s", target.getUser())).queue();
            }
            else {
                try {
                    if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                        event.getChannel().sendMessage("You don't have permission to kick that user").queue();
                        return;
                    }
                event.getGuild().kick(target, String.format("Kicked by %#s for %s", event.getAuthor(), reason)).queue();
                } catch (HierarchyException ex)
                {
                    event.getChannel().sendMessage("My role is not high enough to kick that user!").queue();
                    return;
                }
                channel.sendMessage(String.format("Kicked %#s for %s", target.getUser(), reason)).queue();}
            return;
        }
        Member target = mentionedMembers.get(0);
        if (reason.equals("")) {
            try {
                if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                    event.getChannel().sendMessage("You don't have permission to kick that user").queue();
                    return;
                }
            event.getGuild().kick(target, String.format("Kicked by %#s", event.getAuthor())).queue();
            } catch (HierarchyException ex)
            {
                event.getChannel().sendMessage("My role is not high enough to kick that user!").queue();
                return;
            }
            channel.sendMessage(String.format("Kicked %#s", target.getUser())).queue();
        }
        else {
            try {
                if (!Objects.requireNonNull(event.getMember()).canInteract(target)) {
                    event.getChannel().sendMessage("You don't have permission to kick that user").queue();
                    return;
                }
            event.getGuild().kick(target, String.format("Kicked by %#s for %s", event.getAuthor(), reason)).queue();
            } catch (HierarchyException ex)
            {
                event.getChannel().sendMessage("My role is not high enough to ban that user!").queue();
                return;
            }
            channel.sendMessage(String.format("Kicked %#s for %s", target.getUser(), reason)).queue();}


    }

    @Override
    public String getHelp() {
        return "Kicks the specified user\n" + "`"  + Core.PREFIX + getInvoke() + " [user] <reason>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

     @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
