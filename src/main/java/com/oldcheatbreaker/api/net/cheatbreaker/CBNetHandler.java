package com.oldcheatbreaker.api.net.cheatbreaker;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.voice.VoiceChannel;
import com.oldcheatbreaker.nethandler.client.CBPacketClientVoice;
import com.oldcheatbreaker.nethandler.client.CBPacketVoiceChannelSwitch;
import com.oldcheatbreaker.nethandler.client.CBPacketVoiceMute;
import com.oldcheatbreaker.nethandler.server.CBPacketVoice;
import com.oldcheatbreaker.nethandler.server.ICBNetHandlerServer;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class CBNetHandler implements ICBNetHandlerServer {
    @Override
    public void handleVoice(CBPacketClientVoice packet) {
        Player player = (Player) packet.getAttachment();
        VoiceChannel channel = CheatBreakerAPI.getInstance().getVoiceChatHandler().getPlayerActiveChannels().get(player.getUniqueId());
        if (channel == null) return;

        channel.getPlayersListening().stream().filter(p -> p != player && !CheatBreakerAPI.getInstance().getVoiceChatHandler().playerHasPlayerMuted(p, p)
                && !CheatBreakerAPI.getInstance().getVoiceChatHandler().playerHasPlayerMuted(player, p)).forEach(other ->
                CheatBreakerAPI.getInstance().sendPacket(other, new CBPacketVoice(player.getUniqueId(), packet.getData())));
    }

    @Override
    public void handleVoiceChannelSwitch(CBPacketVoiceChannelSwitch packet) {
        Player player = (Player) packet.getAttachment();
        CheatBreakerAPI.getInstance().getVoiceChatHandler().setActiveChannel(player, packet.getSwitchingTo());
    }

    @Override
    public void handleVoiceMute(CBPacketVoiceMute packet) {
        Player player = (Player) packet.getAttachment();
        UUID muting = packet.getMuting();

        VoiceChannel channel = CheatBreakerAPI.getInstance().getVoiceChatHandler().getPlayerActiveChannels().get(player.getUniqueId());
        if (channel == null) return;

        CheatBreakerAPI.getInstance().getVoiceChatHandler().toggleVoiceMute(player, muting);
    }
}
