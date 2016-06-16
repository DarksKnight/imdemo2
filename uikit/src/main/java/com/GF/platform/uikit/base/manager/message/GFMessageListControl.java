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

    public List<GFMessage> GFMessages = new ArrayList<>();

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

    public void addMessage(GFMessage GFMessage) {
        GFMessages.add(GFMessage);
    }

    public void addMessage(int position, GFMessage GFMessage) {
        GFMessages.add(position, GFMessage);
    }

    public List<GFMessage> getGFMessages() {
        return GFMessages;
    }

    public void clear() {
        GFMessages.clear();
    }

    public void remove(int position) {
        GFMessages.remove(position);
    }

    public void remove(GFMessage GFMessage) {
        GFMessages.remove(GFMessage);
    }

    public GFMessage getMessage(int position) {
        return GFMessages.get(position);
    }

    public int getMessageSize() {
        return GFMessages.size();
    }
}
