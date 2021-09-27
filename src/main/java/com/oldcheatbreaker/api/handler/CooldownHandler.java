package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.object.ClientCooldown;
import com.oldcheatbreaker.nethandler.server.CBPacketCooldown;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CooldownHandler {

    private final CheatBreakerAPI plugin;

    public void sendCooldown(Player player, ClientCooldown cooldown) {
        plugin.sendPacket(player, new CBPacketCooldown(cooldown.getMessage(), cooldown.getDurationMs(), cooldown.getIcon().getId()));
    }

    public void clearCooldown(Player player, ClientCooldown cooldown) {
        plugin.sendPacket(player, new CBPacketCooldown(cooldown.getMessage(), 0L, cooldown.getIcon().getId()));
    }
}
