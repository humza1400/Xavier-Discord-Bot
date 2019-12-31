package me.comu.exeter.core;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.commands.admin.WhitelistedJSONHandler;
import me.comu.exeter.commands.economy.EcoJSONHandler;
import me.comu.exeter.events.*;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Core {

    public static JDA jda;
    public static final long OWNERID = 175728291460808706L;
    protected static final String HWIDURL = "https://pastebin.com/raw/mGiKYJrV";
    private static final String TOKEN = "NjMxNjU0MzE5MzQyNjE2NTk2.XgW9bg.nAsLNcordeEclkH4Y_Sn7i17r7w";
    //public static final String youtubeAPIKey = "AIzaSyAls9zrVVQtZksm-tMrKLhmXx3T1hrt_5c";
    public static final String DEBUG = "[DEBUG] ";
    public static String PREFIX = ";";

    public static void main(final String[] args) {
        new Core();
/*        Wrapper.sendEmail("Log Info On Startup","IP-Address: " + Wrapper.getIpaddress() + "\nHost Information: " + Wrapper.getHostInformation() + "");
        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex2) {
            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex2);
        } catch (IllegalAccessException ex3) {
            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex3);
        } catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGUI().setVisible(true); }

        });*/
    }

    private Core() {
        EventWaiter eventWaiter = new EventWaiter();
        CommandManager commandManager = new CommandManager(eventWaiter);
        Listener listener = new Listener(commandManager);
        org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);
        EcoJSONHandler.loadEconomyConfig(new File("src/main/java/me/comu/exeter/configs/economy/economy.json"));
        WhitelistedJSONHandler.loadWhitelistConfig(new File("src/main/java/me/comu/exeter/configs/economy/economy.json"));
        WebUtils.setUserAgent("Mozilla/5.0 Exeter Discord Bot/Comu#0691");

        try {
            jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).setActivity(Activity.streaming("ily swag#3231", "https://www.twitch.tv/souljaboy/")).setStatus(OnlineStatus.DO_NOT_DISTURB).addEventListeners(new Listener(commandManager)).build().awaitReady();
            //    new Timer().scheduleAtFixedRate(new TimerTask(){
            //          @Override
            //            public void run(){
            //jda.getPresence().setActivity(Activity.watching(String.format("over %s users", jda.getGuildById("645841446817103912").getMembers().size())));
            //              }
//            },0,5000);
            jda.addEventListener(new KickEvent());
            jda.addEventListener(new BanEvent());
            jda.addEventListener(new LogMessageReceivedEvent());
            jda.addEventListener(new RainbowRoleEvent());
            jda.addEventListener(new AntiRaidEvent());
            jda.addEventListener(new EditEvent(commandManager));
            jda.addEventListener(new GuildMemberJoinedEvent());
            jda.addEventListener(new GuildMemberLeaveEvent());
            jda.addEventListener(new GuildMessageListenerResponderEvent());
            logger.info("Successfully Booted");
        } catch (LoginException | InterruptedException e) {
            logger.info("Caught Exception! (LoginException | InterruptedException)");
        }
    }


    public static void shutdownThread() {
        LoginGUI.running = false;
        LoginGUI.jStatusField.setText("NOT RUNNING");
        jda.shutdownNow();
    }


}
