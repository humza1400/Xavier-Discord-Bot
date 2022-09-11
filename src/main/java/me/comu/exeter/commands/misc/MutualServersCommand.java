package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MutualServersCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            List<Guild> mutualGuilds = Objects.requireNonNull(event.getMember()).getUser().getMutualGuilds();
            StringBuilder stringBuffer = new StringBuilder();
            for (Guild guild : mutualGuilds)
                stringBuffer.append("**").append(guild.getName()).append("** (").append(guild.getId()).append(")\n");
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(stringBuffer.toString()).setTitle("Mutual Guilds with " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).setColor(Core.getInstance().getColorTheme()).build()).queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (!memberList.isEmpty()) {
            List<Guild> mutualGuilds = memberList.get(0).getUser().getMutualGuilds();
            StringBuilder stringBuffer = new StringBuilder();
            for (Guild guild : mutualGuilds)
                stringBuffer.append("**").append(guild.getName()).append("** (").append(guild.getId()).append(")\n");
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(stringBuffer.toString()).setTitle("Mutual Guilds with " + memberList.get(0).getUser().getName() + "#" + memberList.get(0).getUser().getDiscriminator()).setColor(Core.getInstance().getColorTheme()).build()).queue();
            return;
        }
        if (!args.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Couldn't find the user " + Utility.removeMentions(args.get(0) + ".")).build()).queue();
                return;
            }
            List<Guild> mutualGuilds = targets.get(0).getUser().getMutualGuilds();
            StringBuilder stringBuffer = new StringBuilder();
            for (Guild guild : mutualGuilds)
                stringBuffer.append("**").append(guild.getName()).append("** (").append(guild.getId()).append(")\n");
            event.getChannel().sendMessageEmbeds(Utility.embedMessage(stringBuffer.toString()).setTitle("Mutual Guilds with " + targets.get(0).getUser().getName() + "#" + targets.get(0).getUser().getDiscriminator()).setColor(Core.getInstance().getColorTheme()).build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Checks the mutual servers a user has with the bot\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "mutual";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"mutuals", "mutualservers"};
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
