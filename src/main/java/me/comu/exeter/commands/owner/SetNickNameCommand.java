package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SetNickNameCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a nickname").build()).queue();
            return;
        }
        String message = event.getMessage().getContentRaw().substring(Core.PREFIX.length() + 10).trim();
        if (message.equals("resetnick")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Resetting nicknames, this may take a while.").build()).queue();
            for (Member m : event.getGuild().getMembers()) {
                if (event.getGuild().getSelfMember().canInteract(m)) {
                    m.modifyNickname(m.getUser().getName()).queue();
                }
            }
            return;
        }
        event.getChannel().sendMessageEmbeds(Utility.embed("Changing nicknames, this may take a while.").build()).queue();
        for (Member m : event.getGuild().getMembers()) {
            if (event.getGuild().getSelfMember().canInteract(m)) {
                m.modifyNickname(message).queue();
            }
        }

    }

    @Override
    public String getHelp() {
        return "Sets everyone's nickname to the specified nickname.\n`" + Core.PREFIX + getInvoke() + " [nickname]/[resetnick]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "changenick";
    }

    @Override
    public String[] getAlias() {
        return new String[]{};
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
