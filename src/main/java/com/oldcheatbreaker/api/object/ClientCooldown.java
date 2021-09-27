package com.oldcheatbreaker.api.object;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Material;

import java.util.concurrent.TimeUnit;

public final class ClientCooldown {

    @Getter
    private final String message;
    @Getter
    private final long durationMs;
    @Getter
    private final Material icon;

    public ClientCooldown(String message, long unitCount, TimeUnit unit, Material icon) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.durationMs = unit.toMillis(unitCount);
        this.icon = Preconditions.checkNotNull(icon, "icon");
    }
}