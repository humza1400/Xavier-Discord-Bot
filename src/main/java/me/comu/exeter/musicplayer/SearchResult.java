package me.comu.exeter.musicplayer;

public class SearchResult {
    private String title;
    private String author;
    private String link;
    private String text;
    private String thumbnail;

    /**
     * Null SearchResult Constructor
     */
    public SearchResult()
    {
        this.title = null;
        this.author = null;
        this.link = null;
        this.text = null;
        this.thumbnail = null;
    }

    /**
     * @param title SearchResult title
     * @param link SearchResult link
     * @param author SearchResult lyrics author/artist
     * @param text SearchResult description
     * @param thumbnail SearchResult album picture, icon
     */
    public SearchResult(String title, String author, String link, String text, String thumbnail)
    {
        this.title = title;
        this.author = author;
        this.link = link;
        this.text = text;
        this.thumbnail = thumbnail;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String info)
    {
        this.author = info;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}