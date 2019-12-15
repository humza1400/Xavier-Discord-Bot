package me.comu.exeter.musicplayer;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class WebScraper {

    /**
     * Lyrics getter from Genius.com
     *
     * @param link the value of lyrics URI, works with Search.lyricsSearch
     * @throws IOException
     */
    public static String getLyrics(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        String html = doc.select("div.lyrics").html();
        Matcher matcher = Pattern.compile("(?<=<p>)(?s)(.+)(?=<\\/p>)").matcher(html);

        String result = "";
        if (matcher.find()) {
            result = matcher.group()
                    .replaceAll("<br>", "\n") // Line Break
                    .replaceAll("(?s)<.*?>", "") // Remove html elements
                    .replaceAll("(?m)(^\\s)", "") // Remove empty space at start of the line
                    .replaceAll("\n\n", "\n"); // Remove two line breaks
        }
        return StringEscapeUtils.unescapeHtml4(result);
    }

    /**
     * Get specific information from a IMDb Title Link.
     *
     * @param result from Search#IMDbSearch, only used the link.
     * @return EmbedBuilder
     * @throws IOException
     */
    public static EmbedBuilder getIMDbInfo(SearchResult result) throws IOException {
        Document doc = Jsoup.connect(result.getLink()).timeout(0).get();

        //Title, Content Rating, Duration, Genre, Release Date
        Elements info = doc.select("div#main_top>.title-overview>div.heroic-overview>div.vital")
                .select("div.title_block>div.title_bar_wrapper");

        String title = info.select("h1").get(0).text();
        Elements sub = info.select("div.subtext");
        String contentRating = sub.text().split(" | ")[0];
        String duration = sub.select("time").text();
        String genre = sub.select(".itemprop").text().replaceAll(" ", ", ");
        String releaseDate = sub.select("a[title=\"See more release dates\"]").text();
        //System.out.println(title + "\n" + contentRating + "\t" + duration + "\t" + genre + "\t" + releaseDate);

        //Plot, Crew Credit
        Elements plot = doc.select("div#main_top>.title-overview>div.heroic-overview>div.plot_summary_wrapper");

        String summary = plot.select("div.summary_text").text();
        String director = plot.select("span[itemprop=director]").text();
        String stars = plot.select("span[itemprop=actors").text();
        //System.out.println(summary + "\n" + director + "\t" + STARS);

        //Ratings
        Elements rate = doc.select("div#main_top>.title-overview>div.heroic-overview>div.vital")
                .select("div.title_block>div.title_bar_wrapper>div.ratings_wrapper>div.imdbRating");

        String rating = "**" + rate.select("div.ratingValue").text() + "**";
        String rates = " | " + rate.select("a").text() + " rates";
        String metaScore = plot.select("div.titleReviewBar>div.titleReviewBarItem>a").text();
        //System.out.println(rating + "\t" + rates + "\t" + metaScore);

        //Nominations
        String top = "**" + doc.select("div#main_bottom>div#titleAwardsRanks>strong").text() + "**";
        String middle = " | ";
        String nomination = doc.select("div#main_bottom>div#titleAwardsRanks>span[itemprop=awards]").text(); //doc.select("div#main_bottom>div#titleAwardsRanks").TEXT();
        //System.out.println(top + "\t" + nomination);

        //Assign "None" to null datas
        //if("".equals(title))
        if ("".equals(summary))
            summary = "None";
        if ("".equals(director))
            director = "None";
        if ("".equals(stars))
            stars = "None";
        if ("".equals(metaScore))
            metaScore = "None";
        if ("****".equals(top))
            top = "";
        else
            top += middle;
        if ("".equals(nomination))
            nomination = "No nomination or awards.";

        //Build MessageEmbed
        EmbedBuilder imdb = new EmbedBuilder();
        imdb.setColor(0xfffff);
        imdb.setThumbnail(result.getThumbnail());
        imdb.setAuthor("IMDb Search", result.getLink(), null);
        imdb.addField("Title", title, true);
        imdb.addField("Rating", contentRating, true);
        imdb.addField("Duration", duration, true);
        imdb.addField("Genre", genre, true);
        imdb.addField("Release Date", releaseDate, true);

        imdb.addField("Director(s)", director, true);
        imdb.addField( "Stars", stars, false);

        imdb.addField("IMDb Rating", rating + rates, true);
        imdb.addField("MetaScore", metaScore, true);

        imdb.addField("Nomination and Awards", top + nomination, true);

        imdb.addField("Plot", summary, true);

        return imdb;
    }

    /**
     * IMDb Thumbnail getter
     *
     * @param result the value of SearchResult to add thumbnail
     * @throws IOException
     */
    public static void getIMDbThumbNail(SearchResult result) throws IOException {
        Document doc = Jsoup.connect(result.getLink()).timeout(0).get();
        String pic = doc.select("div#main_top>.title-overview>div.heroic-overview>div.vital>div.slate_wrapper>div.poster").select("a>img").attr("src");
        //Change getter for special thumbnails
        if ("".equals(pic)) {
            try {
                pic = doc.select("div#content-2-wide").get(0).select("div#main_top").get(0).select(".title-overview").get(0).select(".heroic-overview").get(0).select(".minPosterWithPlotSummaryHeight").get(0).select("div.poster").select("a").select("img").attr("src");
            } catch (IndexOutOfBoundsException ioobe) {
                //Initialize pic direcctly if there is no thumbnail for the result
                pic = "http://ia.media-imdb.com/images/G/01/imdb/images/nopicture/32x44/film-3119741174._CB522736599_.png";
            }
        }
        result.setThumbnail(pic);
    }

    /**
     * YouTube Thumbnail Getter
     *
     * @param link
     * @throws IOException
     */
    public static String getYouTubeThumbNail(String link) throws IOException {
        return  "http://img.youtube.com/vi/" + link.split("\\?v=")[1] + "/0.jpg";
    }

    /**
     * Get the YouTube autoplay next song from the provided link
     *
     * @param link
     * @return
     */
    public static String getYouTubeAutoPlay(String link) throws IOException {
        Document doc = Jsoup.connect(link).timeout(0).get();
        String next = doc.select("#body-container>#page-container>#page>#content>#watch7-container>#watch7-main-container>#watch7-main")
                .select("#watch7-sidebar>#watch7-sidebar-contents>#watch7-sidebar-modules>.watch-sidebar-section>.autoplay-bar>.watch-sidebar-body")
                .select(".video-list>.video-list-item>.content-wrapper>.content-link").attr("href");

        return "https://www.youtube.com" + next;
    }

    /**
     * Generate a random bill board song
     *
     * @return the title of a random bill board song to be put into YouTube search
     */
    public static String randomBillboardSong() {
        try {
            Document doc = Jsoup.connect("http://www.billboard.com/rss/charts/hot-100").timeout(0).get();
            Elements titles = doc.select("item>chart_item_title");
            int random = new Random().nextInt(99);
            return titles.get(random).text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}