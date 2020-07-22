package me.comu.exeter.commands.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.natanbc.reliqua.request.PendingRequest;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.web.WebParserUtils;
import me.duncte123.botcommons.web.WebUtils;
import me.duncte123.botcommons.web.requests.JSONRequestBody;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.duncte123.botcommons.web.WebParserUtils.toJSONObject;

public class ShortenURLCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty() || !Utility.isUrl(args.get(0))) {
            event.getChannel().sendMessage("Please specify a valid URL to shorten").queue();
            return;
        }
        PendingRequest<String> pendingRequest = shortenUrl(args.get(0), new ObjectMapper());
        if (pendingRequest == null) {
            event.getChannel().sendMessage("I couldn't resolve the URL, sorry").queue();
            return;
        }
        pendingRequest.async((s) -> event.getChannel().sendMessage(Objects.requireNonNullElse(s, "I couldn't resolve the URL, sorry")).queue());

    }

    public static PendingRequest<String> shortenUrl(String url, ObjectMapper mapper) {
        final ObjectNode json = mapper.createObjectNode();

        json.set("dynamicLinkInfo",
                mapper.createObjectNode()
                        .put("domainUriPrefix", "swagbot.page.link")
                        .put("link", url)
        );
        json.set("suffix",
                mapper.createObjectNode()
                        .put("option", "SHORT") // SHORT or UNGUESSABLE
        );

        try {
            return WebUtils.ins.postRequest("\u0068\u0074\u0074\u0070\u0073\u003a\u002f\u002f\u0066\u0069\u0072\u0065\u0062\u0061\u0073\u0065\u0064\u0079\u006e\u0061\u006d\u0069\u0063\u006c\u0069\u006e\u006b\u0073\u002e\u0067\u006f\u006f\u0067\u006c\u0065\u0061\u0070\u0069\u0073\u002e\u0063\u006f\u006d\u002f\u0076\u0031\u002f\u0073\u0068\u006f\u0072\u0074\u004c\u0069\u006e\u006b\u0073\u003f\u006b\u0065\u0079\u003d\u0041\u0049\u007a\u0061\u0053\u0079\u0042\u0046\u006a\u0057\u0055\u0032\u0033\u0070\u0068\u0052\u0066\u0059\u004d\u0042\u004b\u0078\u0043\u007a\u0059\u0054\u0056\u0053\u0071\u0055\u0069\u0078\u0053\u0031\u0030\u0052\u0053\u0049\u0055", JSONRequestBody.fromJackson(json))
                    .build(
                            (r) -> {
                                final ObjectNode response = toJSONObject(r, mapper);

                                if (response == null) {
                                    return "null";
                                }
                                return response.get("shortLink").asText();
                            },
                            WebParserUtils::handleError
                    );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public String getHelp() {
        return "Shortens the specified URL\n`" + Core.PREFIX + getInvoke() + " [URL]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "shortenurl";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bitly", "googl", "goo.gl", "bit.ly", "cutly", "cut.ly", "shorturl", "shorten", "urlshorten"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
