package com.gf.platform.uikit.widget.chatkeyboard.base.ports;


import com.gf.platform.uikit.widget.chatkeyboard.base.entity.EmoticonEntity;

/**
 * Created by sunhaoyang on 2016/4/25.
 */
public interface KeyBoardListener {

    void sendMessage(String text);

    void sendEmoticon(EmoticonEntity entity);

    void delMessage();

    void functionSelected(int position);
}
