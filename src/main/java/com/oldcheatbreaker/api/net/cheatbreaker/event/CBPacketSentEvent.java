package com.oldcheatbreaker.api.net.cheatbreaker.event;

import com.oldcheatbreaker.nethandler.CBPacket;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CBPacketSentEvent extends PlayerEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final CBPacket packet;

    public CBPacketSentEvent(Player who, CBPacket packet) {
        super(who);
        this.packet = packet;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}