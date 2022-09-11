package me.comu.exeter.commands.admin;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.util.Captcha;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class CaptchaCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getMessage().getAuthor().getId().equals(event.getGuild().getOwnerId()) || Objects.requireNonNull(event.getMember()).getIdLong() == Core.OWNERID) {
            if (args.isEmpty()) {
                event.getChannel().sendMessageEmbeds(Utility.errorEmbed(getHelp()).build()).queue();
                return;
            }
            if (args.get(0).equalsIgnoreCase("disable")) {
                event.getChannel().sendMessageEmbeds(Utility.embed("Captcha Verification has been successfully **disabled**.").build()).queue();
                Captcha.captchaEnabledServers.put(event.getGuild().getId(), null);
            } else {
                try {
                    Role role = event.getGuild().getRoleById(args.get(0));
                    Captcha.captchaEnabledServers.put(event.getGuild().getId(), Objects.requireNonNull(role).getId());
                    event.getChannel().sendMessageEmbeds(Utility.embed("Successfully enabled Captcha Verification with `" + role.getName() + "`.").build()).queue();
                } catch (Exception ex) {
                    event.getChannel().sendMessageEmbeds(Utility.errorEmbed("I couldn't find the specified role.").build()).queue();
                }
            }

        } else {
            event.getChannel().sendMessageEmbeds(Utility.embed("Captcha Verification can only be managed by the owner of the server.").build()).queue();
        }

    }

    @Override
    public String getHelp() {
        return "Enables captcha verification in your server\n`" + Core.PREFIX + getInvoke() + " [role-id]/[disable]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`\n";
    }

    @Override
    public String getInvoke() {
        return "captcha";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"recaptcha"};
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
