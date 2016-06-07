package com.GF.platform.uikit.widget.chatkeyboard.base.ports;


import com.GF.platform.uikit.widget.chatkeyboard.base.entity.EmoticonEntity;

/**
 * Created by sunhaoyang on 2016/4/25.
 */
public interface EmojiListener {

    void selectedEmoji(EmoticonEntity entity);

    void selectedBackSpace();
}
