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
public class CBPacketVoiceChannelUpdate extends CBPacket {

    public int status;
    private UUID channelUuid;
    private UUID uuid;
    private String name;

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.writeVarInt(this.status);
        out.writeUUID(this.channelUuid);
        out.writeUUID(this.uuid);
        out.writeString(this.name);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException {
        this.status = in.readVarInt();
        this.channelUuid = in.readUUID();
        this.uuid = in.readUUID();
        this.name = in.readString();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient) handler).handleVoiceChannelUpdate(this);
    }

}
