package com.GF.platform.uikit.widget.chatkeyboard.base.ports;


import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFEmoticonEntity;

/**
 * Created by sunhaoyang on 2016/4/25.
 */
public interface GFEmojiListener {

    void selectedEmoji(GFEmoticonEntity entity);

    void selectedBackSpace();
}
