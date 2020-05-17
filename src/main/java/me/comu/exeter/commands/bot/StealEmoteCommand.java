package me.comu.exeter.commands.bot;

import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StealEmoteCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_EMOTES) && event.getMember().getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessage("You don't have permission to modify emotes").queue();
            return;
        }

        if (!Objects.requireNonNull(event.getMember()).hasPermission(Permission.MANAGE_EMOTES)) {
            event.getChannel().sendMessage("I don't have permissions to modify emotes").queue();
            return;
        }

        if (args.size() != 2 || event.getMessage().getEmotes().size() == 0) {
            event.getChannel().sendMessage("Please insert a valid emote and an emote-name").queue();
            return;
        }

        Emote emote = event.getMessage().getEmotes().get(0);
        String name = args.get(1);
        if (emote.isAnimated())
        {
            event.getChannel().sendMessage("\u2699 Creating " + name + " emoji...").queue(
            message -> {
                Wrapper.saveGif(emote.getImageUrl(), "cache",name);
                message.editMessage("\u2699 Saving emoji...").queue(message1 -> {
                    try {
                        event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".gif"))).queue(success -> {
                            message1.editMessage("\u2705 Successfully created **" + name + "**").queue();
                            Config.clearCacheDirectory();
                        });
                    } catch (Exception ex)
                    {
                        message.editMessage("\u274C Something went wrong try again later.").queue();
                    }
                });
            }
            );

        } else {
            event.getChannel().sendMessage("\u2699 Creating " + name + " emoji...").queue(
                    message -> {
                        Wrapper.saveImage(emote.getImageUrl(), "cache",name);
                        message.editMessage("\u2699 Saving emoji...").queue(message1 -> {
                            try {
                                event.getGuild().createEmote(name, Icon.from(new File("cache/" + name + ".png"))).queue(success ->
                                        message1.editMessage("\u2705 Successfully created **" + name + "**").queue());
                                        Config.clearCacheDirectory();
                            } catch (Exception ex)
                            {
                                message.editMessage("\u274C Something went wrong try again later.").queue();
                            }
                        });
                    }
            );
        }





    }

    @Override
    public String getHelp() {
        return "Adds the given emoji to the server\n `" + Core.PREFIX + getInvoke() + " [emote] <emote-name>`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "stealemote";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"addemote","addemoji","stealemoji","emoteadd","emojiadd","emotestea","emojisteal"};
    }

    @Override
    public Category getCategory() {
        return Category.BOT;
    }
}
