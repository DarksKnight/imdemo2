package com.GF.platform.uikit.widget.chatkeyboard.base.command;

import com.GF.platform.uikit.base.GFViewBasePorts;
import com.GF.platform.uikit.base.manager.message.GFMessageControl;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFKeyBoardCommand;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFNurtureCommand implements GFKeyBoardCommand {

    private GFMessageControl control = null;
    private GFViewBasePorts ports = null;

    public GFNurtureCommand(GFMessageControl control, GFViewBasePorts ports) {
        this.control = control;
        this.ports = ports;
    }

    @Override
    public void execute() {
        GFMessage msg = new GFMessage("帅的一般", "", "22:22", "", GFMessage.Category.NURTURE, false);
        msg.setMsgId(System.currentTimeMillis() / 1000 + "");
        control.addMessage(msg);
        ports.uiRefresh();
    }
}
