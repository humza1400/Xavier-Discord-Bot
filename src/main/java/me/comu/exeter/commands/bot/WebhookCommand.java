package me.comu.exeter.commands.bot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class WebhookCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        //File file = new File("C:/Users/player/Desktop/image0.gif");
        WebhookClient client = WebhookClient.withUrl("https://discordapp.com/api/webhooks/645872965602246656/EBM4j1VeFKW4Er13mv5qxxp7eSeLlt17O_UORyoDiWEKVXw4lP-4llPxVl1zUetI1r_v");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbed firstEmbed = new WebhookEmbedBuilder().setColor(0).setDescription("**React to gain access to " + event.getGuild().getName() + "**").setImageUrl("https://cdn.discordapp.com/attachments/307987620124688384/656152227957702676/tenor.gif")/*.setFooter(new WebhookEmbed.EmbedFooter("made by swag#3231",event.getJDA().getUserById("175728291460808706").getAvatarUrl()*/.build();
        builder.addEmbeds(firstEmbed);
        WebhookMessage message = builder.build();
        client.send(message);
        client.close();
    }

    @Override
    public String getHelp() {
        return "Sends a webhook request\n`" + Core.PREFIX + getInvoke() + " [message] `\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "webhook";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

  @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
