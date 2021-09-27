package com.oldcheatbreaker.nethandler.server;

import com.oldcheatbreaker.nethandler.ByteBufWrapper;
import com.oldcheatbreaker.nethandler.CBPacket;
import com.oldcheatbreaker.nethandler.ICBNetHandler;
import com.oldcheatbreaker.nethandler.client.ICBNetHandlerClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CBPacketUpdateHologram extends CBPacket {

    private UUID uuid;
    private List<String> lines;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeUUID(this.uuid);
        out.writeVarInt(this.lines.size());
        this.lines.forEach(out::writeString);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.uuid = in.readUUID();
        int linesSize = in.readVarInt();
        this.lines = new ArrayList<>();
        for (int i = 0; i < linesSize; ++i) {
            this.lines.add(in.readString());
        }
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient) handler).handleUpdateHologram(this);
    }

}
