package me.comu.exeter.pagination.method;

import me.comu.exeter.pagination.exception.EmptyPageCollectionException;
import me.comu.exeter.pagination.exception.InvalidStateException;
import me.comu.exeter.pagination.listener.MessageHandler;
import me.comu.exeter.pagination.model.Page;
import me.comu.exeter.pagination.type.PageType;
import me.comu.exeter.util.EmojiUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.PermissionException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.comu.exeter.pagination.type.Emote.*;

public class Pages {
    public static JDA api;
    public static MessageHandler handler = new MessageHandler();

    public static void activate(JDA api) {
        Pages.api = api;
        api.addEventListener(handler);
    }


    public static void paginate(Message msg, List<Page> pages, boolean shouldCancel, int time, TimeUnit unit) throws ErrorResponseException, PermissionException {
        if (api == null) throw new InvalidStateException();

        msg.addReaction(PREVIOUS.getCode()).queue();
        if (shouldCancel) msg.addReaction(CANCEL.getCode()).queue();
        msg.addReaction(NEXT.getCode()).queue();

        handler.addEvent((msg.getChannelType().isGuild() ? msg.getGuild().getId() : msg.getPrivateChannel().getUser().getId()) + msg.getId(), new Consumer<>() {
            private final int maxP = pages.size() - 1;
            private int p = 0;
            private Future<?> timeout;
            private final Consumer<Void> success = s -> handler.removeEvent(msg);

            @Override
            public void accept(MessageReactionAddEvent event) {
                if (timeout == null)
                    try {
                        timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                    } catch (PermissionException ignore) {
                    }
                if (Objects.requireNonNull(event.getUser()).isBot() || !event.getMessageId().equals(msg.getId()))
                    return;

                if (timeout != null) timeout.cancel(true);
                try {
                    timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                } catch (PermissionException ignore) {
                }
                if (event.getReactionEmote().getName().equals(PREVIOUS.getCode())) {
                    if (p > 0) {
                        p--;
                        Page pg = pages.get(p);

                        updatePage(msg, pg);
                    }
                } else if (event.getReactionEmote().getName().equals(NEXT.getCode())) {
                    if (p < maxP) {
                        p++;
                        Page pg = pages.get(p);

                        updatePage(msg, pg);
                    }
                } else if (shouldCancel && event.getReactionEmote().getName().equals(CANCEL.getCode())) {
                    try {
                        msg.clearReactions().queue(success, s -> {
                            msg.getReactions().forEach(r -> r.removeReaction().queue());
                            success.accept(null);
                        });
                    } catch (PermissionException e) {
                        msg.getReactions().forEach(r -> r.removeReaction().queue());
                        success.accept(null);
                    }
                }
                try {
                    event.getReaction().removeReaction(event.getUser()).complete();
                } catch (PermissionException | ErrorResponseException ignore) {
                }
            }
        });
    }


    public static void paginate(Message msg, List<Page> pages, int time, TimeUnit unit, int skipAmount) throws ErrorResponseException, PermissionException {
        if (api == null) throw new InvalidStateException();

        msg.addReaction(SKIP_BACKWARD.getCode()).queue();
        msg.addReaction(PREVIOUS.getCode()).queue();
        msg.addReaction(CANCEL.getCode()).queue();
        msg.addReaction(NEXT.getCode()).queue();
        msg.addReaction(SKIP_FORWARD.getCode()).queue();

        handler.addEvent((msg.getChannelType().isGuild() ? msg.getGuild().getId() : msg.getPrivateChannel().getUser().getId()) + msg.getId(), new Consumer<>() {
            private final int maxP = pages.size() - 1;
            private int p = 0;
            private Future<?> timeout;
            private final Consumer<Void> success = s -> handler.removeEvent(msg);

            @Override
            public void accept(MessageReactionAddEvent event) {
                if (timeout == null)
                    try {
                        timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                    } catch (PermissionException ignore) {
                    }
                if (Objects.requireNonNull(event.getUser()).isBot() || !event.getMessageId().equals(msg.getId()))
                    return;

                if (timeout != null) timeout.cancel(true);
                try {
                    timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                } catch (PermissionException ignore) {
                }
                if (event.getReactionEmote().getName().equals(PREVIOUS.getCode())) {
                    if (p > 0) {
                        p--;
                        Page pg = pages.get(p);

                        updatePage(msg, pg);
                    }
                } else if (event.getReactionEmote().getName().equals(NEXT.getCode())) {
                    if (p < maxP) {
                        p++;
                        Page pg = pages.get(p);

                        updatePage(msg, pg);
                    }
                } else if (event.getReactionEmote().getName().equals(SKIP_BACKWARD.getCode())) {
                    if (p > 0) {
                        p -= (p - skipAmount < 0 ? p : skipAmount);
                        Page pg = pages.get(p);

                        updatePage(msg, pg);
                    }
                } else if (event.getReactionEmote().getName().equals(SKIP_FORWARD.getCode())) {
                    if (p < maxP) {
                        p += (p + skipAmount > maxP ? maxP - p : skipAmount);
                        Page pg = pages.get(p);

                        updatePage(msg, pg);
                    }
                } else if (event.getReactionEmote().getName().equals(CANCEL.getCode())) {
                    try {
                        msg.clearReactions().queue(success, s -> {
                            msg.getReactions().forEach(r -> r.removeReaction().queue());
                            success.accept(null);
                        });
                    } catch (PermissionException e) {
                        msg.getReactions().forEach(r -> r.removeReaction().queue());
                        success.accept(null);
                    }
                }
                try {
                    event.getReaction().removeReaction(event.getUser()).complete();
                } catch (PermissionException | ErrorResponseException ignore) {
                }
            }
        });
    }


    public static void categorize(Message msg, Map<String, Page> categories, int time, TimeUnit unit) throws ErrorResponseException, PermissionException {
        if (api == null) throw new InvalidStateException();

        categories.keySet().forEach(k -> {
            if (EmojiUtils.containsEmoji(k)) msg.addReaction(k).queue(null, Pages::doNothing);
            else msg.addReaction(Objects.requireNonNull(api.getEmoteById(k))).queue(null, Pages::doNothing);
        });
        msg.addReaction(CANCEL.getCode()).queue(null, Pages::doNothing);
        handler.addEvent((msg.getChannelType().isGuild() ? msg.getGuild().getId() : msg.getPrivateChannel().getUser().getId()) + msg.getId(), new Consumer<>() {
            private String currCat = "";
            private Future<?> timeout;
            private final Consumer<Void> success = s -> handler.removeEvent(msg);

            @Override
            public void accept(@Nonnull MessageReactionAddEvent event) {
                if (timeout == null)
                    try {
                        timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                    } catch (PermissionException ignore) {
                    }

                if (Objects.requireNonNull(event.getUser()).isBot() || event.getReactionEmote().getName().equals(currCat) || !event.getMessageId().equals(msg.getId()))
                    return;
                else if (event.getReactionEmote().getName().equals(CANCEL.getCode())) {
                    try {
                        msg.clearReactions().queue(success, s -> {
                            msg.getReactions().forEach(r -> r.removeReaction().queue());
                            success.accept(null);
                        });
                    } catch (PermissionException e) {
                        msg.getReactions().forEach(r -> r.removeReaction().queue());
                        success.accept(null);
                    }
                    return;
                }

                if (timeout != null) timeout.cancel(true);
                try {
                    timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                } catch (PermissionException ignore) {
                }

                Page pg = categories.get(event.getReactionEmote().isEmoji() ? event.getReactionEmote().getName() : event.getReactionEmote().getId());

                currCat = updateCategory(event, msg, pg);
                try {
                    event.getReaction().removeReaction(event.getUser()).complete();
                } catch (PermissionException | ErrorResponseException ignore) {
                }
            }
        });
    }


    public static void buttonize(Message msg, Map<String, BiConsumer<Member, Message>> buttons, boolean showCancelButton) throws ErrorResponseException, PermissionException {
        if (api == null) throw new InvalidStateException();

        buttons.keySet().forEach(k -> {
            if (EmojiUtils.containsEmoji(k)) msg.addReaction(k).queue(null, Pages::doNothing);
            else msg.addReaction(Objects.requireNonNull(api.getEmoteById(k))).queue(null, Pages::doNothing);
        });
        if (!buttons.containsKey(CANCEL.getCode()) && showCancelButton)
            msg.addReaction(CANCEL.getCode()).queue(null, Pages::doNothing);
        handler.addEvent((msg.getChannelType().isGuild() ? msg.getGuild().getId() : msg.getPrivateChannel().getUser().getId()) + msg.getId(), new Consumer<>() {
            private final Consumer<Void> success = s -> handler.removeEvent(msg);

            @Override
            public void accept(@Nonnull MessageReactionAddEvent event) {
                if (Objects.requireNonNull(event.getUser()).isBot() || !event.getMessageId().equals(msg.getId()))
                    return;

                try {
                    if (event.getReactionEmote().isEmoji())
                        buttons.get(event.getReactionEmote().getName()).accept(event.getMember(), msg);
                    else buttons.get(event.getReactionEmote().getId()).accept(event.getMember(), msg);
                } catch (NullPointerException ignore) {
                }

                if ((!buttons.containsKey(CANCEL.getCode()) && showCancelButton) && event.getReactionEmote().getName().equals(CANCEL.getCode())) {
                    try {
                        msg.clearReactions().queue(success, s -> {
                            msg.getReactions().forEach(r -> r.removeReaction().queue());
                            success.accept(null);
                        });
                    } catch (PermissionException e) {
                        msg.getReactions().forEach(r -> r.removeReaction().queue());
                        success.accept(null);
                    }
                }

                try {
                    event.getReaction().removeReaction(event.getUser()).complete();
                } catch (PermissionException | ErrorResponseException ignore) {
                }
            }
        });
    }


    public static void buttonize(Message msg, Map<String, BiConsumer<Member, Message>> buttons, boolean showCancelButton, int time, TimeUnit unit) throws ErrorResponseException, PermissionException {
        if (api == null) throw new InvalidStateException();

        buttons.keySet().forEach(k -> {
            if (EmojiUtils.containsEmoji(k)) msg.addReaction(k).queue(null, Pages::doNothing);
            else msg.addReaction(Objects.requireNonNull(api.getEmoteById(k))).queue(null, Pages::doNothing);
        });
        if (!buttons.containsKey(CANCEL.getCode()) && showCancelButton)
            msg.addReaction(CANCEL.getCode()).queue(null, Pages::doNothing);
        handler.addEvent((msg.getChannelType().isGuild() ? msg.getGuild().getId() : msg.getPrivateChannel().getUser().getId()) + msg.getId(), new Consumer<>() {
            private Future<?> timeout;
            private final Consumer<Void> success = s -> handler.removeEvent(msg);

            @Override
            public void accept(@Nonnull MessageReactionAddEvent event) {
                if (timeout == null)
                    try {
                        timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                    } catch (PermissionException ignore) {
                    }

                if (Objects.requireNonNull(event.getUser()).isBot() || !event.getMessageId().equals(msg.getId()))
                    return;

                try {
                    if (event.getReactionEmote().isEmoji())
                        buttons.get(event.getReactionEmote().getName()).accept(event.getMember(), msg);
                    else buttons.get(event.getReactionEmote().getId()).accept(event.getMember(), msg);
                } catch (NullPointerException ignore) {

                }

                if ((!buttons.containsKey(CANCEL.getCode()) && showCancelButton) && event.getReactionEmote().getName().equals(CANCEL.getCode())) {
                    try {
                        msg.clearReactions().queue(success, s -> {
                            msg.getReactions().forEach(r -> r.removeReaction().queue());
                            success.accept(null);
                        });
                    } catch (PermissionException e) {
                        msg.getReactions().forEach(r -> r.removeReaction().queue());
                        success.accept(null);
                    }
                }


                if (timeout != null) timeout.cancel(true);
                try {
                    timeout = msg.clearReactions().queueAfter(time, unit, success, Pages::doNothing);
                } catch (PermissionException ignore) {
                }
                try {
                    event.getReaction().removeReaction(event.getUser()).complete();
                } catch (PermissionException | ErrorResponseException ignore) {
                }
            }
        });
    }

    private static void updatePage(Message msg, Page p) {
        if (p == null) throw new EmptyPageCollectionException();
        if (p.getType() == PageType.TEXT) {
            msg.editMessage((Message) p.getContent()).queue(null, Pages::doNothing);
        } else {
            msg.editMessage((MessageEmbed) p.getContent()).queue(null, Pages::doNothing);
        }
    }

    private static String updateCategory(GenericMessageReactionEvent event, Message msg, Page p) {
        AtomicReference<String> out = new AtomicReference<>("");
        if (p == null) throw new EmptyPageCollectionException();

        if (p.getType() == PageType.TEXT) {
            msg.editMessage((Message) p.getContent()).queue(s -> out.set(event.getReactionEmote().getName()));
        } else {
            msg.editMessage((MessageEmbed) p.getContent()).queue(s -> out.set(event.getReactionEmote().getName()));
        }

        return out.get();
    }

    private static void doNothing(Throwable t) {
        try {
            throw t;
        } catch (Throwable ignore) {
        }
    }
}