package me.comu.exeter.core;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.commands.CommandManager;
import me.comu.exeter.commands.Listener;
import me.comu.exeter.events.*;
import me.comu.exeter.handler.Handler;
import me.comu.exeter.handler.handlers.*;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.musicplayer.TrackScheduler;
import me.comu.exeter.pagination.method.Pages;
import me.comu.exeter.utility.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Core {

    /*
    TODO:
    - Fix anti-nuke for channels to recreate accurately and smartly
    - Add trusted members feature that are allowed to whitelist, unwhitelist, blacklist, and unblacklist
    - add jsonhandler for tickets and archived tickets
    - fix nsfw cmds (endpoint was removed)
    - TODO: ADD EASY MEMBER CACHING BY USING FIND SIMILAR METHOD IN UTILITY
     */

    private JDA jda;
    public static String PREFIX = ".";
    private static String token = Config.get("TOKEN");
    public static Long OWNERID = Long.parseLong(Config.get("OWNER"));
    private int colorTheme = 0xFF633B;
    private int colorErrorTheme = 0x3B3B9B;
    private static Core instance;
    private CommandManager commandManager;


    public static void main(final String[] args) {
        token = args.length == 0 ? token : args[0];
        if (Config.get("GUI").equalsIgnoreCase("false")) {
            new Core();
        } else if (Config.get("GUI").equalsIgnoreCase("true")) {
            try {
                for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException |
                     InstantiationException ex) {
                Logger.getLogger(LoginGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            EventQueue.invokeLater(() -> new LoginGUI().setVisible(true));
        } else {
            me.comu.exeter.logging.Logger.getLogger().print("Couldn't decide whether to launch GUI or headless");
            System.exit(0);
        }
    }

    protected Core() {
        instance = this;
        initialize();
    }

    public void initialize() {
        if (jda != null)
            jda.shutdown();
        Config.buildDirectory("cache", "cache");
        loadConfigs();
        EventWaiter eventWaiter = new EventWaiter();
        commandManager = new CommandManager(eventWaiter);
        Listener listener = new Listener(commandManager);
        org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);
        try {
            jda = JDABuilder.create(token == null ? Config.get("TOKEN") : token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).setMemberCachePolicy(MemberCachePolicy.ALL).setChunkingFilter(ChunkingFilter.ALL).setRawEventsEnabled(true).setActivity(Activity.streaming("um hi", "https://www.twitch.tv/souljaboy/")).addEventListeners(listener).build().awaitReady();
            jda.addEventListener(eventWaiter);
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
            jda.addEventListener(new DMWizzEvent());
            jda.addEventListener(new SuggestionMessageCleanerEvent());
            jda.addEventListener(new CreateAChannelEvent());
            jda.addEventListener(new BlacklistedWordsEvent());
            jda.addEventListener(new SnipeEvent());
            jda.addEventListener(new ReactionRoleEvent());
            jda.addEventListener(new UsernamePresenceUpdateEvent());
            jda.addEventListener(new TicketEvent());
            Pages.activate(jda);
            TrackScheduler.startAudioManager(PlayerManager.buildMusicPlayer(GuildMusicManager.schedulerHook));
            logger.info("Successfully booted");
        } catch (Exception ex) {
            logger.info("Caught Exception trying to initialize JDA Instance!");
            ex.printStackTrace();
        }
    }

    public void shutdownThread() {
        if (Config.get("GUI").equalsIgnoreCase("true")) {
            LoginGUI.running = false;
            LoginGUI.jStatusField.setText("NOT RUNNING");
        }
        jda.shutdown();
        System.exit(0);
    }

    private EcoJSONHandler ecoHandler;
    private InvoiceJSONHandler invoiceHandler;
    private TicketJSONHandler ticketHandler;
    private UsernameHistoryHandler usernameHistoryHandler;
    private WhitelistedJSONHandler whitelistedHandler;
    private BlacklistedJSONHandler blacklistedJSONHandler;
    private AntiRaidGuildJSONHandler antiRaidGuildJSONHandler;
    private AuthorizedJSONHandler authorizedJSONHandler;
    private CmdBlacklistJSONHandler cmdBlacklistJSONHandler;
    private final ArrayList<Handler> handlers = new ArrayList<>();

    protected void loadConfigs() {
        handlers.add(ecoHandler = new EcoJSONHandler(new File("economy.json")));
        handlers.add(whitelistedHandler = new WhitelistedJSONHandler(new File("whitelisted.json")));
        handlers.add(usernameHistoryHandler = new UsernameHistoryHandler(new File("unhistory.json")));
        handlers.add(ticketHandler = new TicketJSONHandler(new File("tickets.json")));
        handlers.add(invoiceHandler = new InvoiceJSONHandler(new File("invoices.json")));
        handlers.add(blacklistedJSONHandler = new BlacklistedJSONHandler(new File("blacklisted.json")));
        handlers.add(antiRaidGuildJSONHandler = new AntiRaidGuildJSONHandler(new File("antiraid.json")));
        handlers.add(authorizedJSONHandler = new AuthorizedJSONHandler(new File("authorized.json")));
        handlers.add(cmdBlacklistJSONHandler = new CmdBlacklistJSONHandler(new File("cmdblacklist.json")));
    }

    protected void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public static Core getInstance() {
        return instance;
    }

    public static void setInstance(Core core) {
        instance = core;
    }

    public JDA getJDA() {
        return jda;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EcoJSONHandler getEcoHandler() {
        return ecoHandler;
    }

    public InvoiceJSONHandler getInvoiceHandler() {
        return invoiceHandler;
    }

    public TicketJSONHandler getTicketHandler() {
        return ticketHandler;
    }

    public UsernameHistoryHandler getUsernameHistoryHandler() {
        return usernameHistoryHandler;
    }

    public WhitelistedJSONHandler getWhitelistedHandler() {
        return whitelistedHandler;
    }

    public BlacklistedJSONHandler getBlacklistedJSONHandler() {
        return blacklistedJSONHandler;
    }

    public AntiRaidGuildJSONHandler getAntiRaidGuildJSONHandler() {
        return antiRaidGuildJSONHandler;
    }

    public AuthorizedJSONHandler getAuthorizedJSONHandler() {
        return authorizedJSONHandler;
    }

    public CmdBlacklistJSONHandler getCmdBlacklistJSONHandler() {
        return cmdBlacklistJSONHandler;
    }

    public void saveConfigs() {
        handlers.forEach(Handler::saveConfig);
    }

    public void saveConfig(Handler handler) {
        handler.saveConfig();
    }

    public int getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(int COLOR_THEME) {
        this.colorTheme = COLOR_THEME;
    }

    public int getErrorColorTheme() {
        return colorErrorTheme;
    }

    public void setErrorColorTheme(int COLOR_ERROR_THEME) {
        this.colorErrorTheme = COLOR_ERROR_THEME;
    }

}


