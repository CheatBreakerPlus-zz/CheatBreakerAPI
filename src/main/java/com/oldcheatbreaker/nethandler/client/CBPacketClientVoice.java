package com.oldcheatbreaker.nethandler.client;

import com.oldcheatbreaker.nethandler.ByteBufWrapper;
import com.oldcheatbreaker.nethandler.CBPacket;
import com.oldcheatbreaker.nethandler.ICBNetHandler;
import com.oldcheatbreaker.nethandler.server.ICBNetHandlerServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CBPacketClientVoice extends CBPacket {

    private byte[] data;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        this.writeBlob(out, this.data);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.data = this.readBlob(in);
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerServer) handler).handleVoice(this);
    }

}
