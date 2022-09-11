package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DiscriminatorCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a discriminator.").build()).queue();
            return;
        }
        if (args.get(0).length() != 4) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please specify a valid 4-digit discriminator.").build()).queue();
            return;
        }
        if (!StringUtils.isNumeric(args.get(0))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Discriminators don't have letters in them, please specify a valid discriminator.").build()).queue();
            return;
        }
        String tag = args.get(0);
        List<String> userIDS = Core.getInstance().getJDA().getUsers().stream().filter(user -> user.getDiscriminator().equals(tag)).map(ISnowflake::getId).collect(Collectors.toList());
        if (userIDS.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("No user found in cache with `" + tag + "` tag.").build()).queue();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(userIDS.size() + " users found with the #" + tag + " tag:\n");
        for (String string : userIDS) {
            Core.getInstance().getJDA().retrieveUserById(string).queue((user) -> stringBuilder.append(user.getAsTag()).append("\n"));
        }
        if (stringBuilder.toString().length() > 1999) {
            try {
                event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(Utility.createPaste(stringBuilder.toString(), false)).setColor(Core.getInstance().getColorTheme()).setTitle(userIDS.size() + " users found with the `#" + tag + "` discriminator:").build()).queue();
            } catch (IOException ex) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Connection throttled when making GET request").build()).queue();
                ex.printStackTrace();
            }
        } else
            event.getChannel().sendMessageEmbeds(new EmbedBuilder().setDescription(stringBuilder.toString().replace(userIDS.size() + " users found with the #" + tag + " tag:", "")).setColor(Core.getInstance().getColorTheme()).setTitle(userIDS.size() + " users found with the #" + tag + " discriminator:").build()).queue();
    }


    @Override
    public String getHelp() {
        return "Used for getting a list of users or bots with the same discriminator\n`" + Core.PREFIX + getInvoke() + " [user]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "discriminator";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"discrim"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
