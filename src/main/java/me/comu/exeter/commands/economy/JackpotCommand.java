package me.comu.exeter.commands.economy;

import me.comu.exeter.core.Core;
import me.comu.exeter.handlers.EcoJSONHandler;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JackpotCommand implements ICommand {

    private static final HashMap<String, Integer> jackpot = new HashMap<>();
    private ScheduledFuture<?> jackpotExecutor;
    private final ScheduledExecutorService anc = Executors.newScheduledThreadPool(2);
    ;
    private boolean running = false;
    private boolean autojackpot = true;
    private String textChannel;
    private String previousWinner = "No One";
    private int previousWinnerPrize = 0;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Runnable winnerThread = () -> {
            TextChannel textChannel1 = event.getJDA().getTextChannelById(textChannel);
            if (jackpot.isEmpty()) {
                if (textChannel1 == null) {
                    return;
                } else if (textChannel != null) {
                    textChannel1.sendMessage("Nobody joined the jackpot so there was no winner unfortunately :(").queue();
                }
                if (!autojackpot) {
                    running = false;
                    jackpotExecutor.cancel(true);
                }
                return;
            }
            event.getJDA().retrieveUserById(getRandomWinner(getRandomWeightedValue(jackpot.values()))).queue(user -> {
                EconomyManager.setBalance(user.getId(), EconomyManager.getBalance(user.getId()) + getTotalCash());
                previousWinner = user.getAsTag();
                previousWinnerPrize = getTotalCash();
                if (textChannel1 == null) {
                    Utility.sendPrivateMessage(event.getJDA(), user.getId(), "Hey! You won **$" + getTotalCash() + "** in the jackpot! That's amazing because you only wagered **$" + jackpot.get(user.getId()) + "** meaning you had a " + (double) jackpot.get(user.getId()) / (double) getTotalCash() * 100 + "% chance of winning. **Congrats!**");
                    jackpot.clear();
                } else {
                    textChannel1.sendMessage(EmbedUtils.embedMessage(user.getAsMention() + " won **$" + getTotalCash() + "** in the jackpot with **$" + jackpot.get(user.getId()) + " (" + Utility.round((double) jackpot.get(user.getId()) / (double) getTotalCash() * 100, 2) + "%)**").setTitle("$" + getTotalCash() + " Jackpot Finished!").build()).queue();
                    jackpot.clear();
                }
            });
            if (!autojackpot) {
                running = false;
                jackpotExecutor.cancel(true);
            }
        };

        Member member = event.getMember();
        if (EconomyManager.verifyUser(Objects.requireNonNull(member).getUser().getId())) {
            EconomyManager.getUsers().put(member.getUser().getId(), 0);
        }
        if (!running && args.isEmpty()) {
            event.getChannel().sendMessage("The jackpot is currently not running, please tell an admin to start it!").queue();
            textChannel = event.getChannel().getId();
            return;
        }
        if (!args.isEmpty() && (args.get(0).equalsIgnoreCase("start")) && (member.hasPermission(Permission.ADMINISTRATOR) || member.getIdLong() == Core.OWNERID)) {
            if (running) {
                event.getChannel().sendMessage("The jackpot is already running!").queue();
                return;
            }
            if (autojackpot) {
                jackpotExecutor = anc.scheduleAtFixedRate(winnerThread, 10, 10, TimeUnit.SECONDS);
                running = true;
                event.getChannel().sendMessage("Started the jackpot! **" + Core.PREFIX + "jackpot info** for more information!").queue();
                textChannel = event.getChannel().getId();
                return;
            } else {
                jackpotExecutor = anc.schedule(winnerThread, 1, TimeUnit.HOURS);
                running = true;
                event.getChannel().sendMessage("Started the jackpot! **" + Core.PREFIX + "jackpot info** for more information!").queue();
                textChannel = event.getChannel().getId();
                return;
            }
        }
        if (!args.isEmpty() && (args.get(0).equalsIgnoreCase("stop")) && (member.hasPermission(Permission.ADMINISTRATOR) || member.getIdLong() == Core.OWNERID)) {
            if (running) {
                jackpotExecutor.cancel(true);
                running = false;
                event.getChannel().sendMessage("Stopped the jackpot!").queue();
                return;
            } else {
                event.getChannel().sendMessage("The jackpot isn't running").queue();
                return;
            }
        }
        if (!args.isEmpty() && args.get(0).equalsIgnoreCase("auto") && (member.hasPermission(Permission.ADMINISTRATOR) || member.getIdLong() == Core.OWNERID)) {
            if (autojackpot) {
                autojackpot = false;
                event.getChannel().sendMessage("Jackpots will no longer be started automatically and will need an admin to start them manually.").queue();
            } else {
                autojackpot = true;
                event.getChannel().sendMessage("Jackpots will now be started automatically").queue();
            }
            return;
        }
        if (!args.isEmpty() && (args.get(0).equalsIgnoreCase("clear") || args.get(0).equalsIgnoreCase("reset")) && (member.hasPermission(Permission.ADMINISTRATOR) || member.getIdLong() == Core.OWNERID)) {
            event.getChannel().sendMessage("Successfully cleared a `" + jackpot.size() + "` player jackpot worth `$" + getTotalCash() + "`").queue();
            jackpot.clear();
            return;
        }
        if (!running && !args.isEmpty() && args.get(0).equalsIgnoreCase("info")) {
            event.getChannel().sendMessage("There is currently no on-going jackpot.\nPrevious Winner: " + previousWinner + "\nPrevious Prize: $" + previousWinnerPrize).queue();
            return;
        } else if (!running) {
            event.getChannel().sendMessage("The jackpot is currently not running, please tell an admin to start it!").queue();
            return;
        }
        if (args.isEmpty()) {
            event.getChannel().sendMessage("There are currently `" + jackpot.size() + "` users in the jackpot with a total cash prize of `$" + getTotalCash() + "`\n**" + Core.PREFIX + "jackpot info** to see more details").queue();
            textChannel = event.getChannel().getId();
            return;
        }
        if (args.get(0).equalsIgnoreCase("info")) {
            StringBuilder stringBuilder = new StringBuilder("`The jackpot picks its winner in " + jackpotExecutor.getDelay(TimeUnit.SECONDS) / 3600 + " hours " + (jackpotExecutor.getDelay(TimeUnit.SECONDS) % 3600) / 60 + " minutes " + jackpotExecutor.getDelay(TimeUnit.SECONDS) % 60 + " seconds\n" + jackpot.size() + " users in the jackpot!\nPrevious Winner: " + previousWinner + "\nPrevious Prize: $" + previousWinnerPrize + "`\n");
            for (Map.Entry<String, Integer> entry : jackpot.entrySet()) {
                event.getJDA().retrieveUserById(entry.getKey()).queue(user ->
                        stringBuilder.append("**").append(user.getAsTag()).append("** - $").append(jackpot.get(user.getId())).append(" (").append((getTotalCash() == 0 ? "100" : Utility.round((double) jackpot.get(user.getId()) / (double) getTotalCash() * 100, 2))).append("%)\n"));
            }
            event.getChannel().sendMessage(EmbedUtils.embedMessage(stringBuilder.toString()).setTitle("$" + getTotalCash() + " Jackpot").build()).queue();
            textChannel = event.getChannel().getId();
            return;
        }
        try {
            int amount = Integer.parseInt(args.get(0));
            if (amount < 1) {
                event.getChannel().sendMessage("You can't add negative credits to the jackpot!").queue();
                return;
            }
            if (EconomyManager.getBalance(member.getUser().getId()) < amount) {
                event.getChannel().sendMessage("You don't have **$" + amount + "** credits to add to the jackpot!").queue();
            } else {
                if (jackpot.containsKey(member.getId())) {
                    int preCash = jackpot.get(member.getId());
                    event.getChannel().sendMessage("**" + member.getUser().getAsTag() + "** has added **$" + amount + "** to the jackpot, totaling **$" + getTotalCash() + "**").queue();
                    EconomyManager.setBalance(member.getId(), EconomyManager.getBalance(member.getId()) - amount);
                    jackpot.replace(member.getId(), preCash + amount);
                    EcoJSONHandler.saveEconomyConfig();
                } else {
                    event.getChannel().sendMessage("**" + member.getUser().getAsTag().replaceAll("([_`~*>])", "\\\\$1") + "** has entered the jackpot with **$" + amount + "** (" + (getTotalCash() == 0 ? "100" : Utility.round((double) amount / (double) getTotalCash() * 100, 2)) + "% of the total cash-pool)").queue();
                    EconomyManager.setBalance(member.getId(), EconomyManager.getBalance(member.getId()) - amount);
                    jackpot.put(member.getId(), amount);
                    EcoJSONHandler.saveEconomyConfig();
                }
            }
        } catch (Exception ex) {
            event.getChannel().sendMessage("That number is either invalid or too large").queue();

        }
        textChannel = event.getChannel().getId();
        EcoJSONHandler.saveEconomyConfig();
    }

    private int getTotalCash() {
        int cash = 0;
        for (int money : jackpot.values())
            cash += money;
        return cash;
    }

    private String getRandomWinner(int value) {
        Set<String> keysByValue = Utility.getKeysByValue(jackpot, value);
        if (keysByValue.size() > 1) {
            return (String) keysByValue.toArray()[new Random().nextInt(keysByValue.size()-1)];
        } else {
            return Utility.getKeyByValue(jackpot, value);
        }
    }

    private int getRandomWeightedValue(Collection<Integer> collection) {
        double completeWeight = 0.0;
        for (int item : collection)
            completeWeight += item;
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (int item : collection) {
            countWeight += item;
            if (countWeight >= r)
                return item;
        }
        throw new RuntimeException("idk");
    }
    @Override
    public String getHelp() {
        return "See the current jackpot balance and users enrolled in the jackpot as well as put money into it\n" + "`" + Core.PREFIX + getInvoke() + " [info]/[amount][start/reset/stop]/[auto]`\nAliases: `" + Arrays.deepToString(getAlias()) + "`";
    }

    @Override
    public String getInvoke() {
        return "jackpot";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"jp"};
    }

    @Override
    public Category getCategory() {
        return Category.ECONOMY;
    }
}
