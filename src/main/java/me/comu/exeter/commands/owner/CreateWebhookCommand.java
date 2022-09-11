package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreateWebhookCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        int input = Math.min(Integer.parseInt(args.get(0)), 10);
        for (int i = 0; i < input; i++) {
            try {
                event.getChannel().createWebhook("champagnepapi " + getRandom()).queue();
            } catch (Exception ignored) {

            }
        }
        event.getMessage().delete().queue();
//        EmbedBuilder eb = new EmbedBuilder();
//        eb.setColor(346626);
//        eb.setDescription(String.format("✅ Successfully created %s webhook(s) ✅", args.get(0)));
//        event.getChannel().sendMessage(eb.build()).queue();
    }
    private int getRandom() {
        Random randy = new Random();
        return randy.nextInt();
    }
    @Override
    public String getHelp() {
        return "Creates the designated amount of webhooks\n`" + Core.PREFIX + getInvoke() + " [amount] (max 10)`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "cweb";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"cwebhooks","createweb","createwebhooks","createwebhook","webhooks"};
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
