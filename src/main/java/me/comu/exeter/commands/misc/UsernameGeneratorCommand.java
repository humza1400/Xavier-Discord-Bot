package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.List;

public class UsernameGeneratorCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            File file = new File("D:/pfps/usernames.txt");
            RandomAccessFile f = new RandomAccessFile(file, "r");
            final long randomLocation = (long) (Math.random() * f.length());
            f.seek(randomLocation);
            f.readLine();
            String randomLine = f.readLine();
            event.getChannel().sendMessageEmbeds(Utility.embed("Random username: **" + randomLine + "**").build()).queue();
            f.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong, sorry :(").build()).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Spits out a random username\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "usernamegenerator";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"genuser","genusername","randomuser","randomusername","usernamegen"};
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
