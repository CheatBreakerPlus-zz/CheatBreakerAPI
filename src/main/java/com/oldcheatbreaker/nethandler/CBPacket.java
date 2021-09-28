package com.oldcheatbreaker.nethandler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.oldcheatbreaker.nethandler.client.CBPacketClientVoice;
import com.oldcheatbreaker.nethandler.client.CBPacketVoiceChannelSwitch;
import com.oldcheatbreaker.nethandler.client.CBPacketVoiceMute;
import com.oldcheatbreaker.nethandler.server.*;
import com.oldcheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.oldcheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;
import io.netty.buffer.Unpooled;
import lombok.Getter;

import java.io.IOException;

public abstract class CBPacket {

    private static final BiMap<Class<?>, Integer> REGISTRY = HashBiMap.create();

    static {
        addPacket(0, CBPacketAddWaypoint.class);
        addPacket(2, CBPacketRemoveWaypoint.class);
        addPacket(3, CBPacketCooldown.class);
        addPacket(4, CBPacketNotification.class);
        addPacket(5, CBPacketStaffModState.class);
        addPacket(6, CBPacketUpdateNametags.class);
        addPacket(7, CBPacketTeammates.class);
        addPacket(8, CBPacketOverrideNametags.class);
        addPacket(9, CBPacketAddHologram.class);
        addPacket(10, CBPacketUpdateHologram.class);
        addPacket(11, CBPacketRemoveHologram.class);
        addPacket(12, CBPacketTitle.class);
        addPacket(14, CBPacketServerRule.class);
        addPacket(15, CBPacketClientVoice.class);
        addPacket(16, CBPacketVoice.class);
        addPacket(17, CBPacketVoiceChannel.class);
        addPacket(18, CBPacketVoiceChannelUpdate.class);
        addPacket(19, CBPacketVoiceChannelSwitch.class);
        addPacket(20, CBPacketVoiceMute.class);
        addPacket(21, CBPacketDeleteVoiceChannel.class);
        addPacket(23, CBPacketUpdateWorld.class);
        addPacket(24, CBPacketServerUpdate.class);
        addPacket(25, CBPacketWorldBorder.class);
        addPacket(26, CBPacketWorldBorderUpdate.class);
        addPacket(27, CBPacketWorldBorderRemove.class);
        addPacket(32, CBPacketServerTheme.class);
    }

    @Getter
    private Object attachment;

    public static CBPacket handle(ICBNetHandler netHandler, byte[] data) {
        return CBPacket.handle(netHandler, data, null);
    }

    public static CBPacket handle(ICBNetHandler netHandler, byte[] data, Object attachment) {
        ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.wrappedBuffer(data));
        int packetId = wrappedBuffer.readVarInt();
        Class<?> packetClass = REGISTRY.inverse().get(packetId);
        if (packetClass != null) {
            try {
                CBPacket packet = (CBPacket) packetClass.newInstance();
                if (attachment != null) {
                    packet.attach(attachment);
                }
                packet.read(wrappedBuffer);
                return packet;
            } catch (IOException | IllegalAccessException | InstantiationException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] getPacketData(CBPacket packet) {
        ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.buffer());
        wrappedBuffer.writeVarInt(REGISTRY.get(packet.getClass()));
        try {
            packet.write(wrappedBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wrappedBuffer.buf().array();
    }

    private static void addPacket(int id, Class<?> clazz) {
        if (REGISTRY.containsKey(clazz)) {
            throw new IllegalArgumentException("Duplicate packet class (" + clazz.getSimpleName() + "), already used by " + REGISTRY.get(clazz));
        }
        if (REGISTRY.containsValue(id)) {
            throw new IllegalArgumentException("Duplicate packet ID (" + id + "), already used by " + REGISTRY.inverse().get(id));
        }
        REGISTRY.put(clazz, id);
    }

    public abstract void write(ByteBufWrapper out) throws IOException;

    public abstract void read(ByteBufWrapper in) throws IOException;

    public abstract void process(ICBNetHandler handler);

    protected void writeBlob(ByteBufWrapper b, byte[] bytes) {
        b.buf().writeShort(bytes.length);
        b.buf().writeBytes(bytes);
    }

    protected byte[] readBlob(ByteBufWrapper b) {
        short key = b.buf().readShort();
        if (key < 0) {
            System.out.println("Key was smaller than nothing!  Weird key!");
            return null;
        }
        byte[] blob = new byte[key];
        b.buf().readBytes(blob);
        return blob;
    }

    public void attach(Object obj) {
        this.attachment = obj;
    }

}
