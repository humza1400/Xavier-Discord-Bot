package me.comu.exeter.util;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HWIDUtils
{
    public static String getHWID() throws NoSuchAlgorithmException {
        final StringBuilder s = new StringBuilder();
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        byte[] array;
        for (int length = (array = md5).length, j = 0; j < length; ++j) {
            final byte b = array[j];
            s.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
            if (i != md5.length - 1) {
                s.append("-");
            }
            ++i;
        }
        return s.toString();
    }

    public static String get(final String url) throws IOException {
        final HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        in.close();
        return response.toString();
    }

    public static String post(final String url, final Map<String, String> requestMap, final String body) throws IOException {
        final HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        if (requestMap != null) {
            requestMap.forEach(con::setRequestProperty);
        }
        con.setDoOutput(true);
        con.setDoInput(true);
        final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(body);
        wr.flush();
        wr.close();
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine).append("\n");
        }
        in.close();
        return response.toString();
    }
}
