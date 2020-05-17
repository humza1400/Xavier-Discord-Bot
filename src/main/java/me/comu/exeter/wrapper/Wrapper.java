package me.comu.exeter.wrapper;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.comu.exeter.core.Config;
import me.comu.exeter.core.Core;
import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.JDA;
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
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wrapper {

    //    public static List<Shard> shards = new ArrayList<>();
    private static final JsonParser parser = new JsonParser();
    public static final Map<String, String> marriedUsers = new HashMap<>();

    public static void sendPrivateMessage(JDA jda, String userId, String content) {
        RestAction<User> action = jda.retrieveUserById(userId);
        action.queue((user) -> user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue(null, (error) -> Logger.getLogger().print("Couldn't message " + Objects.requireNonNull(Core.jda.getUserById(userId)).getAsTag()))));
    }

    public void sendPrivateMessageWithDelay(JDA jda, String userId, String content, long delay, TimeUnit timeUnit) {
        RestAction<User> action = jda.retrieveUserById(userId);
        action.queue((user) -> user.openPrivateChannel().queueAfter(delay, timeUnit, (channel) -> channel.sendMessage(content).queue(null, (error) -> Logger.getLogger().print("Couldn't message " + Objects.requireNonNull(Core.jda.getUserById(userId)).getAsTag()))));
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
        return !message.getAuthor().isBot();
    }

    public static boolean isMarried(String userID) {
        return marriedUsers.containsValue(userID) || marriedUsers.containsKey(userID);
    }

    public static String getMarriedUser(String user) {
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
        } catch (Exception ignored) {
        }
        return ipAddress;
    }

    public static String getHostInformation() {
        InetAddress ip;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName() + "/" + InetAddress.getLocalHost().getHostAddress();
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
        properties.put("mail.smtp.port", "25");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.get("EMAIL"), Config.get("EMAILPASS"));
            }
        });
        try {
            javax.mail.Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress(Config.get("EMAIL")));
            email.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(Config.get("EMAIL")));
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

    public static InputStream imageFromUrl(String url) {
        if (url == null) {
            return null;
        } else {
            try {
                URL u = new URL(url);
                URLConnection urlConnection = u.openConnection();
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
                return urlConnection.getInputStream();
            } catch (IllegalArgumentException | IOException var3) {
                return null;
            }
        }
    }

    public static Color getAmbientColor()
    {
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static void saveImage(String imageUrl, String path, String name) {
        try {
            URLConnection connection = new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(path + "/" + name + ".png");
            fos.write(response);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveGif(String imageUrl, String path, String name) {
        try {
            URLConnection connection = new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(path + "/" + name + ".gif");
            fos.write(response);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

