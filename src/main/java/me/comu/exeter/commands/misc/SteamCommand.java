package me.comu.exeter.commands.misc;

import com.google.gson.JsonObject;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class SteamCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Please specify who's profile you want to look for").build()).queue();
            return;
        }
        try {
            JsonObject jsonObject = Utility.getJsonFromURL("https://api.alexflipnote.dev/steam/user/" + args.get(0)).getAsJsonObject();
            String steamid = jsonObject.getAsJsonObject("id").get("steamid64").toString();
            String avatarUrl = jsonObject.getAsJsonObject("avatars").get("avatarfull").toString();
            String username = jsonObject.getAsJsonObject("profile").get("username").toString();
            String realName = (jsonObject.getAsJsonObject("profile").has("realname") ? jsonObject.getAsJsonObject("profile").get("realname").toString() : null);
            String bio = (jsonObject.getAsJsonObject("profile").has("summary") ? jsonObject.getAsJsonObject("profile").get("summary").toString() : null);
            String profileUrl = jsonObject.getAsJsonObject("profile").get("url").toString();
            boolean vacBanned = Boolean.parseBoolean(jsonObject.getAsJsonObject("profile").get("url").toString());
            String status = jsonObject.getAsJsonObject("profile").get("state").toString();
            String privacy = jsonObject.getAsJsonObject("profile").get("privacy").toString();
            String timeCreated = (jsonObject.getAsJsonObject("profile").has("timecreated") ? jsonObject.getAsJsonObject("profile").get("timecreated").toString() : null);
            String location = (jsonObject.getAsJsonObject("profile").has("location") ? jsonObject.getAsJsonObject("profile").get("location").toString() : null);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor(Utility.removeLastChar(username.replaceFirst("\"", "")), profileUrl.replaceAll("\"", ""));
            embedBuilder.setThumbnail(avatarUrl.replaceAll("\"", ""));
            embedBuilder.setColor(Core.getInstance().getColorTheme());
            if (bio != null)
                embedBuilder.setDescription(Utility.removeLastChar(bio.substring(1)));
            if (realName != null)
                embedBuilder.setTitle(Utility.removeLastChar(realName.replaceFirst("\"", "")));
            embedBuilder.addField("Steam-ID", steamid.replaceAll("\"", ""), false);
            embedBuilder.addField("Status", status.replaceAll("\"", ""), false);
            embedBuilder.addField("Vac-Banned", vacBanned ? "Retard got banned lmao, probs used aimware" : "Not Banned", false);
            embedBuilder.addField("Privacy", privacy.replaceAll("\"", ""), false);
            if (timeCreated != null)
                embedBuilder.addField("Time Created", Utility.removeLastChar(timeCreated.replaceFirst("\"", "")), false);
            if (location != null && !location.equalsIgnoreCase("null")) {
                embedBuilder.addField("Location", Utility.removeLastChar(location.replaceFirst("\"", "")), false);
            }
            embedBuilder.setFooter("Powered by Steam\u2122", "https://cdn.discordapp.com/emojis/733176829254434816.png?v=1");
            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        } catch (IllegalArgumentException ex) {
            event.getChannel().sendMessageEmbeds(Utility.embed("I couldn't find any steam profile linked to `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "`").build()).queue();
        } catch (Exception ex) {
            ex.printStackTrace();
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Something went wrong when gathering steam information, try again later").build()).queue();
        }
    }


    @Override
    public String getHelp() {
        return "Returns the user's steam profile information\n`" + Core.PREFIX + getInvoke() + " [name/id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "steam";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"steamid", "steamlookup"};
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
