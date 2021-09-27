package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.object.CBNotification;
import com.oldcheatbreaker.nethandler.server.CBPacketNotification;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class NotificationHandler {

    private final CheatBreakerAPI plugin;

    public void sendNotification(Player player, CBNotification notification) {
        plugin.sendPacket(player, new CBPacketNotification(
                notification.getMessage(),
                notification.getDurationMs(),
                notification.getLevel().name()
        ));
    }

    public void sendNotificationOrFallback(Player player, CBNotification notification, Runnable fallback) {
        if (plugin.isRunningCheatBreaker(player)) {
            sendNotification(player, notification);
        } else {
            fallback.run();
        }
    }
}
