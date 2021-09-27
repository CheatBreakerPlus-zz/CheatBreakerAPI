package com.oldcheatbreaker.nethandler.server;

import com.oldcheatbreaker.nethandler.ByteBufWrapper;
import com.oldcheatbreaker.nethandler.CBPacket;
import com.oldcheatbreaker.nethandler.ICBNetHandler;
import com.oldcheatbreaker.nethandler.client.ICBNetHandlerClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CBPacketWorldBorderUpdate extends CBPacket {

    private String id;
    private double minX;
    private double minZ;
    private double maxX;
    private double maxZ;
    private int durationTicks;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeString(this.id);
        out.buf().writeDouble(this.minX);
        out.buf().writeDouble(this.minZ);
        out.buf().writeDouble(this.maxX);
        out.buf().writeDouble(this.maxZ);
        out.buf().writeInt(this.durationTicks);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.id = in.readString();
        this.minX = in.buf().readDouble();
        this.minZ = in.buf().readDouble();
        this.maxX = in.buf().readDouble();
        this.maxZ = in.buf().readDouble();
        this.durationTicks = in.buf().readInt();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient) handler).handleWorldBorderUpdate(this);
    }

}
