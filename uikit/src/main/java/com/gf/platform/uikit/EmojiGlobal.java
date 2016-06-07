package com.GF.platform.uikit;

import android.content.Context;

import com.GF.platform.uikit.widget.chatkeyboard.base.entity.EmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.util.EmojiUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by sunhaoyang on 2016/6/2.
 */

public class EmojiGlobal {

    private List<EmoticonEntity> emojis = null;
    private Map<String, String> emojisCode = null;

    private volatile static EmojiGlobal singleton;

    private EmojiGlobal() {
    }

    public static EmojiGlobal getInstance() {
        if (singleton == null) {
            synchronized (EmojiGlobal.class) {
                if (singleton == null) {
                    singleton = new EmojiGlobal();
                }
            }
        }
        return singleton;
    }

    public void init(Context context) {
        emojis = EmojiUtil.parseEmoji(context);
        emojisCode = EmojiUtil.parseEmojiCode(context);
    }

    public List<EmoticonEntity> getEmojis() {
        return emojis;
    }

    public Map<String, String> getEmojisCode() {
        return emojisCode;
    }
}
