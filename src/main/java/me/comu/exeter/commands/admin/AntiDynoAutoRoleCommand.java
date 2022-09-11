package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AntiDynoAutoRoleCommand implements ICommand {

    public static boolean active = true;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Objects.requireNonNull(event.getMember()).getIdLong() != Core.OWNERID) {
            event.getChannel().sendMessageEmbeds(Utility.errorEmbed("You don't have permission to alter the status of Anti-Dyno.").build()).queue();
            return;
        }
        if (args.isEmpty()) {
            System.out.println("Please specify a value");
            return;
        }
        if (args.get(0).equalsIgnoreCase("on")  || args.get(0).equalsIgnoreCase("true") || args.get(0).equalsIgnoreCase("yes")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Anti-Dyno Auto Role has been **enabled**.").build()).queue();
            active = true;
            return;
        }

        if (args.get(0).equalsIgnoreCase("off")  || args.get(0).equalsIgnoreCase("false") || args.get(0).equalsIgnoreCase("no")) {
            event.getChannel().sendMessageEmbeds(Utility.embed("Anti-Dyno Auto Role has been **disabled**.").build()).queue();
            active = false;
        }
    }

    @Override
    public String getHelp() {
        return "Prevents Dyno or Carl Bot from auto-roling a malicious role\n`" + Core.PREFIX + getInvoke() + " [on/off]`\nAliases `" + Arrays.deepToString(getAlias()) + "`\n" + String.format("Currently `%s`.", active ? "enabled" : "disabled");
    }

    @Override
    public String getInvoke() {
        return "antidyno";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"antiautorole","antidynoautorole","anticarl","antidynobot","anticarlbot","anticarlautorole"};
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