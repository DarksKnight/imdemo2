package com.gf.platform.gfplatform.widget.chatkeyboard.base.ports;

import com.gf.platform.gfplatform.widget.chatkeyboard.base.entity.EmoticonEntity;

/**
 * Created by sunhaoyang on 2016/4/25.
 */
public interface KeyBoardListener {

    void sendMessage(String text);

    void sendEmoticon(EmoticonEntity entity);

    void delMessage();
}
