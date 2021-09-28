package com.oldcheatbreaker.api.object;

import com.oldcheatbreaker.api.CheatBreakerAPI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Location;

@EqualsAndHashCode
@AllArgsConstructor
public final class CBWaypoint {

    @Getter private final String name;
    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;
    @Getter private final String world;
    @Getter private final int color;
    @Getter private final boolean forced;
    @Getter private final boolean visible;

    public CBWaypoint(String name, Location location, int color, boolean forced, boolean visible) {
        this(
                name,
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                CheatBreakerAPI.getInstance().getWorldHandler().getWorldIdentifier(location.getWorld()),
                color,
                forced, visible
        );
    }

    public CBWaypoint(String name, Location location, int color, boolean forced) {
        this(
                name,
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                CheatBreakerAPI.getInstance().getWorldHandler().getWorldIdentifier(location.getWorld()),
                color,
                forced,
                true
        );
    }

}