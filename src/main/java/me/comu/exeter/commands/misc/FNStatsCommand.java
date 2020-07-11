package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class FNStatsCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() < 2) {
            event.getChannel().sendMessage("Please provide a valid platform and username").queue();
            return;
        }
        if (formatConsole(args.get(0)) == null) {
            event.getChannel().sendMessage("Please provide a valid console").queue();
            return;
        }
        String console = formatConsole(args.get(0));
        if (console == null) {
            event.getChannel().sendMessage("Something went wrong with parsing your console").queue();
            return;
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        args.stream().skip(1).forEach(stringJoiner::add);
        String username = stringJoiner.toString();
        String url = "https://api.fortnitetracker.com/v1/profile/{platform}/{epic-nickname}".replace("{platform}", console).replace("{epic-nickname}", username);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("\u0054\u0052\u004e\u002d\u0041\u0070\u0069\u002d\u004b\u0065\u0079", "\u0033\u0064\u0066\u0037\u0037\u0066\u0039\u0063\u002d\u0035\u0036\u0062\u0035\u002d\u0034\u0033\u0034\u0030\u002d\u0061\u0031\u0035\u0039\u002d\u0036\u0064\u0038\u0065\u0035\u0032\u0063\u0066\u0030\u0034\u0035\u0064").get().build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (jsonResponse.contains("error")) {
                    event.getChannel().sendMessage("Couldn't find player: `" + Utility.removeMentions(username)+ "`.").queue();
                    return;
                }
                JSONArray stats = jsonObject.getJSONArray("lifeTimeStats");
                String epicName = jsonObject.toMap().get("epicUserHandle").toString();
                String wins = stats.getJSONObject(0).toMap().get("value").toString();
                String matchesPlayed = stats.getJSONObject(7).toMap().get("value").toString();
                String winPercent = stats.getJSONObject(9).toMap().get("value").toString();
                String kills = stats.getJSONObject(10).toMap().get("value").toString();
                String kd = stats.getJSONObject(11).toMap().get("value").toString();
                String top10 = stats.getJSONObject(3).toMap().get("value").toString();
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setColor(Utility.getAmbientColor())
                        .setTitle(epicName + " - " + Objects.requireNonNull(formattedConsoleName(console)))
                        .addField("Wins", wins + " (" + winPercent + ")", true)
                        .addField("Matches", matchesPlayed, true)
                        .addField("Kills", kills, true)
                        .addField("KDR", kd, true)
                        .addField("KDR", kd, true)
                        .addField("Top 10s", top10, true)
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

    private String formatConsole(String console) {
        if (console.equalsIgnoreCase("xb1") || console.equalsIgnoreCase("xbox") || console.equalsIgnoreCase("xboxone") || console.equalsIgnoreCase("xbox1")) {
            return "xb1";
        }
        if (console.equalsIgnoreCase("pc") || console.equalsIgnoreCase("computer") || console.equalsIgnoreCase("laptop")) {
            return "pc";
        }
        if (console.equalsIgnoreCase("psn") || console.equalsIgnoreCase("ps4") || console.equalsIgnoreCase("playstation") || console.equalsIgnoreCase("playstation4")) {
            return "psn";
        }
        return null;
    }

    private String formattedConsoleName(String console) {
        if (console.equalsIgnoreCase("xb1") || console.equalsIgnoreCase("xbox") || console.equalsIgnoreCase("xboxone") || console.equalsIgnoreCase("xbox1")) {
            return "Xbox-One";
        }
        if (console.equalsIgnoreCase("pc") || console.equalsIgnoreCase("computer") || console.equalsIgnoreCase("laptop")) {
            return "PC";
        }
        if (console.equalsIgnoreCase("psn") || console.equalsIgnoreCase("ps4") || console.equalsIgnoreCase("playstation") || console.equalsIgnoreCase("playstation4")) {
            return "Playstation 4";
        }
        return null;
    }


    @Override
    public String getHelp() {
        return "Returns the specified user's Fortnite stats\n`" + Core.PREFIX + getInvoke() + " [console] <epic-name>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "fnstats";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"fortnitestats", "fnkdr", "fnkd"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
