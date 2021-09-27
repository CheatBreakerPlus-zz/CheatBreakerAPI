package com.oldcheatbreaker.api.listener;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.event.PlayerRegisterCBEvent;
import com.oldcheatbreaker.nethandler.obj.ServerRule;
import com.oldcheatbreaker.nethandler.server.CBPacketServerRule;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

import java.util.ArrayList;

@RequiredArgsConstructor
public class RegisterChannelListener implements Listener {

    private final CheatBreakerAPI plugin;

    @EventHandler
    public void onRegister(PlayerRegisterChannelEvent event) {
        if (event.getChannel().equals(CheatBreakerAPI.getInstance().getCheatBreakerMessageChannel())) {
            plugin.playersNotRegistered.remove(event.getPlayer().getUniqueId());
            plugin.playersRunningCheatBreaker.add(event.getPlayer().getUniqueId());

            plugin.getVoiceChatHandler().muteMap.put(event.getPlayer().getUniqueId(), new ArrayList<>());

            if (plugin.getVoiceChatHandler().voiceEnabled) {
                plugin.sendPacket(event.getPlayer(), new CBPacketServerRule(ServerRule.VOICE_ENABLED, true));
            }

            if (plugin.cbPacketQueue.containsKey(event.getPlayer().getUniqueId())) {
                plugin.cbPacketQueue.get(event.getPlayer().getUniqueId()).forEach(p -> plugin.sendPacket(event.getPlayer(), p));

                plugin.cbPacketQueue.remove(event.getPlayer().getUniqueId());
            }

            plugin.getServer().getPluginManager().callEvent(new PlayerRegisterCBEvent(event.getPlayer()));
            plugin.getWorldHandler().updateWorld(event.getPlayer());
        }
    }
}
