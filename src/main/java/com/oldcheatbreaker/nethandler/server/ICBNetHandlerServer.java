package com.oldcheatbreaker.nethandler.server;

import com.oldcheatbreaker.nethandler.ICBNetHandler;
import com.oldcheatbreaker.nethandler.client.CBPacketClientVoice;
import com.oldcheatbreaker.nethandler.client.CBPacketVoiceChannelSwitch;
import com.oldcheatbreaker.nethandler.client.CBPacketVoiceMute;

public interface ICBNetHandlerServer extends ICBNetHandler {

    void handleVoice(CBPacketClientVoice var1);

    void handleVoiceChannelSwitch(CBPacketVoiceChannelSwitch var1);

    void handleVoiceMute(CBPacketVoiceMute var1);

}
