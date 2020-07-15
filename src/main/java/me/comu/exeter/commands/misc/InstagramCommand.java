package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InstagramCommand implements ICommand {
    @SuppressWarnings("unchecked")
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please specify a valid user").queue();
            return;
        }
        String endpoint = "https://instagram.com/{name}/?__a=1";


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(endpoint.replace("{name}", args.get(0))).get().build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
                Logger.getLogger().print(jsonResponse + "\n");
                HashMap<String, ?> userObject = (HashMap<String, ?>) ((HashMap<String, ?>) new JSONObject(jsonResponse).toMap().get("graphql")).get("user");
                String username = "@"+args.get(0).toLowerCase();
                int followers =  ((HashMap<String, Integer>) userObject.get("edge_followed_by")).get("count");
                int following =  ((HashMap<String, Integer>) userObject.get("edge_follow")).get("count");
                int posts = ((HashMap<String, Integer>) userObject.get("edge_owner_to_timeline_media")).get("count");
                String pfp = (String) userObject.get("profile_pic_url_hd");
                String bioName = (String) userObject.get("full_name");
                String bioDesc = (String) userObject.get("biography");
                String firstPic = null;
                Boolean isPrivate = (Boolean) userObject.get("is_private");
                boolean isVerified = (Boolean) userObject.get("is_verified");
                EmbedBuilder embedBuilder = new EmbedBuilder();
                NumberFormat myFormat = NumberFormat.getInstance();
                embedBuilder.setTitle(bioName);
                embedBuilder.setThumbnail(pfp);
                embedBuilder.setAuthor(username,"https://instagram.com/" + args.get(0), isVerified ? "https://cdn.discordapp.com/emojis/732783207468236870.gif?v=1" : null);
                embedBuilder.setDescription(bioDesc);
                embedBuilder.setColor(Utility.getAmbientColor());
                embedBuilder.addField("Posts", myFormat.format(posts), true);
                embedBuilder.addField("Followers", myFormat.format(followers), true);
                embedBuilder.addField("Following", myFormat.format(following), true);
                if (isPrivate)
                {
                    embedBuilder.setFooter("Private Account", "https://cdn.discordapp.com/emojis/732806672447438850.png?v=1");
                }
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } else {
                event.getChannel().sendMessage("I couldn't find the user `" + args.get(0).replaceAll("`", "\\`") + "`").queue();
            }
        } catch (Exception ex) {
            event.getChannel().sendMessage("Caught an error while trying to connect to the endpoint").queue();
            ex.printStackTrace();
        }


    }

    @Override
    public String getHelp() {
        return "Returns information on the specified Instagram user\n`" + Core.PREFIX + getInvoke() + " [username]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "instagram";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"insta", "ig"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
