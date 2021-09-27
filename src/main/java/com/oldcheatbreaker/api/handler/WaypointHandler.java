package com.oldcheatbreaker.api.handler;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import com.oldcheatbreaker.api.object.CBWaypoint;
import com.oldcheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.oldcheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class WaypointHandler {

    private final CheatBreakerAPI plugin;

    public void sendWaypoint(Player player, CBWaypoint waypoint) {
        plugin.sendPacket(player, new CBPacketAddWaypoint(
                waypoint.getName(),
                waypoint.getWorld(),
                waypoint.getColor(),
                waypoint.getX(),
                waypoint.getY(),
                waypoint.getZ(),
                waypoint.isForced(),
                waypoint.isVisible()
        ));
    }

    public void removeWaypoint(Player player, CBWaypoint waypoint) {
        plugin.sendPacket(player, new CBPacketRemoveWaypoint(
                waypoint.getName(),
                waypoint.getWorld()
        ));
    }


}
