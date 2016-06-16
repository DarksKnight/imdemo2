package com.GF.platform.uikit;

import android.content.Context;

import com.GF.platform.uikit.widget.chatkeyboard.base.entity.GFEmoticonEntity;
import com.GF.platform.uikit.widget.chatkeyboard.util.GFEmojiUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by sunhaoyang on 2016/6/2.
 */

public class GFEmojiGlobal {

    private List<GFEmoticonEntity> emojis = null;
    private Map<String, String> emojisCode = null;

    private volatile static GFEmojiGlobal singleton;

    private GFEmojiGlobal() {
    }

    public static GFEmojiGlobal getInstance() {
        if (singleton == null) {
            synchronized (GFEmojiGlobal.class) {
                if (singleton == null) {
                    singleton = new GFEmojiGlobal();
                }
            }
        }
        return singleton;
    }

    public void init(Context context) {
        emojis = GFEmojiUtil.parseEmoji(context);
        emojisCode = GFEmojiUtil.parseEmojiCode(context);
    }

    public List<GFEmoticonEntity> getEmojis() {
        return emojis;
    }

    public Map<String, String> getEmojisCode() {
        return emojisCode;
    }
}
