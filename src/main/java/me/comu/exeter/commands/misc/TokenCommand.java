package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TokenCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

        if (mentionedMembers.isEmpty()) {
        event.getChannel().sendMessage("Please specify a user").queue();
        } else {
            String list = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
            StringBuilder message = new StringBuilder(mentionedMembers.get(0).getAsMention() + "'s account token is: ");
            for (int i = 0; i < 59; i++)
                message.append(selectAChar(list));
            event.getChannel().sendMessage(message.toString()).queue();

    }

}
    private static char selectAChar(String s){

        Random random = new Random();
        int index = random.nextInt(s.length());
        return s.charAt(index);

    }


    @Override
    public String getHelp() {
        return "Resolve's a user's account token\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "token";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"gettoken","resolvetoken","grabtoken"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
