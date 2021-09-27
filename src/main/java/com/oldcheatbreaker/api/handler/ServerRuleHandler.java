package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.object.MinimapStatus;
import com.oldcheatbreaker.nethandler.obj.ServerRule;
import com.oldcheatbreaker.nethandler.server.CBPacketServerRule;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ServerRuleHandler {

    private final CheatBreakerAPI plugin;

    public void setMinimapStatus(Player player, MinimapStatus status) {
        plugin.sendPacket(player, new CBPacketServerRule(ServerRule.MINIMAP_STATUS, status.name()));
    }

    public void setCompetitiveGame(Player player, boolean isCompetitive) {
        plugin.sendPacket(player, new CBPacketServerRule(ServerRule.COMPETITIVE_GAMEMODE, isCompetitive));
    }

    public void setVoiceEnabled(boolean enabled) {
        plugin.getVoiceChatHandler().voiceEnabled = enabled;
    }
}
