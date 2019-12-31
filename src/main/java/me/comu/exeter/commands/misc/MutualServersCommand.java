package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MutualServersCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            List<Guild> mutualGuilds = event.getMember().getUser().getMutualGuilds();
            StringBuffer stringBuffer = new StringBuffer();
            for (Guild guild : mutualGuilds)
                stringBuffer.append("**" + guild.getName() + "** (" + guild.getId() + ")\n");
            event.getChannel().sendMessage(EmbedUtils.embedMessage(stringBuffer.toString()).setTitle("Mutual Guilds with " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator()).build()).queue();
            return;
        }
        List<Member> memberList = event.getMessage().getMentionedMembers();
        if (!memberList.isEmpty()) {
                List<Guild> mutualGuilds = memberList.get(0).getUser().getMutualGuilds();
                StringBuffer stringBuffer = new StringBuffer();
                for (Guild guild : mutualGuilds)
                    stringBuffer.append("**" + guild.getName() + "** (" + guild.getId() + ")\n");
                event.getChannel().sendMessage(EmbedUtils.embedMessage(stringBuffer.toString()).setTitle("Mutual Guilds with " + memberList.get(0).getUser().getName() + "#" + memberList.get(0).getUser().getDiscriminator()).build()).queue();
                return;
        }
        if (!args.isEmpty() && memberList.isEmpty()) {
            List<Member> targets = event.getGuild().getMembersByName(args.get(0), true);
            if (targets.isEmpty()) {
                event.getChannel().sendMessage("Couldn't find the user " + args.get(0)).queue();
                return;
            } else if (targets.size() > 1) {
                event.getChannel().sendMessage("Multiple users found! Try mentioning the user instead.").queue();
                return;
            }
            List<Guild> mutualGuilds = targets.get(0).getUser().getMutualGuilds();
            StringBuffer stringBuffer = new StringBuffer();
            for (Guild guild : mutualGuilds)
                stringBuffer.append("**" + guild.getName() + "** (" + guild.getId() + ")\n");
            event.getChannel().sendMessage(EmbedUtils.embedMessage(stringBuffer.toString()).setTitle("Mutual Guilds with " + targets.get(0).getUser().getName() + "#" + targets.get(0).getUser().getDiscriminator()).build()).queue();
            return;
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
}
