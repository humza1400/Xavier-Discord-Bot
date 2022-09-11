package me.comu.exeter.events;

import me.comu.exeter.commands.moderation.BindLogChannelCommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.objects.WhitelistKey;
import me.comu.exeter.objects.ObjectKey;
import me.comu.exeter.util.Captcha;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class LogMessageReceivedEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && BindLogChannelCommand.bound && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            DateFormat df = new SimpleDateFormat("MM/dd/yy h:mm:ss a");
            Date dateobj = new Date();
            String guildName = event.getGuild().getName();
            String channelName = event.getChannel().getName();
            User author = event.getAuthor();
            Message message = event.getMessage();
            try {
                Objects.requireNonNull(event.getJDA().getTextChannelById(BindLogChannelCommand.logChannelID)).sendMessage(Utility.removeMentions(String.format("`%s (%s)[%s]<%#s>:` %s", df.format(dateobj.getTime()), guildName, channelName, author, message))).queue();
            } catch (NullPointerException ex) {
                event.getChannel().sendMessage("SOMETHING WENT WRONG").queue();
            }
        }
        if (event.isFromType(ChannelType.PRIVATE)) {
            User user = event.getAuthor();
            String msg = event.getMessage().getContentRaw();
            for (WhitelistKey key : Captcha.captchaUsers.keySet())
            {
                if (key.getUserID().equals(user.getId()))
                {
                    ObjectKey oKey = Captcha.captchaUsers.get(key);
                    if (oKey.getKey().equals(msg)) {
                        Captcha.captchaUsers.remove(key);
                        try {
                            Role role = event.getJDA().getRoleById(Captcha.captchaEnabledServers.get(key.getGuildID()));
                            Guild guild = event.getJDA().getGuildById(key.getGuildID());
                            Objects.requireNonNull(guild).addRoleToMember(user.getId(), Objects.requireNonNull(role)).queue();
                            event.getChannel().sendMessage("**Thank you** for completing the captcha, you have successfully been verified in " + Objects.requireNonNull(guild).getName()).queue(null, failure -> Logger.getLogger().print("User verified but couldn't send confirmation | " + user.getAsTag()));
                        } catch (Exception ex)
                        {
                            event.getChannel().sendMessage("You completed the captcha but something went wrong and we were unable to verify you. Please contact an administrator.").queue(null, failure -> Logger.getLogger().print("Something went wrong!!!!! | " + user.getAsTag()));
                            ex.printStackTrace();
                        }
                    } else {
                        if ((int) oKey.getValue() <= 1) {
                            Captcha.captchaUsers.remove(key);
                            event.getChannel().sendMessage("You have failed all **3 attempts** at verifying for the server, you must now be manually verified.").queue(null, failure -> Logger.getLogger().print("User failed verification 3 times but couldn't send confirmation | " + user.getAsTag()));
                        } else {
                            Captcha.captchaUsers.put(key, ObjectKey.of(oKey.getKey(), (int)oKey.getValue()-1));
                            event.getChannel().sendMessage("Incorrect Captcha! You have **" + ((int)oKey.getValue()-1) + " attempts** remaining.").queue(null, failure -> Logger.getLogger().print("User failed verification 3 times but couldn't send confirmation | " + user.getAsTag()));
                        }
                    }
                }
            }

        }
    }
}

