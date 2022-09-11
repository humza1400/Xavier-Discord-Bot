package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreateVoiceChannelCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID)) {
            return;
        }
        try {
            int input = Integer.parseInt(args.get(0));
            for (int i = 0; i < input; i++) {
                event.getGuild().createVoiceChannel("champagnepapi" + (this.getRandom())).setUserlimit(69).queue();
                if (i == input - 1) {
                    event.getMessage().delete().queue();
//                    EmbedBuilder eb = new EmbedBuilder();
//                    eb.setColor(346626);
//                    eb.setDescription(String.format("✅ Successfully created %s voice channel(s) ✅", args.get(0)));
//                    event.getChannel().sendMessage(eb.build()).queue();
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Insert an amount of voice channels to create, dummy!").build()).queue();
        }

    }
    private int getRandom() {
        Random randy = new Random();
        return randy.nextInt();
    }

    @Override
    public String getHelp() {
        return "Creates the specified amounts of voice channels\n`" + Core.PREFIX + getInvoke() + " [amount]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "cvc";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"createvoice","createvoicechannel","createvc"};
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
