package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class EditSnipeCommand implements ICommand {

    public static HashMap<String, String> messages = new HashMap<>();
    public static HashMap<String, String> authors = new HashMap<>();
    public static String preContentDeleted;
    public static String postContentDeleted;
    public static String author;
    public static Instant timeDeleted;
    public static boolean snipeable = false;
    public static boolean containedAttachments;


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!snipeable) {
            event.getChannel().sendMessage("There's nothing to snipe.").queue();
            return;
        }

        if (containedAttachments) {
            event.getChannel().sendMessage(EmbedUtils.embedImage(Wrapper.extractUrls(preContentDeleted).get(0))
                    .setColor(Wrapper.getAmbientColor())
                    .addField("Before", preContentDeleted, false)
                    .addField("After", postContentDeleted, false)
                    .setTimestamp(timeDeleted)
                    .setAuthor(Objects.requireNonNull(event.getGuild().getMemberById(author)).getUser().getAsTag(), null, Objects.requireNonNull(event.getGuild().getMemberById(author)).getUser().getEffectiveAvatarUrl()).build()).queue();
        } else {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Wrapper.getAmbientColor())
                    .addField("Before", preContentDeleted, false)
                    .addField("After", postContentDeleted, false)
                    .setTimestamp(timeDeleted)
                    .setAuthor(Objects.requireNonNull(event.getGuild().getMemberById(author)).getUser().getAsTag(), null, Objects.requireNonNull(event.getGuild().getMemberById(author)).getUser().getEffectiveAvatarUrl());
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }


    }

    @Override
    public String getHelp() {
        return "Snipes the last edited message\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "editsnipe";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"esnipe", "esnipemessage", "esnipemsg"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
