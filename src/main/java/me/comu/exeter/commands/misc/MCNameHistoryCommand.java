package me.comu.exeter.commands.misc;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MCNameHistoryCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Please insert an IGN").queue();
            return;
        }
        fetchUUID(args.get(0), (uuid) -> fetchNameHistory(uuid, names -> {
            if (names == null || names.isEmpty()) {
                event.getChannel().sendMessage("UUID Returned Null").queue();
                return;
            }
            final String igns = String.join(", ", names);
            event.getChannel().sendMessage("**" + args.get(0) + "'s** name history (" + uuid + "):\n" + Utility.removeMentions(igns)).queue();
        }));
    }

    private String fetchUUID(String username, Consumer<String> response) {
        StringBuilder stringBuilder = new StringBuilder();
        WebUtils.ins.getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username).async((jsonNodes) -> {
            response.accept(jsonNodes.get("id").asText());
            stringBuilder.append(jsonNodes.get("id").asText());
        }, (error) -> response.accept(null));
        return stringBuilder.toString();
    }

    private void fetchNameHistory(String uuid, Consumer<List<String>> response) {
        WebUtils.ins.getJSONArray("https://api.mojang.com/user/profiles/" + uuid + "/names").async((jsonNodes) -> {
            List<String> names = new ArrayList<>();
            jsonNodes.forEach((item) -> names.add(item.get("name").asText()));
            response.accept(names);
        }, (error) -> response.accept(null));
    }


    @Override
    public String getHelp() {
        return "Shows all the IGN's the player has had\n`" + Core.PREFIX + getInvoke() + " [IGN]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "namemc";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"mcign", "mcignhistory", "ignhistory", "ign"};
    }

    @Override
    public Category getCategory() {
        return Category.MISC;
    }
}
