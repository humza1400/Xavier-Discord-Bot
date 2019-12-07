package me.comu.exeter.commands;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserInfoCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
        List<Member> memberList = event.getMessage().getMentionedMembers();
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                try {
                MessageEmbed embed = new EmbedBuilder().setColor(event.getMember().getColor())
                        .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                        .addField("Username", event.getMember().getUser().getAsTag(), false)
                        .addField("User ID", String.format("%s %s", event.getAuthor().getId(), event.getMember().getAsMention()), false)
                        .addField("Account Created", event.getMember().getUser().getTimeCreated().format(timeFormatter), false)
                        .addField("Joined Server", event.getMember().getTimeJoined().format(timeFormatter), false)
                        .addField("Online Status", event.getMember().getOnlineStatus().name().replaceAll("_", " "), false)
                        .addField("Game:", displayGameInfo(event.getMember()), false)
                        .addField(String.format("Roles: (%s)", event.getMember().getRoles().size(), event.getMember().getRoles().size()), getRolesAsString(event.getMember().getRoles()), false)
                        .addField("Bot Account", event.getMember().getUser().isBot() ? "Yes" : "No", false).build();
                    event.getChannel().sendMessage(embed).queue();
                } catch (IllegalArgumentException ex) {
                    MessageEmbed embedEx = new EmbedBuilder().setColor(event.getMember().getColor())
                            .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                            .addField("Username", event.getMember().getUser().getAsTag(), false)
                            .addField("User ID", String.format("%s %s", event.getAuthor().getId(), event.getMember().getAsMention()), false)
                            .addField("Account Created", event.getMember().getUser().getTimeCreated().format(timeFormatter), false)
                            .addField("Joined Server", event.getMember().getTimeJoined().format(timeFormatter), false)
                            .addField("Online Status", event.getMember().getOnlineStatus().name().replaceAll("_", " "), false)
                            .addField("Game:", displayGameInfo(event.getMember()), false)
                            .addField(String.format("Roles: (%s)", event.getMember().getRoles().size()), "Too many to display", false)
                            .addField("Bot Account", event.getMember().getUser().isBot() ? "Yes" : "No", false).build();
                    event.getChannel().sendMessage(embedEx).queue();
                }
            }
            else {
                try {
                MessageEmbed embed = new EmbedBuilder().setColor(memberList.get(0).getColor())
                        .setThumbnail(memberList.get(0).getUser().getEffectiveAvatarUrl())
                        .addField("Username", memberList.get(0).getUser().getAsTag(), false)
                        .addField("User Id", String.format("%s %s", memberList.get(0).getId(), memberList.get(0).getAsMention()), false)
                        .addField("Account Created", memberList.get(0).getUser().getTimeCreated().format(timeFormatter), false)
                        .addField("Joined Server", memberList.get(0).getTimeJoined().format(timeFormatter), false)
                        .addField("Online Status", memberList.get(0).getOnlineStatus().name().replaceAll("_", " "), false)
                        .addField("Game:", displayGameInfo(memberList.get(0)), false)
                        .addField(String.format("Roles: (%s)", event.getMember().getRoles().size()), getRolesAsString(memberList.get(0).getRoles()), false)
                        .addField("Bot Account", memberList.get(0).getUser().isBot() ? "Yes" : "No", false)
                        .build();
                    event.getChannel().sendMessage(embed).queue();
                } catch (IllegalArgumentException ex)
                {
                    MessageEmbed embedEx = new EmbedBuilder().setColor(memberList.get(0).getColor())
                            .setThumbnail(memberList.get(0).getUser().getEffectiveAvatarUrl())
                            .addField("Username", memberList.get(0).getUser().getAsTag(), false)
                            .addField("User Id", String.format("%s %s", memberList.get(0).getId(), memberList.get(0).getAsMention()), false)
                            .addField("Account Created", memberList.get(0).getUser().getTimeCreated().format(timeFormatter), false)
                            .addField("Joined Server", memberList.get(0).getTimeJoined().format(timeFormatter), false)
                            .addField("Online Status", memberList.get(0).getOnlineStatus().name().replaceAll("_", " "), false)
                            .addField("Game:", displayGameInfo(memberList.get(0)), false)
                            .addField(String.format("Roles: (%s)", event.getMember().getRoles().size()), getRolesAsString(memberList.get(0).getRoles()), false)
                            .addField("Bot Account", memberList.get(0).getUser().isBot() ? "Yes" : "No", false)
                            .build();
                        event.getChannel().sendMessage(embedEx).queue();
                }
            }

    }
    private String displayGameInfo(Member name){
        try{
            String game = name.getActivities().get(0).getName();
            return "Playing: " + game;
        }catch (NullPointerException | IndexOutOfBoundsException exx){
            return "None";
        }
    }
    private String getRolesAsString(List rolesList){
        String roles;
        if (!rolesList.isEmpty()){
            Role tempRole = (Role) rolesList.get(0);
            roles = tempRole.getName();
            for (int i = 1; i < rolesList.size(); i++){
                tempRole = (Role) rolesList.get(i);
                roles = roles + ", " + tempRole.getName();
            }
        }else{
            roles = "No Roles";
        }
        return roles;
    }

    @Override
    public String getHelp() {
        return "Gets information about a user\n" + '`' + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "info";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"userinfo","whois"};
    }
}
