package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PfpGeneratorCommand implements ICommand {

    private final String[] categories = new String[]{"aesthetic", "animals", "anime", "banners", "anime", "banners", "besties", "body", "boy_anime", "boy_cartoon", "boy_gif", "boy_icon", "cars", "cartoon_lovers", "celebs", "rapper", "color_gifs", "couples", "decor", "default", "dividers", "egirl", "faceless", "food", "freaks", "girl_anime", "girl_cartoon", "girl_gif", "girl_icon", "guns", "hairstyles", "halloween", "jewelery", "kpop", "lover_anime", "lover_gif", "lover_icon", "makeup", "matching_gif", "matching_icon", "money", "mood", "mouth", "nails", "quotes", "random", "shoes", "smoking", "tattoos", "twerking", "wallpapers", "xmas", "youngboy", "y2k"};

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ATTACH_FILES)) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed(Utility.ERROR_EMOTE + " I don't have permission to send files.").build()).queue();
            return;
        }
        Random random = new Random();
        String ext = "random";
        if (!args.isEmpty()) {
            boolean found = false;
            for (String category : categories) {
                if (args.get(0).equalsIgnoreCase("matching")) {
                    found = true;
                    ext = random.nextInt() % 2 == 0 ? "matching_icon" : "matching_gif";
                    break;
                } else if (args.get(0).equalsIgnoreCase("boy")) {
                    found = true;
                    ext = random.nextInt() % 2 == 0 ? "boy_icon" : "boy_gif";
                    break;
                } else if (args.get(0).equalsIgnoreCase("girl")) {
                    found = true;
                    ext = random.nextInt() % 2 == 0 ? "girl_icon" : "girl_gif";
                    break;
                } else if (args.get(0).equalsIgnoreCase("cartoon")) {
                    found = true;
                    ext = "cartoon_lovers";
                    break;
                }
                if (args.get(0).equalsIgnoreCase(category)) {
                    found = true;
                    ext = args.get(0).toLowerCase(Locale.ROOT);
                }
            }
            if (!found) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Sorry, we don't have a category for `" + Utility.removeMentionsAndMarkdown(args.get(0)) + "`.").build()).queue();
                return;
            }
        }
        if (!event.getChannel().isNSFW() && (ext.equalsIgnoreCase("body") || ext.equalsIgnoreCase("freaks") || ext.equalsIgnoreCase("twerking"))) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("<a:no:961695015195775086> **Naughty Naughty** For safety reasons you can only access " + ext + " pfps, in a NSFW-Marked channel. Sorry, you freak.").build()).queue();
            return;
        }
        File directory = new File("pfps/" + ext);
        File[] categories = directory.listFiles();
        if (categories == null || categories.length == 0) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("No categories have been loaded into my cache.").build()).queue();
            return;
        }

        File pfp = new File(String.valueOf(categories[random.nextInt(categories.length)]));
        if (ext.contains("matching")) {
            try {
                int index1 = Integer.parseInt(pfp.getName().replaceAll("[^0-9]", ""));
                int index2 = index1 % 2 == 0 ? --index1 : ++index1;
//                String matching1 = "attachment://" + pfp.getAbsolutePath();
                File pfp2 = new File("pfps/" + ext + "/comp_bot_" + ext + " (" + index2 + ")." + pfp.getName().substring(pfp.getName().lastIndexOf(".") + 1));
//                String matching2 = "attachment://comp_bot_matching_" + ext + " (" + index2 + ")";
                event.getChannel().sendMessage("**Here, enjoy two matching pfps**").addFile(pfp).addFile(pfp2).queue();
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Sorry, something went wrong with indexing our matching pfps :(").build()).queue();
            }
        } else {
            String an = ext.charAt(0) == 'a' ? "an" : "a";
            event.getChannel().sendMessage("**Here, enjoy " + an + " " + ext + " pfp**").addFile(pfp).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Spits out a random profile picture, or a category-specific one.\n`"
                + Core.PREFIX + getInvoke() + " <category>`\n"
                + "**Available Categories:** \n"
                + Arrays.deepToString(categories)
                + "\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "pfpgenerator";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"pfpgen", "genpfp", "profilepic", "profilepicgen", "genprofilepic", "pfp"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }

    @Override
    public boolean isPremium() {
        return true;
    }


}
