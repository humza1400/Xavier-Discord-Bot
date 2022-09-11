package me.comu.exeter.commands.bot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class WebhookCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() != 2) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Please provide a webhook link and message").build()).queue();
            return;
        }
        try {
            StringJoiner stringJoiner = new StringJoiner(" ");
            args.stream().skip(1).forEach(stringJoiner::add);
            WebhookClient client = WebhookClient.withUrl(args.get(0));
            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            WebhookMessage message = builder.setContent(stringJoiner.toString()).build();
            client.send(message);
            client.close();
            event.getChannel().sendMessageEmbeds(Utility.embed("Successfully sent POST request").build()).queue();
        } catch (Exception ex)
        {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Invalid Webhook URL").build()).queue();
        }
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
