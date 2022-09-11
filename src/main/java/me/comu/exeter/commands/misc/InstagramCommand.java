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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InstagramCommand implements ICommand {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm a").withLocale(Locale.US).withZone(ZoneId.of("America/New_York"));

    @SuppressWarnings("unchecked")
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify a valid user").build()).queue();
            return;
        }
        String endpoint = "https://instagram.com/{name}/?__a=1";

/*        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("sessionid", " ");
        cookie.setDomain(".instagram.com");
        cookie.setPath("/");*/
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36").url(endpoint.replace("{name}", args.get(0))).get().build();

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
                String caption = null;
                int comments = -1;
                int likes = -1;
                int epoch = -1;
                NumberFormat myFormat = NumberFormat.getInstance();
                ArrayList<?> objects = ((HashMap<String, ArrayList<?>>) userObject.get("edge_owner_to_timeline_media")).get("edges");
                if (!objects.isEmpty())
                {
                    HashMap<String, HashMap<?, ?>> hashMap = ((HashMap<String, HashMap<?, ?>>) objects.get(0));
                    HashMap<?, ?> nodeMap = hashMap.get("node");
                    firstPic = (String) nodeMap.get("display_url");
                    likes = ((HashMap<String, Integer>) nodeMap.get("edge_liked_by")).get("count");
                    comments = ((HashMap<String, Integer>) nodeMap.get("edge_media_to_comment")).get("count");
                    epoch = (Integer) nodeMap.get("taken_at_timestamp");
                    ArrayList<?> captionList = ((HashMap<String, ArrayList<?>>) nodeMap.get("edge_media_to_caption")).get("edges");
                    if (!captionList.isEmpty())
                    {
                        HashMap<String, HashMap<?, ?>> hashMap1 = ((HashMap<String, HashMap<?, ?>>) captionList.get(0));
                        HashMap<String, String> nodeMap1 = (HashMap<String, String>) hashMap1.get("node");
                        caption = nodeMap1.get("text");
                    }
                }
                String timestamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.of("GMT")).format(dtf);
                Boolean isPrivate = (Boolean) userObject.get("is_private");
                boolean isVerified = (Boolean) userObject.get("is_verified");
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(bioName.equals("") ? null : bioName);
                embedBuilder.setThumbnail(pfp);
                embedBuilder.setAuthor(username,"https://instagram.com/" + args.get(0), isVerified ? "https://cdn.discordapp.com/emojis/732783207468236870.gif?v=1" : null);
                embedBuilder.setDescription(bioDesc);
                embedBuilder.setColor(Core.getInstance().getColorTheme());
                embedBuilder.addField("Posts", myFormat.format(posts), true);
                embedBuilder.addField("Followers", myFormat.format(followers), true);
                embedBuilder.addField("Following", myFormat.format(following), true);
                if (isPrivate)
                {
                    embedBuilder.setFooter("Private Account", "https://cdn.discordapp.com/emojis/732806672447438850.png?v=1");
                } else if (firstPic != null){
                    embedBuilder.setImage(firstPic);
                    if (caption != null)
                    {
                        embedBuilder.setFooter(caption + "\n\n" + myFormat.format(likes) + " likes | " + myFormat.format(comments) + " comments\n" + "Posted on " + timestamp);
                    } else {
                        embedBuilder.setFooter(myFormat.format(likes) + " likes | " + myFormat.format(comments) + " comments\n" + "Posted on " + timestamp);
                    }
                }
                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            } else {
                event.getChannel().sendMessageEmbeds(Utility.embed("I couldn't find the user `" + args.get(0).replaceAll("`", "\\`") + "`").build()).queue();
            }
        } catch (Exception ex) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("An error occurred, Instagram is blocking this proxy.").build()).queue();
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

    @Override
    public boolean isPremium() {
        return false;
    }
}
