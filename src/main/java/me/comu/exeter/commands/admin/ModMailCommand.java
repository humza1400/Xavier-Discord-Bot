package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ModMailCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) && event.getMember().getIdLong() != Core.OWNERID) {
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please insert a message to send as Mod Mail!").build()).queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        String message = stringJoiner.toString();
        for (Member member : event.getGuild().getMembers()) {
            if (member.hasPermission(Permission.ADMINISTRATOR) && !member.getUser().isBot()) {
                member.getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(Utility.embed(message).build())).queue(null,
                        failure -> event.getChannel().sendMessageEmbeds(Utility.errorEmbed(member.getUser().getAsTag() + " has their DMs disabled.").build()).queue());
            }

        }
        event.getChannel().sendMessageEmbeds(Utility.embed("Successfully sent out a mod mail!\nExecuted by: " + event.getAuthor().getAsTag()).build()).queue();
    }

    @Override
    public String getHelp() {
        return "Sends a direct message to all admins\n`" + Core.PREFIX + getInvoke() + " [message]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "modmail";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public Category getCategory() {
        return Category.OWNER;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
