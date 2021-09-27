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
public class CBPacketStaffModState extends CBPacket {

    private String mod;
    private boolean state;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeString(this.mod);
        out.buf().writeBoolean(this.state);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.mod = in.readString();
        this.state = in.buf().readBoolean();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient) handler).handleStaffModState(this);
    }

}
