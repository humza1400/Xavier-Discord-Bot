//package me.comu.exeter.util;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import net.dv8tion.jda.internal.utils.IOUtil;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import javax.annotation.Nullable;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.TimeUnit;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.InflaterInputStream;
//
//
//@SuppressWarnings({"unused", "WeakerAccess", "ConstantConditions"})
//public final class WebUtils extends Reliqua {
//
//    public static final WebUtils ins = new WebUtils();
//    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) discord/0.0.306 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36";
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    private WebUtils() {
//        super(
//                new OkHttpClient.Builder()
//                        .connectTimeout(30L, TimeUnit.SECONDS)
//                        .readTimeout(30L, TimeUnit.SECONDS)
//                        .writeTimeout(30L, TimeUnit.SECONDS)
//                        .build(),
//                null,
//                true
//        );
//    }
//
//    public PendingRequest<String> getText(String url) {
//        return prepareGet(url).build(
//                (response) -> response.body().string(),
//                WebParserUtils::handleError
//        );
//    }
//
//    public PendingRequest<Document> scrapeWebPage(String url) {
//        return prepareGet(url, ContentType.TEXT_HTML).build(
//                (response) -> Jsoup.parse(response.body().string()),
//                WebParserUtils::handleError
//        );
//    }
//
//    public PendingRequest<ObjectNode> getJSONObject(String url) {
//        return prepareGet(url, ContentType.JSON).build(
//                (res) -> WebParserUtils.toJSONObject(res, mapper),
//                WebParserUtils::handleError
//        );
//    }
//
//    public PendingRequest<ArrayNode> getJSONArray(String url) {
//        return prepareGet(url, ContentType.JSON).build(
//                (res) -> (ArrayNode) mapper.readTree(WebParserUtils.getInputStream(res)),
//                WebParserUtils::handleError
//        );
//    }
//
//    public PendingRequest<InputStream> getInputStream(String url) {
//        return prepareGet(url).build(
//                WebParserUtils::getInputStream,
//                WebParserUtils::handleError
//        );
//    }
//
//    public PendingRequest<byte[]> getByteStream(String url) {
//        return prepareGet(url).build(
//                (res) -> IOUtil.readFully(WebParserUtils.getInputStream(res)),
//                WebParserUtils::handleError
//        );
//    }
//
//    public PendingRequestBuilder prepareGet(String url, ContentType accept) {
//        return createRequest(defaultRequest()
//                .url(url)
//                .addHeader("Accept", accept.getType()));
//    }
//
//    public PendingRequestBuilder prepareGet(String url) {
//        return prepareGet(url, ContentType.ANY);
//    }
//
//    public PendingRequestBuilder postRequest(String url, IRequestBody body) {
//        return createRequest(
//                defaultRequest()
//                        .url(url)
//                        .header("content-Type", body.getContentType())
//                        .post(body.toRequestBody())
//        );
//    }
//
//    public ArrayNode translate(String sourceLang, String targetLang, String input) {
//        return (ArrayNode) getJSONArray(
//                "https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + sourceLang + "&tl=" + targetLang + "&dt=t&q=" + input
//        )
//                .execute()
//                .get(0)
//                .get(0);
//    }
//
//    public PendingRequest<String> shortenUrl(String url, String domain, String apiKey, GoogleLinkLength linkLength) {
//        final ObjectNode json = mapper.createObjectNode();
//
//        json.set("dynamicLinkInfo",
//                mapper.createObjectNode()
//                        .put("domainUriPrefix", domain)
//                        .put("link", url)
//        );
//        json.set("suffix",
//                mapper.createObjectNode()
//                        .put("option", linkLength.name())
//        );
//
//        try {
//            return postRequest(
//                    "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=" + apiKey,
//                    JSONRequestBody.fromJackson(json)
//            )
//                    .build(
//                            (r) -> toJSONObject(r, mapper).get("shortLink").asText(),
//                            WebParserUtils::handleError
//                    );
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//
//            return null;
//        }
//    }
//
//    public <T> PendingRequest<T> prepareRaw(Request request, ResponseMapper<T> mapper) {
//        return createRequest(request).build(mapper, WebParserUtils::handleError);
//    }
//
//    public static String getUserAgent() {
//        return USER_AGENT;
//    }
//
//    public static void setUserAgent(String userAgent) {
//        USER_AGENT = userAgent;
//    }
//
//    public static Request.Builder defaultRequest() {
//        return new Request.Builder()
//                .get()
//                .addHeader("User-Agent", USER_AGENT)
//                .addHeader("cache-control", "no-cache");
//    }
//
//    public static String urlEncodeString(String input) {
//        return URLEncoder.encode(input, StandardCharsets.UTF_8);
//    }
//
//    @Nullable
//    private static ObjectNode toJSONObject(Response response) throws IOException {
//        return toJSONObject(response, new ObjectMapper());
//    }
//
//    @Nullable
//    public static ObjectNode toJSONObject(Response response, ObjectMapper mapper) throws IOException {
//        return (ObjectNode) mapper.readTree(getInputStream(response));
//    }
//
//    public static InputStream getInputStream(Response response) {
//        final ResponseBody body = response.body();
//
//        if (body == null) {
//            throw new IllegalStateException("Body should never be null");
//        }
//
//        final String encoding = response.header("Content-Encoding");
//
//        if (encoding != null) {
//            switch (encoding.toLowerCase()) {
//                case "gzip":
//                    try {
//                        return new GZIPInputStream(body.byteStream());
//                    } catch (IOException e) {
//                        throw new IllegalStateException("Received Content-Encoding header of gzip, but data is not valid gzip", e);
//                    }
//                case "deflate":
//                    return new InflaterInputStream(body.byteStream());
//            }
//        }
//
//        return body.byteStream();
//    }
//
//    public static <T> void handleError(RequestContext<T> context) {
//        final Response response = context.getResponse();
//        final ResponseBody body = response.body();
//
//        if (body == null) {
//            context.getErrorConsumer().accept(new RequestException("Unexpected status code " + response.code() + " (No body)", context.getCallStack()));
//            return;
//        }
//
//        JsonNode json = null;
//
//        try {
//            json = toJSONObject(response);
//        } catch (Exception ignored) {
//        }
//
//        if (json != null) {
//            context.getErrorConsumer().accept(new RequestException("Unexpected status code " + response.code() + ": " + json, context.getCallStack()));
//        } else {
//            context.getErrorConsumer().accept(new RequestException("Unexpected status code " + response.code(), context.getCallStack()));
//        }
//    }
//}
//
//
