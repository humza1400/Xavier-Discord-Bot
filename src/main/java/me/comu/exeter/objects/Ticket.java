package me.comu.exeter.objects;

import me.comu.exeter.utility.Utility;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ticket {

    private String author;
    private String guild;
    private String id;
    private String channel;
    private String transcript;
    private List<String> members;
    private String timeCreated;
    private String timeClosed;
    private String closer;

    public static List<Ticket> tickets = new ArrayList<>();

    public Ticket(String author, String guild, String channel, String id, String transcript, String members, String timeCreated, String timeClosed, String closer) {
        this.author = author;
        this.guild = guild;
        this.channel = channel;
        this.transcript = transcript;
        this.id = id;
        this.members = new ArrayList<>(Arrays.asList(members.split(",")));
        this.timeCreated = timeCreated;
        this.timeClosed = timeClosed;
        this.closer = closer;
    }


    public static Ticket of(String author, String guild, String channel, String id, List<String> members, Instant timeCreated) {
        return new Ticket(author, guild, channel, id, null, members, timeCreated, null, null);
    }

    public Ticket(String author, String guild, String channel, String id, String transcript, List<String> members, Instant timeCreated, String timeClosed, String closer) {
        this.author = author;
        this.guild = guild;
        this.channel = channel;
        this.transcript = transcript;
        this.id = id;
        if (members == null)
            this.members = new ArrayList<>();
        else
            this.members = members;
        setTimeCreated(timeCreated);
        this.timeClosed = timeClosed;
        this.closer = closer;
    }

    public Ticket(String author, String guild, String channel, String id, String transcript, List<String> members, String timeCreated, String timeClosed, String closer) {
        this.author = author;
        this.guild = guild;
        this.channel = channel;
        this.transcript = transcript;
        this.id = id;
        if (members == null)
            this.members = new ArrayList<>();
        else
            this.members = members;
        this.timeCreated = timeCreated;
        this.timeClosed = timeClosed;
        this.closer = closer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuild() {
        return guild;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public List<String> getMembers() {
        return members;
    }

    public String getMembersAsString() {
        return members.stream().map(String::valueOf).collect(Collectors.joining(", ", "{", "}"));
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public void addMember(String member) {
        this.members.add(member);
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = Utility.dtf.format(timeCreated);
    }

    public String getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(Instant timeClosed) {
        this.timeClosed = Utility.dtf.format(timeClosed);
    }

    public String getCloser() {
        return closer;
    }

    public void setCloser(String closer) {
        this.closer = closer;
    }

    public static Ticket findTicket(String hash) {
        for (Ticket ticket : tickets) {
            if (hash.equals(ticket.getId()))
                return ticket;
        }
        return null;
    }

    public static boolean isTicketChannel(String textChannel) {
        for (Ticket ticket : tickets) {
            if (textChannel.equals(ticket.getChannel())) {
                return true;
            }
        }
        return false;
    }


    public static void loadTickets(List<Ticket> ticketList) {
        tickets = ticketList;
    }


    public static String getAuthorById(String ticketId) {
        for (Ticket ticket : tickets) {
            if (ticket.getId().equals(ticketId)) {
                return ticket.getAuthor();
            }
        }
        return null;
    }
    public static Ticket getTicketByAuthorAndGuild(String author, String guild) {
        for (Ticket ticket : tickets) {
            if (ticket.getAuthor().equals(author) && ticket.getGuild().equalsIgnoreCase(guild)) {
                return ticket;
            }
        }
        return null;
    }

}
