package me.comu.exeter.events;

import me.comu.exeter.commands.admin.AntiRaidCommand;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.commands.moderation.OffCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.util.CompositeKey;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OffEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (OffCommand.offedUsers.contains(event.getAuthor().getId())) {
            event.getMessage().delete().queue();
        }
        if (AntiRaidCommand.isActive() && event.getMessage().isWebhookMessage() && (!(event.getMessage().getMentionedUsers().isEmpty() || event.getMessage().getMentionedRoles().isEmpty()) || event.getMessage().getContentRaw().contains(".gg/"))) {
            event.getMessage().delete().queue();
            if (!event.getGuild().getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
                String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                Wrapper.sendPrivateMessage(event.getJDA(), userComu, "Someone may have just attempted to wizz in `" + event.getGuild().getName() + "`, and I don't have permission to do anything about it. **TYPE_WEBHOOK**");
                return;
            }
            event.getChannel().retrieveWebhooks().queue((webhooks -> {
                for (Webhook webhook : webhooks) {
                    event.getChannel().deleteWebhookById(webhook.getId()).queue((specificwebhook -> {
                        if (webhook.getOwner() != null && webhook.getOwner().getIdLong() != (Core.OWNERID) && !webhook.getOwner().getId().equals(event.getJDA().getSelfUser().getId()) && !webhook.getOwner().getId().equals(event.getGuild().getOwnerId()) && !Wrapper.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), webhook.getOwner().getId(), event.getGuild().getId())) {
                            if (event.getGuild().getSelfMember().canInteract(webhook.getOwner())) {
                                Member member = webhook.getOwner();
                                List<Role> roles = member.getRoles();
                                String[] stringArray = new String[member.getRoles().size()];
                                List<String> strings = Arrays.asList(stringArray);
                                for (int i = 0; i < roles.size(); i++) {
                                    stringArray[i] = roles.get(i).getName();
                                }
                                stringArray = strings.toArray(new String[0]);
                                if (member.getRoles().size() == 0) {
                                    event.getGuild().getPublicRole().getManager().setPermissions(0).givePermissions(Permission.CREATE_INSTANT_INVITE, Permission.MESSAGE_READ, Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD, Permission.VOICE_STREAM, Permission.NICKNAME_CHANGE).queue();
                                    stringArray[0] = "@\u200beveryone";
                                } else {
                                    for (Role role : member.getRoles()) {
                                        if (event.getGuild().getSelfMember().canInteract(role)) {
                                            if (role.isManaged() || role.isPublicRole()) {
                                                role.getManager().revokePermissions(Permission.values()).queue();
                                            }
                                            if (!role.isManaged()) {
                                                event.getGuild().removeRoleFromMember(member.getId(), role).queue();
                                            }
                                        }
                                    }
                                }
                                String rolesRemoved = (stringArray.length == 0) ? "@\u200beveryone" : Arrays.deepToString(stringArray);
                                String userComu = Objects.requireNonNull(event.getJDA().getUserById(Core.OWNERID)).getId();
                                String userOwner = Objects.requireNonNull(event.getGuild().getOwner()).getUser().getId();
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm:ss a MM/dd/yyyy");
                                LocalDateTime now = LocalDateTime.now();
                                String botCheck = member.getUser().isBot() ? "`Yes`" : "`No`";
                                Wrapper.sendPrivateMessage(event.getJDA(), userComu, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `WEBHOOK`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + rolesRemoved + "`");
                                Wrapper.sendPrivateMessage(event.getJDA(), userOwner, "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `WEBHOOK`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + rolesRemoved + "`");
                                if (!WhitelistCommand.getWhitelistedIDs().isEmpty()) {
                                    for (CompositeKey x : WhitelistCommand.getWhitelistedIDs().keySet()) {
                                        if (WhitelistCommand.getWhitelistedIDs().get(x).equals(event.getGuild().getId())) {
                                            User whitelistUser = event.getJDA().getUserById(x.getUserID());
                                            if (!Objects.requireNonNull(whitelistUser).isBot())
                                                Wrapper.sendPrivateMessage(event.getJDA(), Objects.requireNonNull(event.getJDA().getUserById(x.getUserID())).getId(), "**Anti-Raid Report For " + event.getGuild().getName() + "**\nWizzer: `" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + " (" + member.getId() + ")`\nWhen: `" + dtf.format(now) + "`" + "\nType: `WEBHOOK`\nBot: " + botCheck + "\nAction Taken: `Roles Removed`\nRoles Removed: `" + rolesRemoved + "`");
                                        }
                                    }
                                }
                            }
                        }
                    }));
                }
            }));
        }
    }
}
