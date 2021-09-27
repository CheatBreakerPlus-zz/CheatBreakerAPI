package com.oldcheatbreaker.nethandler.server;

import com.oldcheatbreaker.nethandler.ByteBufWrapper;
import com.oldcheatbreaker.nethandler.CBPacket;
import com.oldcheatbreaker.nethandler.ICBNetHandler;
import com.oldcheatbreaker.nethandler.client.ICBNetHandlerClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CBPacketVoice extends CBPacket {

    private UUID uuid;
    private byte[] data;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeUUID(this.uuid);
        this.writeBlob(out, this.data);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.uuid = in.readUUID();
        this.data = this.readBlob(in);
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient) handler).handleVoice(this);
    }

}
