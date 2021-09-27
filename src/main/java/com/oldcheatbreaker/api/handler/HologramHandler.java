package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.nethandler.server.CBPacketAddHologram;
import com.oldcheatbreaker.nethandler.server.CBPacketRemoveHologram;
import com.oldcheatbreaker.nethandler.server.CBPacketUpdateHologram;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
public class HologramHandler {

    private final CheatBreakerAPI plugin;

    public void addHologram(Player player, UUID id, Vector position, String[] lines) {
        plugin.sendPacket(player, new CBPacketAddHologram(id, position.getX(), position.getY(), position.getZ(), Arrays.asList(lines)));
    }

    public void updateHologram(Player player, UUID id, String[] lines) {
        plugin.sendPacket(player, new CBPacketUpdateHologram(id, Arrays.asList(lines)));
    }

    public void removeHologram(Player player, UUID id) {
        plugin.sendPacket(player, new CBPacketRemoveHologram(id));
    }

}
