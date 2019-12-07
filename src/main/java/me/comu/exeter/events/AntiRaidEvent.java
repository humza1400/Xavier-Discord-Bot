package me.comu.exeter.events;

import me.comu.exeter.commands.AntiRaidCommand;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;

public class AntiRaidEvent extends ListenerAdapter {

//    @Override
//    public void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {
//        boolean active = AntiRaidCommand.isActive();
//        if (active && !event.getChannel().getTimeCreated().isEqual(OffsetDateTime.now())) {
//            event.getChannel().delete().complete();
//        }
//    }
//
//    @Override
//    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
//        boolean active = AntiRaidCommand.isActive();
//        if (active && event.getChannel().getTimeCreated().isBefore(OffsetDateTime.now().minusMinutes(10)))
//            event.getChannel().createCopy().complete();
//    }
//
//    @Override
//    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
//        boolean active = AntiRaidCommand.isActive();
//        if (active && !event.getChannel().getTimeCreated().isEqual(OffsetDateTime.now())) {
//            event.getChannel().delete().complete();
//        }
//    }
//
//    @Override
//    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
//        boolean active = AntiRaidCommand.isActive();
//        if (active && event.getChannel().getTimeCreated().isBefore(OffsetDateTime.now().minusMinutes(10)))
//            event.getChannel().createCopy().complete();
//    }
//
//    @Override
//    public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
//        boolean active = AntiRaidCommand.isActive();
//        if (active && !event.getCategory().getTimeCreated().isEqual(OffsetDateTime.now())) {
//            event.getCategory().delete().complete();
//        }
//    }
//
//    @Override
//    public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
//        boolean active = AntiRaidCommand.isActive();
//        if (active && event.getCategory().getTimeCreated().isBefore(OffsetDateTime.now().minusMinutes(10)))
//            event.getCategory().createCopy().complete();
//    }
}
