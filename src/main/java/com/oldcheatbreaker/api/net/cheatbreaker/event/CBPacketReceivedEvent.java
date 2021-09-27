package com.oldcheatbreaker.api.net.cheatbreaker.event;

import com.oldcheatbreaker.nethandler.CBPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CBPacketReceivedEvent extends PlayerEvent implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final CBPacket packet;

    private boolean cancelled;

    public CBPacketReceivedEvent(Player who, CBPacket packet) {
        super(who);
        this.packet = packet;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}