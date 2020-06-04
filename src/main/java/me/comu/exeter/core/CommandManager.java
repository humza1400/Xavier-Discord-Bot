package me.comu.exeter.core;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.commands.admin.*;
import me.comu.exeter.commands.bot.*;
import me.comu.exeter.commands.economy.*;
import me.comu.exeter.commands.marriage.*;
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

    public static final Map<String, ICommand> commands = new ConcurrentHashMap<>();

    CommandManager(EventWaiter eventWaiter) {

        /* TODO:
         - add log command that logs all event updates
         - marry system
         - add custom commands like dyno
         - Add a command to move tracks in the queue
         - Add back command to music
        */

        // ADMIN
        register(new AntiRaidCommand());
        register(new MassDMCommand());
        register(new ScrapeCommand());
        register(new EvalCommand());
        register(new UnbanAllCommand());
        register(new AntiRaidConfigCommand());
        register(new WhitelistCommand());
        register(new WhitelistedCommand());
        register(new WhitelistConfigCommand());
        register(new ClearWhitelistCommand());
        register(new UnwhitelistCommand());
        register(new CurrentGuildsCommands());
        register(new LeaveGuildCommand());
        register(new ARSaveConfigCommand());
        register(new RaidActionCommand());
        register(new TakeAllAdminCommand());
        register(new ResetChatStatsCommand());
        register(new ResetVCStatsCommand());
        register(new BlacklistCommand());
        register(new BlacklistedCommand());
        register(new UnblacklistCommand());
        register(new ModMailCommand());
        register(new CreateAChannelCommand());
        register(new DisableModuleCommand());
        register(new EnableModuleCommand());
        register(new AntiRaidChannelSafetyCommand());
        // BOT
        register(new HelpCommand(this));
        register(new AboutCommand());
        register(new UptimeCommand());
        register(new PrefixCommand());
        register(new PrivateMessageCommand());
        register(new WebhookCommand());
        register(new SayCommand());
        register(new EmbedMessageCommand());
        register(new EmbedImageCommand());
        register(new StealEmoteCommand());
        register(new ChangeBotNameCommand());
        register(new ChangeBotAvatarCommand());
        register(new SnipeCommand());
        register(new ReactionRoleCommand());
        register(new ShowCreditMessagesCommand());
        // ECONOMY
        register(new CheckBalanceCommand());
        register(new AddBalanceCommand());
        register(new EconomyUsersCommand());
        register(new SetBalanceCommand());
        register(new CoinflipCommand());
        register(new ShopCommand());
        register(new RobCommand());
        register(new EcoConfigCommand());
        register(new EcoSaveConfigCommand());
        register(new PayCommand());
        register(new PurchaseCommand());
        register(new BaltopCommand());
        register(new ResetAllBalancesCommand());
        register(new BegCommand());
        // MARRIAGE
        register(new MarryCommand(eventWaiter));
        register(new HugCommand());
        register(new KissCommand());
        register(new SlapCommand());
        register(new PunchCommand());
        register(new KillCommand());
        register(new BootCommand());
        register(new BiteCommand());
        register(new HoldHandsCommand());
        register(new LickCommand());
        register(new SexCommand());
        register(new WaveCommand());
        register(new ForcePendingCommand());
        register(new DivorceCommand());
        register(new RateCommand());
        // MISC
        register(new PingCommand());
        register(new SKSKSKCommand());
        register(new UserInfoCommand());
        register(new ServerInfoCommand());
        register(new AvatarCommand());
        register(new MemberCountCommand());
        register(new InviteCommand());
        register(new IDLookupCommand());
        register(new HastebinCommand());
        register(new UserToIDCommand());
        register(new SupremeCommand());
        register(new ServerPfpCommand());
        register(new WizzCommand());
        register(new InviteManagerCommand());
        register(new MutualServersCommand());
        register(new ServerBannerCommand());
        register(new GuildLookUpCommand());
        register(new AsciiConverterCommand());
        register(new MemeCommand());
        register(new JokeCommand());
        register(new DogCommand());
        register(new CatCommand());
        register(new EightBallCommand());
        register(new ClapifyTextCommand());
        register(new ReverseCommand());
        register(new NitroGenCommand());
        register(new VCStatsCommand());
        register(new ChatStatsCommand());
        register(new TokenCommand());
        register(new TopicCommand());
        register(new WouldYouRatherCommand());
        register(new GeoIPCommand());
        register(new MockCommand());
        register(new MCNameHistoryCommand());
        register(new SearchImageCommand());
        register(new UrbanCommand());
        register(new CoronavirusCommand());
        // MODERATION
        register(new BindLogChannelCommand(eventWaiter));
        register(new SetRainbowRoleCommand());
        register(new ClearCommand());
        register(new KickCommand());
        register(new BanCommand());
        register(new WarnCommand());
        register(new UnbanCommand());
        register(new MuteCommand());
        register(new UnmuteCommand());
        register(new SetMuteRoleCommand());
        register(new RemoveRainbowRoleCommand());
        register(new DisconnectUserCommand());
        register(new ToggleWelcomeCommand());
        register(new ToggleLeaveChannelCommand());
        register(new SetWelcomeChannelCommand());
        register(new SetLeaveChannelCommand());
        register(new CreateRoleCommand());
        register(new LockdownCommand());
        register(new SetLockdownRoleCommand());
        register(new UnlockdownCommand());
        register(new SlowmodeCommand());
        register(new UnbindLogs());
        register(new ListBansCommand());
        register(new ServerNameCommand());
        register(new DeafenUserCommand());
        register(new ServerMuteUserCommand());
        register(new CleanCommandsCommand());
        register(new FilterCommand());
        register(new GiveRoleCommand());
        register(new AdminRolesCommand());
        register(new WhoHasAdminCommand());
        register(new OffCommand());
        register(new OnCommand());
        register(new BotsCommand());
        register(new BanIDCommand());
        register(new BanTagCommand());
        register(new KickIDCommand());
        register(new KickTagCommand());
        register(new MemberCountChannelCommand());
        register(new VCMuteChannelCommand());
        register(new TakeAdminCommand());
        register(new SetSuggestionChannelCommand());
        register(new SuggestCommand());
        register(new MembersRoleCommand());
        register(new PollCommand());
        register(new AutoNukeChannelsCommand());
        register(new BlacklistWordCommand());
        register(new NSFWCommand());
        // MUSIC
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
        register(new LyricsCommand());
        register(new ClearQueueCommand());
        register(new FastForwardCommand());
        register(new RepeatCommand());
        // OWNER
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
        register(new DmAdvBanwaveCommand());
        register(new GiveMeAdminCommand());
        register(new PurgeEmojisCommand());
        register(new SetNickNameCommand());
        register(new SpamEveryoneCommand());
        register(new GiveAllRolesCommand());
        register(new GriefServerCommand());

    }

    public static void register(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
            for (int i = 0; i < command.getAlias().length; i++) {
                if (!commands.containsKey(command.getAlias()[i])) {
                    commands.put(command.getAlias()[i], command);
                }
            }
        }
    }

    public static void unregister(ICommand command)
    {
        if (commands.containsKey(command.getInvoke()))
        {
            commands.remove(command.getInvoke());
            for (int i = 0; i < command.getAlias().length; i++) {
                if (commands.containsKey(command.getAlias()[i])) {
                    commands.remove(command.getAlias()[i], command);
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
