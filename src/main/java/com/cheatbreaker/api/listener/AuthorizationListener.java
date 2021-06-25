package com.cheatbreaker.api.listener;

import com.cheatbreaker.api.CheatBreakerAPI;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@RequiredArgsConstructor
public class AuthorizationListener implements Listener {

    private final CheatBreakerAPI plugin;

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (!plugin.getConfig().getBoolean("auth.kick-if-banned", true))
            return;

        plugin.isCheatBreakerBanned(event.getUniqueId(), value ->
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, plugin.getConfig().getString(
                        "auth.kick-message",
                        "&cCheatBreaker authentication failure\nContact staff with error code 0x0A")
                        .replaceAll("\n", "\n"))
        );
    }
}