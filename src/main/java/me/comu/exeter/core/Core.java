package me.comu.exeter.core;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.commands.economy.EcoJSONLoader;
import me.comu.exeter.events.*;
import me.comu.exeter.logging.Logger;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Core {

    public static  JDA jda;
    public final static String[] WHITELISTEDPEOPLE = new String[]{"175728291460808706"/*comu*/, "492828741555453972"/*tarik*/, "234394647324131328" /*tarik alt*/, "217090758593871872" /*del*/, "544668778600988672" /*macmiller*/};
    public static final long OWNERID = 175728291460808706L;
    private static final String TOKEN = "NjMxNjU0MzE5MzQyNjE2NTk2.XfLsXw.RFnQayLGY5lmr38NAk79JDNvp3s";
    private static final boolean WHITELIST = false;
    public static boolean WHITELISTPERMS = true;
    public static final String DEBUG = "[DEBUG] ";
    public static String PREFIX = ";";
    public static long runningTime;
    public static final String youtubeAPIKey = "AIzaSyAls9zrVVQtZksm-tMrKLhmXx3T1hrt_5c";

    public static void main(String[] args) {
        new Core();

    }

    private Core() {
        EventWaiter eventWaiter = new EventWaiter();
        CommandManager commandManager = new CommandManager(eventWaiter);
        Listener listener = new Listener(commandManager);
        org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);
        EcoJSONLoader.loadEconomyConfig(new File("src/main/java/me/comu/exeter/commands/economy/economy.json"));
        Logger.getLogger().print("Loaded economy.json");
        WebUtils.setUserAgent("Mozilla/5.0 Exeter Discord Bot/Comu#0691");

        try {
           jda = new JDABuilder(AccountType.BOT).setToken(TOKEN)./*setActivity((Activity.streaming("ily comu", "https://www.twitch.tv/ilycomu/")))*/setStatus(OnlineStatus.DO_NOT_DISTURB).addEventListeners(new Listener(commandManager)).build().awaitReady();
            new Timer().scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                       jda.getPresence().setActivity(Activity.watching(String.format("over %s users", jda.getGuildById("645841446817103912").getMembers().size())));
                }
            },0,5000);
            jda.addEventListener(new KickEvent());
            jda.addEventListener(new BanEvent());
           jda.addEventListener(new LogMessageReceivedEvent());
           jda.addEventListener(new RainbowRoleEvent());
           jda.addEventListener(new AntiRaidEvent());
           jda.addEventListener(new EditEvent());
           jda.addEventListener(new GuildMemberJoinedEvent());
            jda.addEventListener(new GuildMemberLeaveEvent());
            jda.addEventListener(new GuildMessageListenerResponderEvent());
           logger.info("Successfully Booted");
        } catch (LoginException | InterruptedException e) {
            logger.info("Caught Exception! (LoginException | InterruptedException)");
        }
    }
    public static void shutdownThread() {
        jda.shutdownNow();
        System.exit(0);
    }


    public static Color getRandomColor() {
        final Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }


}
