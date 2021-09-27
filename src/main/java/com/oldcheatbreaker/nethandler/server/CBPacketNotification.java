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
public class CBPacketNotification extends CBPacket {

    private String message;
    private long durationMs;
    private String level;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeString(this.message);
        out.buf().writeLong(this.durationMs);
        out.writeString(this.level);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.message = in.readString();
        this.durationMs = in.buf().readLong();
        this.level = in.readString();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient) handler).handleNotification(this);
    }

}
