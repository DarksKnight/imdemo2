package com.GF.platform.uikit.widget.chatkeyboard.base.ports;

import com.GF.platform.uikit.widget.chatkeyboard.ChatKeyBoard;

/**
 * Created by sunhaoyang on 2016/6/9.
 */

public interface KeyBoardPorts {

    void reset();

    void switchBoard(ChatKeyBoard.Type type);
}
