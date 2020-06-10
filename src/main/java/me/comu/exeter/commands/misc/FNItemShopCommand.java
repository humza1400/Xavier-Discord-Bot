package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FNItemShopCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String url = "https://api.fortnitetracker.com/v1/store";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("\u0054\u0052\u004e\u002d\u0041\u0070\u0069\u002d\u004b\u0065\u0079", "\u0033\u0064\u0066\u0037\u0037\u0066\u0039\u0063\u002d\u0035\u0036\u0062\u0035\u002d\u0034\u0033\u0034\u0030\u002d\u0061\u0031\u0035\u0039\u002d\u0036\u0064\u0038\u0065\u0035\u0032\u0063\u0066\u0030\u0034\u0035\u0064").get().build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JSONArray jsonArray = new JSONArray(jsonResponse);
// item1

                String item1Name = (String) jsonArray.getJSONObject(0).toMap().get("name");
                String item1Link = (String) jsonArray.getJSONObject(0).toMap().get("imageUrl");
                String item1Rarity = (String) jsonArray.getJSONObject(0).toMap().get("rarity");
                int item1vBucks = (int) jsonArray.getJSONObject(0).toMap().get("vBucks");
// item2

                String item2Name = (String) jsonArray.getJSONObject(1).toMap().get("name");
                String item2Link = (String) jsonArray.getJSONObject(1).toMap().get("imageUrl");
                String item2Rarity = (String) jsonArray.getJSONObject(1).toMap().get("rarity");
                int item2vBucks = (int) jsonArray.getJSONObject(1).toMap().get("vBucks");
// item3

                String item3Name = (String) jsonArray.getJSONObject(2).toMap().get("name");
                String item3Link = (String) jsonArray.getJSONObject(2).toMap().get("imageUrl");
                String item3Rarity = (String) jsonArray.getJSONObject(2).toMap().get("rarity");
                int item3vBucks = (int) jsonArray.getJSONObject(2).toMap().get("vBucks");
// item4

                String item4Name = (String) jsonArray.getJSONObject(3).toMap().get("name");
                String item4Link = (String) jsonArray.getJSONObject(3).toMap().get("imageUrl");
                String item4Rarity = (String) jsonArray.getJSONObject(3).toMap().get("rarity");
                int item4vBucks = (int) jsonArray.getJSONObject(3).toMap().get("vBucks");
// item5

                String item5Name = (String) jsonArray.getJSONObject(4).toMap().get("name");
                String item5Link = (String) jsonArray.getJSONObject(4).toMap().get("imageUrl");
                String item5Rarity = (String) jsonArray.getJSONObject(4).toMap().get("rarity");
                int item5vBucks = (int) jsonArray.getJSONObject(4).toMap().get("vBucks");
// item6

                String item6Name = (String) jsonArray.getJSONObject(5).toMap().get("name");
                String item6Link = (String) jsonArray.getJSONObject(5).toMap().get("imageUrl");
                String item6Rarity = (String) jsonArray.getJSONObject(5).toMap().get("rarity");
                int item6vBucks = (int) jsonArray.getJSONObject(5).toMap().get("vBucks");
// item7

                String item7Name = (String) jsonArray.getJSONObject(6).toMap().get("name");
                String item7Link = (String) jsonArray.getJSONObject(6).toMap().get("imageUrl");
                String item7Rarity = (String) jsonArray.getJSONObject(6).toMap().get("rarity");
                int item7vBucks = (int) jsonArray.getJSONObject(6).toMap().get("vBucks");
// item8

                String item8Name = (String) jsonArray.getJSONObject(7).toMap().get("name");
                String item8Link = (String) jsonArray.getJSONObject(7).toMap().get("imageUrl");
                String item8Rarity = (String) jsonArray.getJSONObject(7).toMap().get("rarity");
                int item8vBucks = (int) jsonArray.getJSONObject(7).toMap().get("vBucks");
// item9

                String item9Name = (String) jsonArray.getJSONObject(8).toMap().get("name");
                String item9Link = (String) jsonArray.getJSONObject(8).toMap().get("imageUrl");
                String item9Rarity = (String) jsonArray.getJSONObject(8).toMap().get("rarity");
                int item9vBucks = (int) jsonArray.getJSONObject(8).toMap().get("vBucks");
// item10

                String item10Name = (String) jsonArray.getJSONObject(9).toMap().get("name");
                String item10Link = (String) jsonArray.getJSONObject(9).toMap().get("imageUrl");
                String item10Rarity = (String) jsonArray.getJSONObject(9).toMap().get("rarity");
                int item10vBucks = (int) jsonArray.getJSONObject(9).toMap().get("vBucks");
// item11

                String item11Name = (String) jsonArray.getJSONObject(10).toMap().get("name");
                String item11Link = (String) jsonArray.getJSONObject(10).toMap().get("imageUrl");
                String item11Rarity = (String) jsonArray.getJSONObject(10).toMap().get("rarity");
                int item11vBucks = (int) jsonArray.getJSONObject(10).toMap().get("vBucks");
// item12

                String item12Name = (String) jsonArray.getJSONObject(11).toMap().get("name");
                String item12Link = (String) jsonArray.getJSONObject(11).toMap().get("imageUrl");
                String item12Rarity = (String) jsonArray.getJSONObject(11).toMap().get("rarity");
                int item12vBucks = (int) jsonArray.getJSONObject(11).toMap().get("vBucks");
// item13

                String item13Name = (String) jsonArray.getJSONObject(12).toMap().get("name");
                String item13Link = (String) jsonArray.getJSONObject(12).toMap().get("imageUrl");
                String item13Rarity = (String) jsonArray.getJSONObject(12).toMap().get("rarity");
                int item13vBucks = (int) jsonArray.getJSONObject(12).toMap().get("vBucks");
// item14

                String item14Name = (String) jsonArray.getJSONObject(13).toMap().get("name");
                String item14Link = (String) jsonArray.getJSONObject(13).toMap().get("imageUrl");
                String item14Rarity = (String) jsonArray.getJSONObject(13).toMap().get("rarity");
                int item14vBucks = (int) jsonArray.getJSONObject(13).toMap().get("vBucks");
// item15

                String item15Name = (String) jsonArray.getJSONObject(14).toMap().get("name");
                String item15Link = (String) jsonArray.getJSONObject(14).toMap().get("imageUrl");
                String item15Rarity = (String) jsonArray.getJSONObject(14).toMap().get("rarity");
                int item15vBucks = (int) jsonArray.getJSONObject(14).toMap().get("vBucks");
// item16

                String item16Name = (String) jsonArray.getJSONObject(15).toMap().get("name");
                String item16Link = (String) jsonArray.getJSONObject(15).toMap().get("imageUrl");
                String item16Rarity = (String) jsonArray.getJSONObject(15).toMap().get("rarity");
                int item16vBucks = (int) jsonArray.getJSONObject(15).toMap().get("vBucks");
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd");
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setColor(Wrapper.getAmbientColor())
                        .setTitle("Fortnite Daily Item-Shop - " + dateFormat.format(date))
                        .setDescription("Resets everyday at 8:00 PM EST")
                        .addField("", "**__[" + item1Name + "](" + item1Link + ")__**\n**Rarity:** `" + item1Rarity + "`\n**Cost:** `" + item1vBucks + "`", true)
                        .addField("", "**__[" + item2Name + "](" + item2Link + ")__**\n**Rarity:** `" + item2Rarity + "`\n**Cost:** `" + item2vBucks + "`", true)
                        .addField("", "**__[" + item3Name + "](" + item3Link + ")__**\n**Rarity:** `" + item3Rarity + "`\n**Cost:** `" + item3vBucks + "`", true)
                        .addField("", "**__[" + item4Name + "](" + item4Link + ")__**\n**Rarity:** `" + item4Rarity + "`\n**Cost:** `" + item4vBucks + "`", true)
                        .addField("", "**__[" + item5Name + "](" + item5Link + ")__**\n**Rarity:** `" + item5Rarity + "`\n**Cost:** `" + item5vBucks + "`", true)
                        .addField("", "**__[" + item6Name + "](" + item6Link + ")__**\n**Rarity:** `" + item6Rarity + "`\n**Cost:** `" + item6vBucks + "`", true)
                        .addField("", "**__[" + item7Name + "](" + item7Link + ")__**\n**Rarity:** `" + item7Rarity + "`\n**Cost:** `" + item7vBucks + "`", true)
                        .addField("", "**__[" + item8Name + "](" + item8Link + ")__**\n**Rarity:** `" + item8Rarity + "`\n**Cost:** `" + item8vBucks + "`", true)
                        .addField("", "**__[" + item9Name + "](" + item9Link + ")__**\n**Rarity:** `" + item9Rarity + "`\n**Cost:** `" + item9vBucks + "`", true)
                        .addField("", "**__[" + item10Name + "](" + item10Link + ")__**\n**Rarity:** `" + item10Rarity + "`\n**Cost:** `" + item10vBucks + "`", true)
                        .addField("", "**__[" + item11Name + "](" + item11Link + ")__**\n**Rarity:** `" + item11Rarity + "`\n**Cost:** `" + item11vBucks + "`", true)
                        .addField("", "**__[" + item12Name + "](" + item12Link + ")__**\n**Rarity:** `" + item12Rarity + "`\n**Cost:** `" + item12vBucks + "`", true)
                        .addField("", "**__[" + item13Name + "](" + item13Link + ")__**\n**Rarity:** `" + item13Rarity + "`\n**Cost:** `" + item13vBucks + "`", true)
                        .addField("", "**__[" + item14Name + "](" + item14Link + ")__**\n**Rarity:** `" + item14Rarity + "`\n**Cost:** `" + item14vBucks + "`", true)
                        .addField("", "**__[" + item15Name + "](" + item15Link + ")__**\n**Rarity:** `" + item15Rarity + "`\n**Cost:** `" + item15vBucks + "`", true)
                        .addField("", "**__[" + item16Name + "](" + item16Link + ")__**\n**Rarity:** `" + item16Rarity + "`\n**Cost:** `" + item16vBucks + "`", true)
                        .setFooter("Powered by Fortnie Tracker\u2122", "https://pbs.twimg.com/profile_images/1052980485104656384/6mctrPUF_400x400.jpg");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                event.getChannel().sendMessage("Response was unsuccessful. Something went wrong ig").queue();
                System.out.println(response.message());
            }
        } catch (Exception ex) {
            event.getChannel().sendMessage("Something went wrong with the endpoint").queue();
            ex.printStackTrace();
        }
    }


    @Override
    public String getHelp() {
        return "Returns the current Fortnite Item-Shop\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "itemshop";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"fnshop", "fortniteitemshop", "itemshopfn", "dailyshop"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
