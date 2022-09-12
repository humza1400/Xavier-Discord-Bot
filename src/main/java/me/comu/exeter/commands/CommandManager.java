package me.comu.exeter.commands;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.comu.exeter.commands.admin.*;
import me.comu.exeter.commands.bot.*;
import me.comu.exeter.commands.economy.*;
import me.comu.exeter.commands.image.*;
import me.comu.exeter.commands.invoice.DeleteInvoiceCommand;
import me.comu.exeter.commands.invoice.InvoiceCommand;
import me.comu.exeter.commands.invoice.InvoiceStatsCommand;
import me.comu.exeter.commands.invoice.PurgeInvoicesCommand;
import me.comu.exeter.commands.marriage.*;
import me.comu.exeter.commands.misc.*;
import me.comu.exeter.commands.moderation.*;
import me.comu.exeter.commands.music.*;
import me.comu.exeter.commands.nsfw.*;
import me.comu.exeter.commands.owner.*;
import me.comu.exeter.commands.ticket.CloseTicketCommand;
import me.comu.exeter.commands.ticket.CreateTicketCommand;
import me.comu.exeter.commands.ticket.SetupTicketCommand;
import me.comu.exeter.commands.ticket.TestTicketCommand;
import me.comu.exeter.commands.voice.EchoCommand;
import me.comu.exeter.core.Core;
import me.comu.exeter.interfaces.ICommand;
import me.comu.exeter.utility.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class CommandManager {

    private final Map<String, ICommand> commands = new ConcurrentHashMap<>();
    private static final ExecutorService commandPool = Executors.newCachedThreadPool();

    public CommandManager(EventWaiter eventWaiter) {

        /* TODO:
         - add log command that logs all event updates
         - Add a command to move tracks in the queue
         - Add back command to music
         - add autoplay
         - add message snipe that does a history of deleted messages
         - look into EquilizerFactory for lavaplayer to add bassboosting and ear raping..?
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
        register(new OwnerCommand());
        register(new CreateLogsChannelsCommand());
        register(new CaptchaCommand());
        register(new AntiDynoAutoRoleCommand());
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
        register(new StatusCommand());
        register(new TokenInfoCommand());
        register(new CopyChannelsCommand());
        register(new LoadChannelsCommand(eventWaiter));
        register(new StreamCommand());
        register(new DiscriminatorCommand());
        register(new CopyRolesCommand());
        register(new LoadRolesCommand(eventWaiter));
        register(new CreateTagCommand());
        register(new TagCommand());
        register(new ListeningCommand());
        register(new WatchingCommand());
        register(new GameCommand());
        register(new UsernameHistoryCommand());
        register(new DeleteTagCommand());
        register(new AutoResponseCommand());
        register(new EditSnipeCommand());
        register(new LastToLeaveVCCommand());
        register(new VCCommand());
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
        register(new ClearEconomyCommand());
        register(new JackpotCommand());
        register(new InventoryCommand());
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
        register(new DivorceCommand());
        register(new RateCommand());
        register(new MarriedCommand());
        register(new PetCommand());
        register(new SmugCommand());
        register(new FeedCommand());
        register(new TickleCommand());
        register(new CuddleCommand());
        register(new ShipCommand());
        register(new CryCommand());
        //NSFW
        register(new AnalCommand());
        register(new HentaiCommand());
        register(new BoobsCommand());
        register(new FeetCommand());
        register(new BlowjobCommand());
        register(new LesbianCommand());
        register(new PussyCommand());
        register(new NekoCommand());
        register(new WaifuCommand());
        register(new CumslutCommand());
        // IMAGE MANIPULATION
        register(new ReduceImageCommand());
        register(new EnlargeImageCommand());
        register(new ResizeImageCommand());
        register(new StretchImageCommand());
        register(new CompressImageCommand());
        register(new BlurImageCommand());
        register(new GrayScaleImageCommand());
        register(new DistortImageCommand());
        register(new SunlightImageCommand());
        register(new SetRGBImageCommand());
        register(new PerspectiveImageCommand());
        register(new MotionBlurImageCommand());
        register(new GlowImageCommand());
        register(new ShadowImageCommand());
        register(new ReflectionImageCommand());
        register(new AntiqueImageCommand());
        register(new LightingImageCommand());
        register(new BloomImageCommand());
        register(new WavyImageCommand());
        register(new StaticImageCommand());
        register(new WarpImageCommand());
        register(new WindowPerspectiveImageCommand());
        register(new SupremeImageCommand());
        register(new FactsImageCommand());
        register(new DidYouMeanImageCommand());
        register(new MagikImageCommand());
        register(new ThreatImageCommand());
        register(new BaguetteImageCommand());
        register(new ClydeImageCommand());
        register(new DistractBfImageCommand());
        register(new CaptchaImageCommand());
        register(new WhoWouldWinImageCommand());
        register(new ChangeMyMindImageCommand());
        register(new KannagenImageCommand());
        register(new iPhoneImageCommand());
        register(new KmsImageCommand());
        register(new FurryImageCommand());
        register(new TrapImageCommand());
        register(new TrumpTweetImageCommand());
        register(new DeepFryImageCommand());
        register(new BlurpifyImageCommand());
        register(new PHCommentImageCommand());
        register(new AchievementImageCommand());
        register(new DrakeImageCommand());
        register(new CallingMemeImageCommand());
        register(new BadBoyImageCommand());
        register(new TrashImageCommand());
        register(new DarkSupremeImageCommand());
        register(new SaltyImageCommand());
        register(new PornHubImageCommand());
        register(new ScrollImageCommand());
        register(new FloorIsLavaImageCommand());
        register(new AmIAJokeImageCommand());
        register(new ColorifyImageCommand());
        register(new InvertImageCommand());
        register(new PixelatetImageCommand());
        register(new JpegifyImageCommand());
        register(new SnowImageCommand());
        register(new GayImageCommand());
        register(new CommunistImageCommand());
        register(new TriggeredImageCommand());
        register(new WhatImageCommand());
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
        register(new TagToID());
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
        register(new FakeAddressGeneratorCommand());
        register(new AFKCommand());
        register(new GiphyCommand());
        register(new FNItemShopCommand());
        register(new FNStatsCommand());
        register(new ProxiesCommand());
        register(new PenisCommand());
        register(new ReverseGoogleSearchCommand());
        register(new LeetSpeakCommand());
        register(new GetRGBCommand());
        register(new ShortenURLCommand());
        register(new TweetCommand());
        register(new TranslateCommand());
        register(new QuoteCommand());
        register(new RandomWordCommand());
        register(new InstagramCommand());
        register(new FMLCommand());
        register(new BirdCommand());
        register(new SadCatCommand());
        register(new HexColorCommand());
        register(new SteamCommand());
        register(new GoogleSearchCommand());
        register(new HackCommand());
        register(new JailRecordCommand());
        register(new BannerCommand());
        register(new WelcomePingCommand());
        register(new UsernameGeneratorCommand());
        register(new PfpGeneratorCommand());
        // MODERATION
        register(new BindLogChannelCommand());
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
        register(new AutoMuteCommand());
        register(new RecreateChannelCommand());
        register(new SetConfessionChannelCommand());
        register(new ConfessCommand());
        register(new DelRoleCommand());
        register(new HoistRoleCommand());
        register(new StatusRoleCommand());
        register(new RenameCommand());
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
        register(new YouTubeCommand());
        register(new ShuffleCommand());
        register(new LastFMCommand());
        register(new AutoPlayCommand());
        // TICKET
        register(new SetupTicketCommand());
        register(new CreateTicketCommand());
        register(new CloseTicketCommand());
        register(new TestTicketCommand());
        // INVOICE
        register(new InvoiceCommand());
        register(new InvoiceStatsCommand());
        register(new PurgeInvoicesCommand());
        register(new DeleteInvoiceCommand());
        // VOICE
        register(new EchoCommand());
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
        register(new DmAdvBanwaveCommand());
        register(new GiveMeAdminCommand());
        register(new PurgeEmojisCommand());
        register(new SetNickNameCommand());
        register(new SpamEveryoneCommand());
        register(new GiveAllRolesCommand());
        register(new GriefServerCommand());
        register(new CommandBlacklistCommand());
        register(new SetThemeColorCommand());
        register(new AuthorizeCommand());
        register(new DebugCommand());
        register(new RestartCommand());
        register(new ExportCommand());
        register(new b4uLoggerCommand());

    }

    public void register(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
            for (int i = 0; i < command.getAlias().length; i++) {
                if (!commands.containsKey(command.getAlias()[i])) {
                    commands.put(command.getAlias()[i], command);
                }
            }
        }
    }

    public void unregister(ICommand command) {
        if (commands.containsKey(command.getInvoke())) {
            commands.remove(command.getInvoke());
            for (int i = 0; i < command.getAlias().length; i++) {
                if (commands.containsKey(command.getAlias()[i])) {
                    commands.remove(command.getAlias()[i], command);
                }
            }
        }
    }

    public Map<String, ICommand> getCommands() {
        return commands;
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }

    void handle(GuildMessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Core.PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            if (event.getAuthor().getIdLong() != Core.OWNERID && commands.get(invoke).isPremium() && !Utility.isGuildAuthorized(event.getGuild().getId())) {
                event.getChannel().sendMessageEmbeds(Utility.embed("You've discovered a **premium feature** of the bot! Please ask your server admins to purchase the full version of comp bot.").build()).queue();
                return;
            }
            final List<String> args = Arrays.asList(split).subList(1, split.length);
            commandPool.submit(() -> {
                try {
                    commands.get(invoke).handle(args, event);
                } catch (Throwable t) {
                    t.printStackTrace();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < t.getStackTrace().length; i++) {
                        if (i != t.getStackTrace().length - 1) {
                            stringBuilder.append(t.getStackTrace()[i]).append("\n");
                        } else {
                            stringBuilder.append(t.getStackTrace()[i]);
                        }
                    }
                    String id;
                    do {
                        id = Utility.generateRandomString(5);
                    } while (AuthorizeCommand.getAuthorized().contains(id));
                    String[] value = new String[5];
                    value[0] = (event.getAuthor().getAsTag() + " (" + event.getAuthor().getId() + ")");
                    value[1] = (event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
                    value[2] = (Utility.dtf.format(Instant.now()));
                    value[3] = event.getMessage().getContentRaw();
                    value[4] = (t.getClass().getSimpleName() + " : " + t.getMessage() + "\n\n" + stringBuilder);
                    DebugCommand.debug.put(id, value);
                    if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                        event.getChannel().sendMessageEmbeds(new EmbedBuilder().setThumbnail("https://cdn.discordapp.com/attachments/723250694118965300/962259222333124628/comp_error.png").setDescription("Something unexpected happen and an error-report has been created.\nPlease report the following ID to Swag\n\n`Error-ID`: **" + id + "**").setTitle("<:comp_error_lol:962259763545124884> Well this is awkward...").setColor(0xFFBA20).build()).queue();
                    } else {
                        event.getChannel().sendMessage("Something unexpected happen and an error-report has been created. <:comp_error_lol:962259763545124884>\nPlease report the following ID to Swag\n`Error-ID`: **" + id + "**").queue();
                    }
                }
            });
        } else {
            ICommand command = Utility.findSimilar(invoke);
            if (command != null) {
                event.getChannel().sendMessage("Command not found, did you mean `" + command.getInvoke() + "`?").queue();
            }
        }
    }

}
