package com.oldcheatbreaker.nethandler.client;

import com.oldcheatbreaker.nethandler.ByteBufWrapper;
import com.oldcheatbreaker.nethandler.CBPacket;
import com.oldcheatbreaker.nethandler.ICBNetHandler;
import com.oldcheatbreaker.nethandler.server.ICBNetHandlerServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CBPacketVoiceMute extends CBPacket {

    private UUID muting;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeUUID(this.muting);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.muting = in.readUUID();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerServer) handler).handleVoiceMute(this);
    }

}
