package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class HastebinCommand implements ICommand {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Missing arguments").build()).queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.forEach(stringJoiner::add);
        final String body = stringJoiner.toString();
        try {
            event.getChannel().sendMessage(Utility.createPaste(body, false)).queue();
        } catch (IOException ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Connection throttled when making GET request").build()).queue();
            ex.printStackTrace();
        }
//        this.createPaste(body, (text) -> channel.sendMessage(text).queue());
    }


//    private void createPaste(String text, Consumer<String> callback) {
//        Request request = WebUtils.defaultRequest()
//                .post(RequestBody.create(null, text.getBytes()))
//                .addHeader("Content-Type", ContentType.TEXT_PLAIN.getType())
//                .url("https://hasteb.in/" + "documents")
//                .build();
//
//        WebUtils.ins.prepareRaw(request, (r) -> WebParserUtils.toJSONObject(r, new ObjectMapper())).async(
//                (json) -> {
//                    String key = json.get("key").asText();
//
//                    callback.accept("https://hasteb.in/" + key);
//                },
//                (e) -> callback.accept("Error: " + e.getMessage())
//        );
//    }


    @Override
    public String getHelp() {
        return "Creates a hastebin link with the specified content\n`" + Core.PREFIX + getInvoke() + "[content]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "hastebin";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"pastebin", "haste", "paste"};
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
