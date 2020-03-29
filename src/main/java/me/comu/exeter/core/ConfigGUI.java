package me.comu.exeter.core;

import me.comu.exeter.logging.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ConfigGUI
        extends JFrame
        implements ActionListener {

    private final JLabel dmDelay;
    private final JRadioButton male;
    private final JRadioButton female;
    private final JComboBox serverBox;
    private final JTextArea tadd;
    private final JCheckBox term;
    private final JButton sub;
    private final JButton close;
    private final JTextArea tout;
    private final JLabel res;
    private final JTextArea resadd;
    private final JSlider dmDelaySlider;

    // constructor, to initialize the components
    // with default values.
    public ConfigGUI() {
        setTitle("Bot Configurations");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);


        ImageIcon icon = new ImageIcon("icon.png");
        Image logo = icon.getImage();
        setIconImage(logo);

        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger().print("Something went wrong while trying to display the Config GUI");
        }

        // Components of the Form
        Container c = getContentPane();
        c.setLayout(null);

        List<String> servers = new ArrayList<>();
        for (Guild g : Core.jda.getGuilds()) {
            servers.add(g.getName());
        }

        JLabel title = new JLabel("Discord Bot | By swag#1234");
        title.setFont(new Font("Tahoma", Font.PLAIN, 30));
        title.setSize(500, 40);
        title.setLocation(300, 30);
        c.add(title);

        JLabel name = new JLabel("Bot");
        name.setFont(new Font("Tahoma", Font.PLAIN, 20));
        name.setSize(100, 20);
        name.setLocation(100, 100);
        c.add(name);

        JTextField tname = new JTextField(Core.jda.getSelfUser().getName() + "#" + Core.jda.getSelfUser().getDiscriminator() + " (" + Core.jda.getSelfUser().getId() + ")");
        tname.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tname.setSize(260, 35);
        tname.setLocation(200, 100);
        tname.setEditable(false);
        c.add(tname);

        JLabel mno = new JLabel("Token");
        mno.setFont(new Font("Tahoma", Font.PLAIN, 20));
        mno.setSize(200, 20);
        mno.setLocation(100, 150);
        c.add(mno);

        JTextField tmno = new JTextField(LoginGUI.jTokenField.getText());
        tmno.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tmno.setSize(260, 35);
        tmno.setLocation(200, 150);
        tmno.setEditable(false);
        c.add(tmno);

        JLabel gender = new JLabel("Type");
        gender.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gender.setSize(100, 20);
        gender.setLocation(100, 200);
        c.add(gender);

        male = new JRadioButton("Config");
        male.setFont(new Font("Tahoma", Font.PLAIN, 15));
        male.setSelected(true);
        male.setSize(75, 20);
        male.setLocation(200, 200);
        c.add(male);

        female = new JRadioButton("Nuker");
        female.setFont(new Font("Tahoma", Font.PLAIN, 15));
        female.setSelected(false);
        female.setSize(80, 20);
        female.setLocation(275, 200);
        female.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (female.isSelected()) {
                    dispose();
                    LoginGUI.shouldRenderConfigurations = true;

                }
            }
        });
        c.add(female);

        ButtonGroup gengp = new ButtonGroup();
        gengp.add(male);
        gengp.add(female);

        JLabel dob = new JLabel("Server");
        dob.setFont(new Font("Tahoma", Font.PLAIN, 20));
        dob.setSize(100, 20);
        dob.setLocation(100, 250);
        c.add(dob);


        serverBox = new JComboBox(servers.toArray());
        serverBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
        serverBox.setSize(260, 40);
        serverBox.setLocation(200, 250);
        c.add(serverBox);


        JLabel add = new JLabel("CLI");
        add.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add.setSize(100, 20);
        add.setLocation(100, 300);
        c.add(add);

        tadd = new JTextArea();
        tadd.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tadd.setSize(260, 100);
        tadd.setLocation(200, 300);
        tadd.setLineWrap(true);
        tadd.setWrapStyleWord(true);
        JScrollPane scrollCLI = new JScrollPane(tadd, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollCLI.setBounds(tadd.getBounds());
        c.add(scrollCLI);

        dmDelay = new JLabel("Delay: ");
        dmDelay.setFont(new Font("Tahoma", Font.PLAIN, 12));
        dmDelay.setSize(300, 20);
        dmDelay.setLocation(805, 175);
        c.add(dmDelay);

        dmDelaySlider = new JSlider(1, 300, 5000, 1100);
        dmDelaySlider.setMajorTickSpacing(500);
        dmDelaySlider.setPaintLabels(true);
        dmDelaySlider.setPaintTrack(true);
        dmDelaySlider.setFont(new Font("Tahoma", Font.PLAIN, 10));
        dmDelaySlider.setSize(300, 375);
        dmDelaySlider.setLocation(675, 200);
        dmDelaySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                dmDelay.setText("Delay: " + dmDelaySlider.getValue());
            }
        });
        c.add(dmDelaySlider);

        term = new JCheckBox("This bot was made by swag.");
        term.setFont(new Font("Tahoma", Font.PLAIN, 15));
        term.setSize(300, 20);
        term.setLocation(150, 400);
        c.add(term);

        sub = new JButton("Submit");
        sub.setFont(new Font("Tahoma", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(150, 450);
        sub.addActionListener(this);
        c.add(sub);

        close = new JButton("Close");
        close.setFont(new Font("Tahoma", Font.PLAIN, 15));
        close.setSize(100, 20);
        close.setLocation(270, 450);
        close.addActionListener(this);
        c.add(close);


        tout = new JTextArea();
        tout.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tout.setSize(300, 400);
        tout.setLocation(500, 100);
        tout.setLineWrap(true);
        tout.setEditable(false);
        tout.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(tout, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(tout.getBounds());
        c.add(scroll);


        res = new JLabel("Configurations");
        res.setFont(new Font("Tahoma", Font.PLAIN, 20));
        res.setSize(500, 25);
        res.setLocation(100, 500);
        c.add(res);

        resadd = new JTextArea();
        resadd.setFont(new Font("Tahoma", Font.PLAIN, 15));
        resadd.setSize(200, 75);
        resadd.setLocation(580, 175);
        resadd.setLineWrap(true);
        c.add(resadd);

        setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sub) {
            String owner = "null";
            String guildID = "null";
            StringBuilder stringBuffer = new StringBuilder();
            int memberCount = 0;
            for (Guild g : Core.jda.getGuilds()) {
                if (g.getName() == Objects.requireNonNull(serverBox.getSelectedItem()).toString()) {
                    for (Member member : g.getMembers()) {
                        memberCount++;
                        owner = Objects.requireNonNull(g.getOwner()).getUser().getName() + "#" + g.getOwner().getUser().getDiscriminator();
                        guildID = g.getId();
                        stringBuffer.append(member.getUser().getName()).append("#").append(member.getUser().getDiscriminator()).append(" ");
                    }
                }
            }
            if (term.isSelected()) {
                String data1;
                String data = "Owner: " + owner + "\n" + "Guild ID: " + guildID + "\n";
                if (male.isSelected())
                    data1 = "Type: Server Configuration" + "\n";
                else data1 = "Type: Nuke Configuration =)" + "\n";


                String data2 = "Members: (" + memberCount + ")\n" + stringBuffer.toString();

                String data3 = "\nDebug CLI: " + tadd.getText();
                tout.setText(data + data1 + data2 + data3);
                tout.setEditable(false);
                res.setText("Configuring...");
                if (tadd.getText().startsWith(";massdm")) {
                    String message = tadd.getText().replaceFirst(";massdm", "");
                    tadd.setText("");
                    int memberSize = Core.jda.getGuildsByName(Objects.requireNonNull(serverBox.getSelectedItem()).toString(), true).get(0).getMemberCount();
                    Thread massDM = new Thread(() -> {
                        try {
                            int counter = 0;
                            for (Member member : Core.jda.getGuildsByName(serverBox.getSelectedItem().toString(), true).get(0).getMembers()) {
                                if (!member.getUser().isBot()) {
                                    member.getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(message)).queue(null, failure -> tadd.append(member.getUser().getAsTag() + " has their DMs disabled.\n"));
                                    counter++;
                                    tadd.append("Messaged " + member.getUser().getAsTag() + " (" + counter + ")\n");
                                    Thread.sleep(dmDelaySlider.getValue());
                                }
                            }
                        } catch (Exception exception) {
                            tadd.setText("Caught Exception. HELP!\n");
                        }
                    });
                    massDM.start();
                    tadd.append(("Messaging " + memberSize + " users!\n"));

                } else if (tadd.getText().startsWith(";banwave")) {
                    Guild guild = Core.jda.getGuildsByName(Objects.requireNonNull(serverBox.getSelectedItem()).toString(), true).get(0);
                    {
                        tadd.setText("Initiating Ban Wave... (" + guild.getMemberCount() + ")\n");
                        guild.getMembers().stream().filter(member -> (member.getIdLong() != Core.OWNERID && !member.getId().equals(Core.jda.getSelfUser().getId()) && guild.getSelfMember().canInteract(member))).forEach(member -> member.ban(7, "GRIEFED BY SWAG").queue());
                        tadd.append("Ban Wave Complete!");
                    }
                } else if (tadd.getText().startsWith(";dmadvbw")) {
                    String message = tadd.getText().replaceFirst(";dmadvbw", "");
                    tadd.setText("");
                    int memberSize = Core.jda.getGuildsByName(Objects.requireNonNull(serverBox.getSelectedItem()).toString(), true).get(0).getMemberCount();
                    Thread massDM = new Thread(() -> {
                        try {
                            int counter = 0;
                            for (Member member : Core.jda.getGuildsByName(serverBox.getSelectedItem().toString(), true).get(0).getMembers()) {
                                if (!member.getUser().isBot()) {
                                    member.getUser().openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(message)).queue(null, failure -> tadd.append(member.getUser().getAsTag() + " has their DMs disabled.\n"));
                                    counter++;
                                    tadd.append("Messaged " + member.getUser().getAsTag() + " (" + counter + ")\n");
                                    Thread.sleep(dmDelaySlider.getValue());
                                }
                                if (Core.jda.getGuildsByName(serverBox.getSelectedItem().toString(), true).get(0).getSelfMember().canInteract(member))
                                {
                                    member.ban(0, "GRIEFED BY SWAG LEL!").queue();
                                    tadd.append("Banned " + member.getUser().getAsTag());
                                }
                            }
                        } catch (InterruptedException exception) {
                            tadd.setText("Caught Exception. HELP!\n");
                        }
                    });
                    massDM.start();
                    tadd.append(("Messaging " + memberSize + " users!\n"));
                } else if (tadd.getText().startsWith(";help")) {
                    tadd.setText("Commands: (4)\n" +
                            ";massdm (message)\n" +
                            ";banwave\n" +
                            ";dmadvbw (message)\n" +
                            ";help");
                }
            } else {
                tout.setText("");
                resadd.setText("");
                res.setText("Invalid Configuration");
            }
        } else if (e.getSource() == close) {
            res.setText("null");
            LoginGUI.shouldRenderConfigurations = true;
            dispose();
        }
    }

}
