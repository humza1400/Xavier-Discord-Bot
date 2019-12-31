package me.comu.exeter.wrapper;

import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Wrapper {

//    public static List<Shard> shards = new ArrayList<>();

    public static void sendPrivateMessage(User user, String content) {
        if (!user.getId().equals("631654319342616596")) {
            user.openPrivateChannel().queue((channel) ->
            {
                try {
                    channel.sendMessage(content).queue();
                } catch (Exception ex)
                {
                    Logger.getLogger().print("Couldn't message " + user.getName() + "#" + user.getDiscriminator());
                }
            });
        }
    }

    public void sendPrivateMessageWithDelay(User user, String content, long delay, TimeUnit timeUnit) {
        user.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(content).queueAfter(delay, timeUnit);
        });
    }

    public static void sendWhitelistedAntiRaidInfoMessage(Guild guild, List<String> list, String message)
    {
        for (String user : list)
        {
            sendPrivateMessage(guild.getMemberById(user).getUser(), message);
        }
    }

    public static boolean botCheck(Message message)
    {
        if (message.getAuthor().isBot())
            return false;
        return true;
    }

    public static long timeToMS(int hours, int minutes, int seconds) {
        if(seconds > 59 || seconds < 0) {
            return -1;
        }
        if(minutes > 59 || minutes < 0) {
            return -1;
        }

        long s = (seconds + (60 * (minutes + (hours * 60))));
        return TimeUnit.SECONDS.toMillis(s);
    }
    public static List<Guild> getGuilds() {
        List<Guild> guilds = new ArrayList<>();
     //   for(Shard shard : shards) {
       //     guilds.addAll(shard.getJda().getGuilds());
        //}
        return guilds;
    }

    public static Color getRandomColor() {
        final Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }

    public static String getIpaddress() {
        String ipAddress = "null";
        try {
            final URL whatismyip = new URL("http://checkip.amazonaws.com");
            final BufferedReader input = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ipAddress = input.readLine();
            input.close();
        } catch (Exception ex) {
        }
        return ipAddress;
    }

    public static String getHostInformation() {
        InetAddress ip;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName() + "/" + ip.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostname;
    }

    public static void sendEmail(String subject, String message)
    {
/*
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ilyswag48@gmail.com","DiscordPassword123");
            }
        });
        try {
            javax.mail.Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress("ilyswag48@gmail.com"));
            email.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse("ilyswag48@gmail.com"));
            email.setSubject(subject);
            email.setText(message);
            Transport.send(email);
        } catch (MessagingException mess)
        {
            mess.printStackTrace();
        }
*/


    }

}
