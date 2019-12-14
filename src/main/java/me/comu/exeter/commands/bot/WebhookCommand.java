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
        WebhookClient client = WebhookClient.withUrl("https://discordapp.com/api/webhooks/653024205410926603/pEXCVelQ2m6nyXIu-8tX-NaUQb9-CUMWaTL12JJvLvTqOJWXfchSD6od_krhjpxvJNtq");
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbed firstEmbed = new WebhookEmbedBuilder().setColor(0).setDescription("**React to gain access to " + event.getGuild().getName() + "**").setImageUrl("https://cdn.discordapp.com/attachments/650905189628248079/653045824334462995/image0.gif").build();
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
        return "embedwebhook";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }
}
