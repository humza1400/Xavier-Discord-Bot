package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RaidActionCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to use raid actions.").build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to execute raid actions.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify an action-type.").build()).queue();
            return;
        }
        List<Member> members = event.getGuild().getMembers();
        List<Member> newAccountMembers = new ArrayList<>();
        List<Member> newJoinedMembers = new ArrayList<>();
        for (Member member : members) {
            if (!member.getTimeCreated().isBefore(OffsetDateTime.now().minusDays(7))) {
                newAccountMembers.add(member);
            }
        }
        for (Member member : members) {
            if (!member.getTimeJoined().isBefore(OffsetDateTime.now().minusMinutes(5))) {
                newJoinedMembers.add(member);
            }
        }
        event.getChannel().getHistory().retrievePast(100).queue((messages -> {

        }));
        if (args.get(0).equalsIgnoreCase("created") || args.get(0).equalsIgnoreCase("create")) {
            StringBuilder buffer = new StringBuilder("Suspected raid accounts made in the last 7 days: `" + newAccountMembers.size() + "`.\n");
            for (Member member : newAccountMembers) {
                //DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy h:mm a");
                buffer.append(String.format("%#s\n" /*+ member.getTimeCreated().format(timeFormatter) + "\n"*/, member.getUser()));
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(buffer.toString()).build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("joined") || args.get(0).equalsIgnoreCase("join")) {
            StringBuilder buffer = new StringBuilder("Suspected raid accounts joined in the last 5 minutes: `" + newJoinedMembers.size() + "`.\n");
            for (Member member : newJoinedMembers) {
                //DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy h:mm a");
                buffer.append(String.format("%#s\n" /*+ member.getTimeCreated().format(timeFormatter) + "\n"*/, member.getUser()));
            }
            event.getChannel().sendMessageEmbeds(Utility.embed(buffer.toString()).build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("kickjoined") || args.get(0).equalsIgnoreCase("kickjoin")) {
            if (newJoinedMembers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No accounts joined in the last 5 minutes to kick.").build()).queue();
                return;
            }
            for (Member member : newJoinedMembers) {
                event.getGuild().kick(member, "Raid attempt").queue();
            }
            event.getChannel().sendMessageEmbeds(Utility.embed("Kicked **" + newJoinedMembers.size() + "** accounts.").build()).queue();
        }
        if (args.get(0).equalsIgnoreCase("banjoined") || args.get(0).equalsIgnoreCase("banjoin")) {
            if (newJoinedMembers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No accounts joined in the last 5 minutes to ban.").build()).queue();
                return;
            }
            for (Member member : newJoinedMembers) {
                event.getGuild().ban(member, 7, "Raid attempt").queue();
            }
            event.getChannel().sendMessageEmbeds(Utility.embed("Banned **" + newJoinedMembers.size() + "** accounts.").build()).queue();
        }
        if (args.get(0).equalsIgnoreCase("kickcreated") || args.get(0).equalsIgnoreCase("kickcreate")) {
            if (newAccountMembers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No accounts made in the last 7 days to kick.").build()).queue();
                return;
            }
            for (Member member : newAccountMembers) {
                event.getGuild().kick(member, "Raid attempt").queue();
            }
            event.getChannel().sendMessageEmbeds(Utility.embed("Kicked **" + newAccountMembers.size() + "** accounts.").build()).queue();
        }
        if (args.get(0).equalsIgnoreCase("bancreated") || args.get(0).equalsIgnoreCase("bancreate")) {
            if (newAccountMembers.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No accounts made in the last 7 days to ban.").build()).queue();
                return;
            }
            for (Member member : newAccountMembers) {
                event.getGuild().ban(member, 7, "Raid attempt").queue();
            }
            event.getChannel().sendMessageEmbeds(Utility.embed("Banned **" + newAccountMembers.size() + "** accounts").build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Executes a raid-action if server is getting raided\n`" + Core.PREFIX + getInvoke() + " [argument-type]`\n" + "`" + getInvoke() + "` created/joined\n`" + getInvoke() + "` kickcreated/kickjoined\n`" + getInvoke() + "` bancreated/banjoined\n" + "Aliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "raidaction";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ra", "raid-action"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
