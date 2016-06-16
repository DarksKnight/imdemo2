package com.GF.platform.uikit.widget.chatkeyboard.base.ports;

import com.GF.platform.uikit.widget.chatkeyboard.GFChatKeyBoard;

/**
 * Created by sunhaoyang on 2016/6/9.
 */

public interface GFKeyBoardPorts {

    void reset();

    void switchBoard(GFChatKeyBoard.Type type);
}
