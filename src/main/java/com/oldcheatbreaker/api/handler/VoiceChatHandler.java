package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.voice.VoiceChannel;
import com.oldcheatbreaker.nethandler.server.CBPacketDeleteVoiceChannel;
import com.oldcheatbreaker.nethandler.server.CBPacketVoiceChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VoiceChatHandler {

    @Getter private final Map<UUID, VoiceChannel> playerActiveChannels = new HashMap<>();
    @Getter private final List<VoiceChannel> voiceChannels = new ArrayList<>();
    public final Map<UUID, List<UUID>> muteMap = new HashMap<>();
    private final CheatBreakerAPI plugin;
    public boolean voiceEnabled;

    public void createBulkChannels(VoiceChannel... voiceChannels) {
        this.voiceChannels.addAll(Arrays.asList(voiceChannels));
        for (VoiceChannel channel : voiceChannels) {
            for (Player player : channel.getPlayersInChannel()) {
                sendChannel(player, channel);
            }
        }
    }

    public void deleteChannel(VoiceChannel channel) {
        this.voiceChannels.removeIf(c -> {
            boolean remove = c == channel;
            if (remove) {
                channel.validatePlayers();
                for (Player player : channel.getPlayersInChannel()) {
                    plugin.sendPacket(player, new CBPacketDeleteVoiceChannel(channel.getUuid()));
                    if (getPlayerActiveChannels().get(player.getUniqueId()) == channel) {
                        getPlayerActiveChannels().remove(player);
                    }
                }
            }
            return remove;
        });
    }

    public void deleteChannel(UUID channelUUID) {
        getChannel(channelUUID).ifPresent(c -> deleteChannel(c));
    }

    public List<VoiceChannel> getPlayerChannels(Player player) {
        return this.voiceChannels.stream().filter(channel -> channel.hasPlayer(player)).collect(Collectors.toList());
    }

    public void sendChannel(Player player, VoiceChannel channel) {
        channel.validatePlayers();
        plugin.sendPacket(player, new CBPacketVoiceChannel(channel.getUuid(), channel.getName(), channel.toPlayersMap(), channel.toListeningMap()));
    }

    public void setActiveChannel(Player player, UUID uuid) {
        getChannel(uuid).ifPresent(channel -> setActiveChannel(player, channel));
    }

    public Optional<VoiceChannel> getChannel(UUID uuid) {
        return voiceChannels.stream().filter(channel -> channel.getUuid().equals(uuid)).findFirst();
    }

    public void setActiveChannel(Player player, VoiceChannel channel) {
        channel.setActive(player);
    }

    public void toggleVoiceMute(Player player, UUID other) {
        if (!muteMap.get(player.getUniqueId()).removeIf(uuid -> uuid.equals(other))) {
            muteMap.get(player.getUniqueId()).add(other);
        }
    }

    public boolean playerHasPlayerMuted(Player player, Player other) {
        return muteMap.get(other.getUniqueId()).contains(player.getUniqueId());
    }

}
