package me.comu.exeter.commands.music;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class DiscriminatorCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

    }
    private String parseId(String[] args, MessageReceivedEvent e) {
        String target;

        if (args.length == 0)
            target = e.getAuthor().getDiscriminator();
        else if (args[0].length() == 4 && e.getMessage().getMentionedUsers().isEmpty())
            target = args[0];
        else if (args[0].length() == 18 && e.getMessage().getMentionedUsers().isEmpty())
            target = e.getGuild().getMemberById(args[0]).getUser().getDiscriminator();
        else if(!e.getMessage().getMentionedUsers().isEmpty()){
            List<User> mention = e.getMessage().getMentionedUsers();
            if(e.getJDA().getSelfUser().getId().equals(mention.get(0).getId()) && mention.size()>1)
                target = mention.get(1).getDiscriminator();
            else
                target = mention.get(0).getDiscriminator();
        } else {
            target = "";
        }
        return target;
    }







    @Override
    public String getHelp() {
        return "Used for getting a list of users or bots with the same discriminator\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: " + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "discrim";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"discriminator","tag"};
    }
}
