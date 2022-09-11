package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FakeAddressGeneratorCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String api = "https://raw.githubusercontent.com/EthanRBrown/rrad/master/addresses-us-1000.min.json";
        Request request = new Request.Builder().url(api).get().build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JSONArray jsonArray = new JSONArray(jsonResponse);
            } else {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Response was unsuccessful. Something went wrong ig").build()).queue();
                System.out.println(response.message());
            }
        } catch (Exception ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong with the endpoint").build()).queue();
            ex.printStackTrace();
        }
        event.getChannel().sendMessage(".").queue();
    }

    @Override
    public String getHelp() {
        return "Returns a fake address\n" + "`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "fakeaddress";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"genaddress","genaddy","fakeaddy"};
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
