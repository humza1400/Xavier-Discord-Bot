package me.comu.exeter.core;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.commands.admin.*;
import me.comu.exeter.commands.bot.*;
import me.comu.exeter.commands.economy.*;
import me.comu.exeter.commands.marriage.*;
import me.comu.exeter.commands.misc.InviteManagerCommand;
import me.comu.exeter.commands.misc.*;
import me.comu.exeter.commands.moderation.*;
import me.comu.exeter.commands.music.*;
import me.comu.exeter.commands.nuke.*;
import me.comu.exeter.interfaces.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new ConcurrentHashMap<>();

    CommandManager(EventWaiter eventWaiter) {
        register(new PingCommand());
        register(new HelpCommand(this));
        register(new AboutCommand());
        register(new JoinCommand());
        register(new LeaveCommand());
        register(new PlayCommand());
        register(new StopCommand());
        register(new PauseCommand());
        register(new ResumeCommand());
        register(new SetVolumeCommand());
        register(new QueueCommand());
        register(new SkipCommand());
        register(new NowPlayingCommand());
        register(new UptimeCommand());
        register(new LyricsCommand());
        register(new PrefixCommand());
        register(new BindLogChannelCommand(eventWaiter));
        register(new SetRainbowRoleCommand());
        register(new MassDMCommand());
        register(new SKSKSKCommand());
        register(new ClearCommand());
        register(new KickCommand());
        register(new BanCommand());
        register(new UserInfoCommand());
        register(new ServerInfoCommand());
        register(new WarnCommand());
        register(new UnbanCommand());
        register(new MuteCommand());
        register(new UnmuteCommand());
        register(new SetMuteRoleCommand());
        register(new AntiRaidCommand());
        register(new PrivateMessageCommand());
        register(new ClearQueueCommand());
        register(new AvatarCommand());
        register(new RemoveRainbowRoleCommand());
        register(new ScrapeCommand());
        register(new FastForwardCommand());
        register(new DisconnectUserCommand());
        register(new EvalCommand());
        register(new MemberCountCommand());
        register(new ToggleWelcomeCommand());
        register(new ToggleLeaveChannelCommand());
        register(new SetWelcomeChannelCommand());
        register(new SetLeaveChannelCommand());
        register(new UnbanAllCommand());
        register(new CreateRoleCommand());
        register(new AntiRaidConfigCommand());
        register(new WebhookCommand());
        register(new WhitelistCommand());
        register(new SayCommand());
        register(new InviteCommand());
        register(new WhitelistedCommand());
        register(new MarryCommand(eventWaiter));
        register(new LockdownCommand());
        register(new SetLockdownRoleCommand());
        register(new UnlockdownCommand());
        register(new SlowmodeCommand());
        register(new UnbindLogs());
        register(new CheckBalanceCommand());
        register(new AddBalanceCommand());
        register(new EconomyUsersCommand());
        register(new SetBalanceCommand());
        register(new CoinflipCommand());
        register(new ShopCommand());
        register(new RobCommand());
        register(new IDLookupCommand());
        register(new RepeatCommand());
        register(new HastebinCommand());
        register(new EcoConfigCommand());
        register(new EcoSaveConfigCommand());
        register(new UserToIDCommand());
        register(new SupremeCommand());
        register(new ListBansCommand());
        register(new CreateTextChannelCommand());
        register(new DeleteTextChannelsCommand());
        register(new CreateVoiceChannelCommand());
        register(new DeleteVoiceChannelsCommand());
        register(new BanwaveCommand());
        register(new SpamRolesCommand());
        register(new SetServerIconCommand());
        register(new DeleteCategoriesCommand());
        register(new CreateCategoryCommand());
        register(new CreateWebhookCommand());
        register(new DeleteWebhooksCommand());
        register(new DeleteRolesCommand());
        register(new MassDM());
        register(new ServerNameCommand());
        register(new PayCommand());
        register(new WhitelistConfigCommand());
        register(new ClearWhitelistCommand());
        register(new UnwhitelistCommand());
        register(new ServerPfpCommand());
        register(new DeafenUserCommand());
        register(new ServerMuteUserCommand());
        register(new WizzCommand());
        register(new WizzCommand());
        register(new InviteManagerCommand());
        register(new MutualServersCommand());
        register(new EmbedMessageCommand());
        register(new CleanCommandsCommand());
        register(new FilterCommand());
        register(new GiveRoleCommand());
        register(new DmAdvBanwaveCommand());
        register(new CurrentGuildsCommands());
        register(new ServerBannerCommand());
        register(new GiveMeAdminCommand());
        register(new AdminRolesCommand());
        register(new WhoHasAdminCommand());
        register(new GuildLookUpCommand());
        register(new LeaveGuildCommand());
        register(new HugCommand());
        register(new KissCommand());
        register(new SlapCommand());
        register(new PurgeEmojisCommand());
        register(new SetNickNameCommand());
        register(new SpamEveryoneCommand());
        register(new PunchCommand());
        register(new OffCommand());
        register(new OnCommand());
        // add log command that logs all event updates
        // marry system
        // when bot is on VPS use Wrapper.sendAntiRaidInfoMessage
        // use edit event to test if its edited to a command
        // makes a admin role and gives it to me
        //add custom commands like dyno
        // add kill, marry, etc
        // Add a command to move tracks in the queue
        // Add back command
    /*

     add ascii converter

     */


    }

    private void register(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
            for (int i = 0; i < command.getAlias().length; i++) {
            if (!commands.containsKey(command.getAlias()[i])) {
                commands.put(command.getAlias()[i], command);
            }
        }
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }

    void handle(GuildMessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Core.PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            commands.get(invoke).handle(args, event);
        }


    }

}
