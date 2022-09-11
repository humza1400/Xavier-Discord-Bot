package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AntiRaidCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            event.getChannel().sendMessageEmbeds(Utility.embed(getHelp(event.getGuild().getId())).build()).queue();
            return;
        }
        if (!(event.getAuthor().getIdLong() == Core.OWNERID) && !event.getAuthor().getId().equalsIgnoreCase(Objects.requireNonNull(event.getGuild().getOwner()).getId()) && !Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), event.getAuthor().getId(), event.getGuild().getId())) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to toggle anti-raid.").build()).queue();
            return;
        }

        if (Utility.isWhitelisted(WhitelistCommand.getWhitelistedIDs(), event.getAuthor().getId(), event.getGuild().getId())) {
            int permissionLevel = Integer.parseInt(WhitelistCommand.getWhitelistedIDs().get(WhitelistKey.of(event.getGuild().getId(), event.getAuthor().getId())));
            if (permissionLevel != 0) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You're whitelist-level isn't high enough to whitelist others. **(" + permissionLevel + ")**").build()).queue();
                return;
            }
        }

        if (args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("on")) {
            if (!Utility.isAntiRaidEnabled(event.getGuild().getId())) {
                Utility.toggleAntiRaid(event.getGuild().getId(), true);
                event.getChannel().sendMessageEmbeds(Utility.embed("Anti-Raid is now **active**.").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Anti-Raid is already enabled.").build()).queue();
        } else if (args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("off")) {
            if (Utility.isAntiRaidEnabled(event.getGuild().getId())) {
                Utility.toggleAntiRaid(event.getGuild().getId(), false);
                event.getChannel().sendMessageEmbeds(Utility.embed("Anti-Raid is **no longer active**.").build()).queue();
            } else
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed("Anti-Raid is already disabled.").build()).queue();
        }
        Core.getInstance().saveConfig(Core.getInstance().getAntiRaidGuildJSONHandler());
    }


    private String getHelp(String guild) {
        return "Tries to prevent a malicious attack against the server\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", Utility.isAntiRaidEnabled(guild) ? "enabled" : "disabled");
    }

    @Override
    public String getHelp() {
        return "Tries to prevent a malicious attack against the server\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "antiraid";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ar", "anti-raid", "antir", "noraid", "antinuke", "anti-nuke", "nonuke"};
    }

    @Override
    public Category getCategory() {
        return Category.ADMIN;
    }

    @Override
    public boolean isPremium() {
        return true;
    }

}