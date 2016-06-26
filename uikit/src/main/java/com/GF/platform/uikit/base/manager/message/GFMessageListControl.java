package com.GF.platform.uikit.base.manager.message;

import com.GF.platform.uikit.entity.GFMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表管理类
 * Created by apple on 16/6/11.
 */

public class GFMessageListControl {

    private static GFMessageListControl singleton = null;

    public List<GFMessage> gfMessages = new ArrayList<>();

    private GFMessageListControl() {
    }

    public static GFMessageListControl getDefault() {
        if (singleton == null) {
            synchronized (GFMessageListControl.class) {
                if (singleton == null) {
                    singleton = new GFMessageListControl();
                }
            }
        }
        return singleton;
    }

    public void addMessage(GFMessage gfMessage) {
        gfMessages.add(gfMessage);
    }

    public void addMessage(int position, GFMessage gfMessage) {
        gfMessages.add(position, gfMessage);
    }

    public List<GFMessage> getGFMessages() {
        return gfMessages;
    }

    public void clear() {
        gfMessages.clear();
    }

    public void remove(int position) {
        gfMessages.remove(position);
    }

    public void remove(GFMessage gfMessage) {
        gfMessages.remove(gfMessage);
    }

    public GFMessage getMessage(int position) {
        return gfMessages.get(position);
    }

    public int getMessageSize() {
        return gfMessages.size();
    }
}
