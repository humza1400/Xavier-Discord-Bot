package me.comu.exeter.commands.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.duncte123.botcommons.web.ContentType;
import me.duncte123.botcommons.web.WebParserUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class HastebinCommand implements ICommand {

    private static final String HASTE_SERVER = "https://hasteb.in/";

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        final TextChannel channel = event.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        final String invoke = getInvoke();
        final String contentRaw = event.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(invoke) + invoke.length() + Core.PREFIX.length() + 1;
        final String body = contentRaw.substring(index).trim();

        this.createPaste(body, (text) -> channel.sendMessage(text).queue());
    }



    private void createPaste(String text, Consumer<String> callback) {
        Request request = WebUtils.defaultRequest()
                .post(RequestBody.create(null, text.getBytes()))
                .addHeader("Content-Type", ContentType.TEXT_PLAIN.getType())
                .url(HASTE_SERVER + "documents")
                .build();

        WebUtils.ins.prepareRaw(request, (r) -> WebParserUtils.toJSONObject(r, new ObjectMapper())).async(
                (json) -> {
                    String key = json.get("key").asText();

                    callback.accept(HASTE_SERVER + key);
                },
                (e) -> callback.accept("Error: " + e.getMessage())
        );
    }


    @Override
    public String getHelp() {
        return "Creates a hastebin link with the specified content\n`" + Core.PREFIX + getInvoke() + "[content]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "haste";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"paste"};
    }
}
