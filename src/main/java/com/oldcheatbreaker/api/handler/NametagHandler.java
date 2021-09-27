package com.oldcheatbreaker.api.handler;

import com.google.common.collect.ImmutableList;
import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.nethandler.server.CBPacketOverrideNametags;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class NametagHandler {

    private final CheatBreakerAPI plugin;

    public void overrideNametag(Player target, List<String> nametag, Player viewer) {
        plugin.sendPacket(viewer, new CBPacketOverrideNametags(target.getUniqueId(), nametag));
    }

    public void resetNametag(Player target, Player viewer) {
        plugin.sendPacket(viewer, new CBPacketOverrideNametags(target.getUniqueId(), null));
    }

    public void hideNametag(Player target, Player viewer) {
        plugin.sendPacket(viewer, new CBPacketOverrideNametags(target.getUniqueId(), ImmutableList.of()));
    }
}
