package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class TranslateCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
      if (args.isEmpty())
      {
          event.getChannel().sendMessage("Please specify what you want translated").queue();
          return;
      }
        OkHttpClient client = new OkHttpClient();
        String language = args.get(0);
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        String arguments = stringJoiner.toString().replaceAll(" ", "%20");
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("format=text&source=en&q=%s&target=%s", arguments, language));
        // https://developers.google.com/custom-search/v1/using_rest
        // https://cloud.google.com/translate/docs/languages
        Request request = new Request.Builder()
                .url("https://www.googleapis.com/customsearch/v2?")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful())
            {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                Logger.getLogger().print(jsonResponse + "\n");
            } else {
                event.getChannel().sendMessage("Something went wrong when accessing the endpoint | " + response.code()).queue();
                System.out.println(Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
            event.getChannel().sendMessage("Something went wrong when translating").queue();
        }
    }

    @Override
    public String getHelp() {
        return "Translates the specified text\n`" + Core.PREFIX + getInvoke() + " [language] <translation>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "translate";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"trans"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
