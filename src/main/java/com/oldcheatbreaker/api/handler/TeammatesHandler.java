package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.nethandler.server.CBPacketTeammates;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TeammatesHandler {

    private final CheatBreakerAPI plugin;

    public void sendTeammates(Player player, CBPacketTeammates packet) {
        this.validatePlayers(player, packet);
        plugin.sendPacket(player, packet);
    }

    public void validatePlayers(Player sendingTo, CBPacketTeammates packet) {
        packet.getPlayers().entrySet().removeIf(entry -> Bukkit.getPlayer(entry.getKey()) != null && !Bukkit.getPlayer(entry.getKey()).getWorld().equals(sendingTo.getWorld()));
    }
}
