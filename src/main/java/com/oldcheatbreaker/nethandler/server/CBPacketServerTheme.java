package com.oldcheatbreaker.nethandler.server;

import com.oldcheatbreaker.nethandler.ByteBufWrapper;
import com.oldcheatbreaker.nethandler.CBPacket;
import com.oldcheatbreaker.nethandler.ICBNetHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class CBPacketServerTheme extends CBPacket {

    private int bgColor;
    private int textColor;
    private int accentColor;
    private int optionHoveredColor;
    private int enabledModuleColor;
    private int disabledModuleColor;

    public CBPacketServerTheme(int bgColor, int textColor, int accentColor, int optionHoveredColor, int enabledModuleColor, int disabledModuleColor) {
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.accentColor = accentColor;
        this.optionHoveredColor = optionHoveredColor;
        this.enabledModuleColor = enabledModuleColor;
        this.disabledModuleColor = disabledModuleColor;
    }

    @Override
    public void write(ByteBufWrapper out) throws IOException {
        out.buf().writeInt(bgColor);
        out.buf().writeInt(textColor);
        out.buf().writeInt(accentColor);
        out.buf().writeInt(optionHoveredColor);
        out.buf().writeInt(enabledModuleColor);
        out.buf().writeInt(disabledModuleColor);
    }

    @Override
    public void read(ByteBufWrapper in) throws IOException { }

    @Override
    public void process(ICBNetHandler handler) { }
}
