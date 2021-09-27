package com.oldcheatbreaker.api.listener;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {
    private final CheatBreakerAPI plugin;

    @EventHandler
    public void onUnregister(PlayerQuitEvent event) {
        plugin.getVoiceChatHandler().getPlayerChannels(event.getPlayer()).forEach(channel -> channel.removePlayer(event.getPlayer()));

        if (plugin.playersRunningCheatBreaker.contains(event.getPlayer().getUniqueId()))
            plugin.playersRunningCheatBreaker.remove(event.getPlayer().getUniqueId());


        plugin.getVoiceChatHandler().getPlayerActiveChannels().remove(event.getPlayer().getUniqueId());
        plugin.getVoiceChatHandler().muteMap.remove(event.getPlayer().getUniqueId());
        plugin.playersNotRegistered.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!plugin.isRunningCheatBreaker(event.getPlayer())) {
                plugin.playersNotRegistered.add(event.getPlayer().getUniqueId());
                plugin.cbPacketQueue.remove(event.getPlayer().getUniqueId());
            }
        }, 2 * 20L);
    }
}
