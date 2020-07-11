package me.comu.exeter.core;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.events.*;
import me.comu.exeter.handlers.EcoJSONHandler;
import me.comu.exeter.handlers.UsernameHistoryHandler;
import me.comu.exeter.handlers.WhitelistedJSONHandler;
import me.comu.exeter.musicplayer.GuildMusicManager;
import me.comu.exeter.musicplayer.PlayerManager;
import me.comu.exeter.musicplayer.TrackScheduler;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static me.comu.exeter.core.Core.jda;

public class LoginGUI extends JFrame implements ActionListener {
    // login
    private JButton startButton;
    private JButton stopButton;
    private JLabel jLabelLoginConfig;
    static JTextField jStatusField;
    public static JTextField field;
    static boolean running;
    static boolean shouldRenderConfigurations = true;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final LocalDateTime now = LocalDateTime.now();

    public LoginGUI() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        ImageIcon icon = new ImageIcon("icon.png");
        Image logo = icon.getImage();
        setIconImage(logo);
        JPanel jPanel1 = new JPanel();
        JLabel jLabelClose = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabelMin = new JLabel();
        JPanel jPanel2 = new JPanel();
        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();
        field = new JTextField();
        jStatusField = new JTextField();
        startButton = new JButton();
        stopButton = new JButton();
        jLabelLoginConfig = new JLabel();
        JLabel jbackarrowLabel = new JLabel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        jPanel1.setBackground(new Color(248, 148, 6));
        jLabelClose.setFont(new Font("Tahoma", Font.BOLD, 24));
        jLabelClose.setForeground(new Color(255, 255, 255));
        jLabelClose.setText("X");
        jLabelClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                LoginGUI.this.jLabelCloseMouseClicked(evt);
            }
        });
        jLabel2.setFont(new Font("Tahoma", Font.BOLD, 24));
        jLabel2.setForeground(new Color(255, 255, 255));
        jLabel2.setText("Discord Bot");
        jLabelMin.setFont(new Font("Tahoma", Font.BOLD, 24));
        jLabelMin.setForeground(new Color(255, 255, 255));
        jLabelMin.setText("\u2014");
        jLabelMin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelMin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                LoginGUI.this.jLabelMinMouseClicked(evt);
            }
        });
        final GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(jLabelMin).addGap(18, 18, 18).addComponent(jLabelClose).addGap(21, 21, 21)).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(29, 29, 29).addComponent(jLabel2).addContainerGap(236, 32767))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jLabelMin, -1, 38, 32767).addComponent(jLabelClose)).addContainerGap()).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2, -1, 40, 32767).addContainerGap())));
        jPanel2.setBackground(new Color(44, 62, 80));
        jLabel4.setFont(new Font("Tahoma", Font.PLAIN, 18));
        jLabel4.setForeground(new Color(236, 240, 241));
        jLabel4.setText("Token");
        jLabel5.setFont(new Font("Tahoma", Font.PLAIN, 18));
        jLabel5.setForeground(new Color(236, 240, 241));
        jLabel5.setText("Status");
        field.setBackground(new Color(108, 122, 137));
        field.setFont(new Font("Tahoma", Font.PLAIN, 14));
        field.setForeground(new Color(228, 241, 254));
        jStatusField.setBackground(new Color(108, 122, 137));
        jStatusField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jStatusField.setForeground(new Color(228, 241, 254));
        jStatusField.setText("AUTHORIZED");
        jStatusField.setEditable(false);
        startButton.setBackground(new Color(34, 167, 240));
        startButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        startButton.setForeground(new Color(255, 255, 255));
        startButton.setText("Start");
        startButton.addActionListener(this);
        stopButton.setBackground(new Color(242, 38, 19));
        stopButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        stopButton.setForeground(new Color(255, 255, 255));
        stopButton.setText("Close");
        stopButton.addActionListener(this);
        jLabelLoginConfig.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jLabelLoginConfig.setForeground(new Color(255, 255, 255));
        jLabelLoginConfig.setText("Made by swag#0014 | " + dtf.format(now));
        jLabelLoginConfig.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jbackarrowLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        jbackarrowLabel.setForeground(new Color(255, 255, 255));
        jbackarrowLabel.setText("\u2190");
        jbackarrowLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabelLoginConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                LoginGUI.this.jLabelLoginConfigMouseClicked(evt);
            }
        });
        final GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(31, 31, 31).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel5, -2, 92, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jStatusField)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel4, -2, 92, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(field, -2, 188, -2))).addGroup(jPanel2Layout.createSequentialGroup().addComponent(stopButton, -2, 91, -2).addGap(26, 26, 26).addComponent(startButton, -2, 91, -2))).addGroup(jPanel2Layout.createSequentialGroup().addGap(76, 76, 76).addComponent(jLabelLoginConfig))).addContainerGap(59, 32767)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(68, 68, 68).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(field, -2, -1, -2)).addGap(24, 24, 24).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jStatusField, -2, -1, -2)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(startButton, -2, 38, -2).addComponent(stopButton, -2, 38, -2)).addGap(18, 18, 18).addComponent(jLabelLoginConfig).addContainerGap(22, 32767)));
        final GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jPanel1, -1, -1, 32767).addComponent(jPanel2, -1, -1, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, -2, -1, -2).addGap(0, 0, 0).addComponent(jPanel2, -2, -1, -2)));
        pack();
    }

    private void jLabelCloseMouseClicked(final MouseEvent evt) {
        if (running)
            jda.shutdownNow();
        System.exit(0);
    }

    private void jLabelMinMouseClicked(final MouseEvent evt) {
        setState(1);
    }

    private void jLabelLoginConfigMouseClicked(final MouseEvent evt) {
        if (!running) {
            jLabelLoginConfig.setText("Bot Must Be Running");
        } else if (shouldRenderConfigurations) {
            if (jLabelLoginConfig.getText().equalsIgnoreCase("bot must be running"))
                jLabelLoginConfig.setText("Made by swag#0014 | " + dtf.format(now));
            shouldRenderConfigurations = false;
            ConfigGUI configGUI = new ConfigGUI();
            configGUI.setVisible(true);

        }

    }


    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource().equals(startButton)) {
            if (running)
                jda.shutdownNow();
            if (field.getText() == null) {
                return;
            }
            try {
          /*      try {
                    if (!HWIDUtils.get("https://pastebin.com/raw/WYd6SZmC").contains(HWIDUtils.getHWID())) {
                        jStatusField.setText("NOT AUTHORIZED");
                        return;
                    }
                } catch (IOException | NoSuchAlgorithmException ex) {
                    jStatusField.setText("NOT AUTHORIZED");
                    return;
                }*/
                jStatusField.setText("AUTHORIZED");
                stopButton.setText("Stop");
                String TOKEN = field.getText().trim();
                EventWaiter eventWaiter = new EventWaiter();
                CommandManager commandManager = new CommandManager(eventWaiter);
                Listener listener = new Listener(commandManager);
                org.slf4j.Logger logger = LoggerFactory.getLogger(Core.class);
                WebUtils.setUserAgent("Mozilla/5.0 Discord Bot");
                Config.buildDirectory("cache", "cache");
                EcoJSONHandler.loadEconomyConfig(new File("economy.json"));
                WhitelistedJSONHandler.loadWhitelistConfig(new File("whitelisted.json"));
                UsernameHistoryHandler.loadUsernameHistoryConfig(new File("unhistory.json"));
                jda = JDABuilder.create(TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).setActivity(Activity.streaming("ily swag", "https://www.twitch.tv/souljaboy/")).addEventListeners(listener).build().awaitReady();
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
                TrackScheduler.startAudioManager(PlayerManager.buildMusicPlayer(GuildMusicManager.schedulerHook));
                logger.info("Successfully booted");
                jStatusField.setText("Running | " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator());
                running = true;
            } catch (LoginException login) {
                jStatusField.setText("Invalid Token");
                login.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                running = true;
                jStatusField.setText("Gateway Connection Throttle | " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator());
                    ex.printStackTrace();
            }
        }
        if (e.getSource().equals(stopButton)) {
            if (stopButton.getText().equalsIgnoreCase("stop")) {
                if (running) {
                    jda.shutdownNow();
                    running = false;
                    jStatusField.setText("Not Running");
                    stopButton.setText("Close");
                }
            } else if (stopButton.getText().equalsIgnoreCase("close")) {
                System.exit(0);
            }
        }
/*        if (e.getSource().equals(update))
        {
            if (running)
            {
                this.add(this.guilds);
                StringBuffer guildBuffer = new StringBuffer("Guilds: \n");
                for (Guild guild : jda.getGuilds())
                {
                    guildBuffer.append(guild.getName() + "(" + guild.getMembers().size() + ")\n");
                }
                guilds.setText(guildBuffer.toString());
            }
        }*/

    }


}


