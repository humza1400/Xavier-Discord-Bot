package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WizzCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String base = String.format("`Wizzing %s, will take %s seconds to complete` \n", event.getGuild().getName(), new Random().nextInt(60));
        event.getChannel().sendMessage(base).queue((response) -> {
            response.editMessage(base + "`Deleting Roles...\n`").queue((response1) -> {
                response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\n`").queueAfter(1, TimeUnit.SECONDS, (response2) -> {
                    response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\nDeleting Voice Channels...\n`").queueAfter(1, TimeUnit.SECONDS, (response3) -> {
                        response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\nDeleting Voice Channels...\nDeleting Categories...\n`").queueAfter(1, TimeUnit.SECONDS, (response4) -> {
                            response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\nDeleting Voice Channels...\nDeleting Categories...\nDeleting Webhooks...\n`").queueAfter(1, TimeUnit.SECONDS, (response5) -> {
                                response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\nDeleting Voice Channels...\nDeleting Categories...\nDeleting Webhooks...\nDeleting Emojis...\n`").queueAfter(1, TimeUnit.SECONDS, (response6) -> {
                                    response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\nDeleting Voice Channels...\nDeleting Categories...\nDeleting Webhooks...\nDeleting Emojis...\nInitiating Banwave...\n`").queueAfter(1, TimeUnit.SECONDS, (response7) -> {
                                        response.editMessage(base + "`Deleting Roles...\nDeleting Text Channels...\nDeleting Voice Channels...\nDeleting Categories...\nDeleting Webhooks...\nDeleting Emojis...\nInitiating Banwave...\nInitializing Mass-DM Advertise...`").queueAfter(1, TimeUnit.SECONDS);
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    @Override
    public String getHelp() {
        return "Wizzes the server!\n`" + Core.PREFIX + getInvoke() + "[id]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "wizz";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"nuke"};
    }
}
