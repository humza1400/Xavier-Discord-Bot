package me.comu.exeter.musicplayer;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioHandler implements AudioSendHandler, AudioReceiveHandler {

    private final Queue<byte[]> queue = new ConcurrentLinkedQueue<>();

    @Override
    public boolean canReceiveCombined() {
        return queue.size() < 10;
    }

    @Override
    public void handleCombinedAudio(@Nonnull CombinedAudio combinedAudio) {
        if (combinedAudio.getUsers().isEmpty())
            return;
        byte[] data = combinedAudio.getAudioData(1.0f);
        queue.add(data);
    }



    @Override
    public boolean canProvide() {
        return !queue.isEmpty();
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        byte[] data = queue.poll();
        return data == null ? null : ByteBuffer.wrap(data);
    }

    @Override
    public boolean isOpus() {
        return false;
    }
}
