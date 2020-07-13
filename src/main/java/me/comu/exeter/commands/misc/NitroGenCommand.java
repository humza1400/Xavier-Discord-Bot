package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


/*
    fN9Rjw2Ek58AyXfb
    bzWZar77CrU8mdBb
    Dxet3Td2NYhsn7u4
    zHJCRGzCtFZ5ZF54
    kdPJr6ce2aCZmbyK
    EdwBGbZWnaER4Xgu
    Wvt3GMdEKj9jvGge
    Z7pc2rSmTvBa29zy
    zHJCRGzCtFZ5ZF54
    GKehmXSJNsZhsvsyXPxu8Htb
    3JYbudmXM5apvhPZ
 */

public class NitroGenCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String list = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        final String link = "https://discord.gift/";
        StringBuilder output = new StringBuilder(("Cracked Nitro Code: " + link));
            if (new Random().nextInt() % 2 == 0) {
                for (int i = 0; i < 16; i++) {
                    output.append(selectAChar(list));
                }
            } else {
                for (int i = 0; i < 24; i++) {
                    output.append(selectAChar(list));
                }
            }


        event.getChannel().sendMessage(output.toString()).queue();

    }
    private static char selectAChar(String s){

        Random random = new Random();
        int index = random.nextInt(s.length());
        return s.charAt(index);

    }

    @Override
    public String getHelp() {
        return "Tries to generate a discord nitro code.\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "nitrogen";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"gennitro","nitro"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
