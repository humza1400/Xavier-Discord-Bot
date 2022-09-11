package me.comu.exeter.commands.moderation;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RenameCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!Objects.requireNonNull(member).hasPermission(Permission.MANAGE_CHANNEL) && member.getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to change the channel name").build()).queue();
            return;
        }
        if (!selfMember.hasPermission(Permission.MANAGE_CHANNEL) && (!selfMember.hasPermission(Permission.MANAGE_CHANNEL))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I don't have permissions to change the channel name").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a channel name").build()).queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        args.forEach(stringBuilder::append);
        event.getChannel().getManager().setName(stringBuilder.toString()).queue(success -> event.getChannel().sendMessageEmbeds(Utility.embedMessage("Channel renamed to `" + stringBuilder + "`.").build()).queue());
    }

    @Override
    public String getHelp() {
        return "Renames the text-channel\n`" + Core.PREFIX + getInvoke() + " [name]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "rename";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"renamechannel", "channelrename", "renamechat"};
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
