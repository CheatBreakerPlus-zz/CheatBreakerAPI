package com.oldcheatbreaker.nethandler;

import com.oldcheatbreaker.nethandler.shared.CBPacketAddWaypoint;
import com.oldcheatbreaker.nethandler.shared.CBPacketRemoveWaypoint;

public interface ICBNetHandler {

    void handleAddWaypoint(CBPacketAddWaypoint var1);

    void handleRemoveWaypoint(CBPacketRemoveWaypoint var1);

}
