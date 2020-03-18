package me.comu.exeter.wrapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.comu.exeter.core.Core;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.comu.exeter.core.Core.jda;

public class Wrapper {

    //    public static List<Shard> shards = new ArrayList<>();
    private static JsonParser parser = new JsonParser();
    public static Map<String, String> marriedUsers = new HashMap<>();

    public static void sendPrivateMessage(JDA jda, String userId, String content)
    {
        RestAction<User> action = jda.retrieveUserById(userId);
        action.queue((user) -> user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue(null, (error) -> Logger.getLogger().print("Couldn't message " + Core.jda.getUserById(userId).getAsTag()))));
    }

    public void sendPrivateMessageWithDelay(JDA jda, String userId, String content, long delay, TimeUnit timeUnit) {
        RestAction<User> action = jda.retrieveUserById(userId);
        action.queue((user) -> user.openPrivateChannel().queueAfter(delay, timeUnit, (channel) -> channel.sendMessage(content).queue(null, (error) -> Logger.getLogger().print("Couldn't message " + Core.jda.getUserById(userId).getAsTag()))));
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public static boolean botCheck(Message message) {
        if (message.getAuthor().isBot())
            return false;
        return true;
    }

    public static boolean isMarried(String userID)
    {
        if (marriedUsers.containsValue(userID) || marriedUsers.containsKey(userID))
            return true;
        return false;
    }

    public static String getMarriedUser(String user)
    {
        if (marriedUsers.containsKey(user))
        return marriedUsers.get(user);
        else if (marriedUsers.containsValue(user))
            return getKeyByValue(marriedUsers, user);
        return "No Married User";
    }

    public static long timeToMS(int hours, int minutes, int seconds) {
        if (seconds > 59 || seconds < 0) {
            return -1;
        }
        if (minutes > 59 || minutes < 0) {
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

    public static void sendEmail(String subject, String message) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ilyswag48@gmail.com", "DiscordPassword123");
            }
        });
        try {
            javax.mail.Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress("ilyswag48@gmail.com"));
            email.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse("ilyswag48@gmail.com"));
            email.setSubject(subject);
            email.setText(message);
            Transport.send(email);
        } catch (MessagingException mess) {
            mess.printStackTrace();
        }


    }

    public static int randomNum(int start, int end) {

        if (end < start) {
            int temp = end;
            end = start;
            start = temp;
        }

        return (int) Math.floor(Math.random() * (end - start + 1) + start);
    }

    public static JsonElement getJsonFromURL(String url) throws IOException, IllegalArgumentException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        if (response.body() == null) {
            throw new IllegalArgumentException("Response returned no content");
        }
        try {
            return parser.parse(response.body().string());
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Response didn't return json!");
        }
    }

}
