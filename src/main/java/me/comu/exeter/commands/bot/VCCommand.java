package me.comu.exeter.commands.bot;

import me.comu.exeter.commands.admin.CreateAChannelCommand;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class VCCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I'm lacking the `MANAGE_CHANNEL` Permission.").build()).queue();
            return;
        }

        if (!args.isEmpty() && (args.get(0).equalsIgnoreCase("owner") || args.get(0).equalsIgnoreCase("whoowner") || args.get(0).equalsIgnoreCase("whoowns") || args.get(0).equalsIgnoreCase("ownership") || args.get(0).equalsIgnoreCase("own"))) {
            if (args.size() == 1) {
                if (!CreateAChannelCommand.vcMap.containsKey(event.getAuthor().getId())) {
                    event.getChannel().sendMessageEmbeds(Utility.embed("You don't currently own any vc.").build()).queue();
                    return;
                } else {
                    VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(CreateAChannelCommand.vcMap.get(event.getAuthor().getId()));
                    if (voiceChannel == null) {
                        event.getChannel().sendMessageEmbeds(Utility.embed("You don't currently own any vc.").build()).queue();
                        return;
                    }

                    event.getChannel().sendMessageEmbeds(Utility.embed(":loud_sound: You currently own `" + Utility.removeMarkdown(voiceChannel.getName()) + "` (" + voiceChannel.getId() + ").").build()).queue();
                }
            } else if (!event.getMessage().getMentionedMembers().isEmpty()) {
                Member member = event.getMessage().getMentionedMembers().get(0);
                if (!CreateAChannelCommand.vcMap.containsKey(member.getId())) {
                    event.getChannel().sendMessageEmbeds(Utility.embed(member.getUser().getAsTag() + " doesn't own any VC.").build()).queue();
                    return;
                } else {
                    VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(CreateAChannelCommand.vcMap.get(member.getId()));
                    if (voiceChannel == null) {
                        event.getChannel().sendMessageEmbeds(Utility.embed(member.getUser().getAsTag() + " doesn't own any VC.").build()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(Utility.embed(":loud_sound: " + member.getUser().getAsTag() + " currently owns `" + Utility.removeMarkdown(voiceChannel.getName()) + "` (" + voiceChannel.getId() + ").").build()).queue();
                }
            } else if (args.size() == 2) {
                try {
                    Long.parseLong(args.get(1));
                    if (CreateAChannelCommand.vcMap.containsValue(args.get(1))) {
                        Member member = event.getGuild().getMemberById(Objects.requireNonNull(Utility.getKeyByValue(CreateAChannelCommand.vcMap, args.get(1))));
                        if (member == null) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong, I'm not sure what.").build()).queue();
                            return;
                        }
                        VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(CreateAChannelCommand.vcMap.get(member.getId()));
                        if (voiceChannel == null) {
                            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong, I'm not sure what.").build()).queue();
                            return;
                        }
                        event.getChannel().sendMessageEmbeds(Utility.embed(":loud_sound: " + member.getUser().getAsTag() + " currently owns `" + Utility.removeMarkdown(voiceChannel.getName()) + "` (" + voiceChannel.getId() + ").").build()).queue();
                    }

                } catch (NumberFormatException ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid voice-channel ID, try again.").build()).queue();
                }
            }
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed(getHelp()).build()).queue();
            return;
        }

        if (!CreateAChannelCommand.isCacSet(event.getGuild())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You can't manage a vc with no CAC channel set, sorry bro.").build()).queue();
            return;
        }

        if (!CreateAChannelCommand.vcMap.containsKey(event.getAuthor().getId())) {
            event.getChannel().sendMessageEmbeds(Utility.embed("You don't currently own any vc.").build()).queue();
            return;
        }

        VoiceChannel voiceChannel = event.getGuild().getVoiceChannelById(CreateAChannelCommand.vcMap.get(event.getAuthor().getId()));
        if (voiceChannel == null) {
            event.getChannel().sendMessageEmbeds(Utility.embed(":x: Something went wrong and I couldn't find your channel.").build()).queue();
            return;
        }
        if (args.get(0).equalsIgnoreCase("lock") || args.get(0).equalsIgnoreCase("private") || args.get(0).equalsIgnoreCase("priv")) {
            voiceChannel.upsertPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VOICE_CONNECT).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed(":lock: Successfully locked " + voiceChannel.getName()).build()).queue();
        } else if (args.get(0).equalsIgnoreCase("unlock") || args.get(0).equalsIgnoreCase("open")) {
            voiceChannel.upsertPermissionOverride(event.getGuild().getPublicRole()).setAllow(Permission.VOICE_CONNECT).queue();
            event.getChannel().sendMessageEmbeds(Utility.embed(":lock: Successfully unlocked " + voiceChannel.getName()).build()).queue();
        } else if (args.get(0).equalsIgnoreCase("setlimit") || args.get(0).equalsIgnoreCase("cap") || args.get(0).equalsIgnoreCase("userlimit") || args.get(0).equalsIgnoreCase("limit")) {
            if (args.size() < 2) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please specify what to set the user-limit to.").build()).queue();
                return;
            }
            try {
                int limit = Integer.parseInt(args.get(1));
                if (limit < 0 || limit > 99) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please specify a valid number (0-99) to cap the user-limit to.").build()).queue();
                } else {
                    voiceChannel.getManager().setUserLimit(limit).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed(":speaking_head: Set the limit of " + voiceChannel.getName() + " to `" + limit + "`.").build()).queue();
                }
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please specify a valid number (0-99) to cap the user-limit to").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("invite") || args.get(0).equalsIgnoreCase("add")) {
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please mention a member you'd like to allow access to your VC.").build()).queue();
            } else {
                voiceChannel.upsertPermissionOverride(event.getGuild().getPublicRole()).setAllow(Permission.VOICE_CONNECT).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(":man_standing: " + event.getMessage().getMentionedMembers().get(0).getUser().getAsTag() + " has been granted access to " + voiceChannel.getName() + ".").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("kick") || args.get(0).equalsIgnoreCase("remove")) {
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please mention a member you'd like to kick from your VC.").build()).queue();
            } else {
                Member member = event.getMessage().getMentionedMembers().get(0);
                if (voiceChannel.getMembers().contains(member)) {
                    event.getGuild().kickVoiceMember(member).queue();
                    event.getChannel().sendMessageEmbeds(Utility.embed(":boot: Successfully kicked " + member.getUser().getAsTag() + " from " + voiceChannel.getName() + ".").build()).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Cannot kick that user as they are not currently in your VC.").build()).queue();
                }
            }
        } else if (args.get(0).equalsIgnoreCase("blacklist") || args.get(0).equalsIgnoreCase("ban")) {
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please mention a member you'd like to blacklist from your VC.").build()).queue();
            } else {
                Member member = event.getMessage().getMentionedMembers().get(0);
                if (voiceChannel.getMembers().contains(member)) {
                    event.getGuild().kickVoiceMember(member).queue();
                }
                voiceChannel.upsertPermissionOverride(member).setDeny(Permission.VOICE_CONNECT).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(":hammer: Successfully banned **" + member.getUser().getAsTag() + "** from " + voiceChannel.getName() + ".").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("unban") || args.get(0).equalsIgnoreCase("unblacklist")) {
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please mention a member you'd like to kick from your VC.").build()).queue();
            } else {
                Member member = event.getMessage().getMentionedMembers().get(0);
                voiceChannel.upsertPermissionOverride(member).clear(Permission.VOICE_CONNECT).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(":butterfly: Successfully unbanned **" + member.getUser().getAsTag() + "** from " + voiceChannel.getName()).build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("setowner") || args.get(0).equalsIgnoreCase("transferowner") || args.get(0).equalsIgnoreCase("giveowner") || args.get(0).equalsIgnoreCase("giveownership") || args.get(0).equalsIgnoreCase("transferownership")) {
            if (event.getMessage().getMentionedMembers().isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(":x: Please mention a member you'd like to set as the new owner.").build()).queue();
            } else {
                Member member = event.getMessage().getMentionedMembers().get(0);
                CreateAChannelCommand.vcMap.put(member.getId(), CreateAChannelCommand.vcMap.get(Objects.requireNonNull(event.getMember()).getId()));
                CreateAChannelCommand.vcMap.remove(Objects.requireNonNull(event.getMember()).getId());
                event.getChannel().sendMessageEmbeds(Utility.embed(event.getAuthor().getAsTag() + " has transferred ownership to **" + member.getUser().getAsTag() + "** of `" + voiceChannel.getName() + "`.").build()).queue();
            }
        } else if (args.get(0).equalsIgnoreCase("setname") || args.get(0).equalsIgnoreCase("name")) {
            if (args.size() < 2) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a VC name").build()).queue();
            } else {
                StringJoiner stringJoiner = new StringJoiner(" ");
                args.stream().skip(1).forEach(stringJoiner::add);
                voiceChannel.getManager().setName(stringJoiner.toString()).queue();
                event.getChannel().sendMessageEmbeds(Utility.embed(Utility.CHECKMARK_EMOTE + " Set VC name to: `" + Utility.removeMentionsAndMarkdown(stringJoiner.toString()) + "`").build()).queue();
            }
        }
    }

    @Override
    public String getHelp() {
        return "*Manage your Create-A-Channel Voice Channel*\n\n`" + getInvoke() + " setname <name>`\n`" + getInvoke() + " owner\n" + getInvoke() + " owner <user>/<channel-id>\n" + getInvoke() + " setowner <user>\n" + getInvoke() + " setlimit <cap>\n" + getInvoke() + " lock\n" + getInvoke() + " unlock\n" + getInvoke() + " add <user>\n" + getInvoke() + " kick <user>\n" + getInvoke() + " ban <user>\n" + getInvoke() + " unban <user>\n" + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "vc";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"pb", "cacvc", "vccac"};
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
