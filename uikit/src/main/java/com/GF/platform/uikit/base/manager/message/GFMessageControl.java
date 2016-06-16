package com.GF.platform.uikit.base.manager.message;

import com.GF.platform.uikit.entity.GFMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天消息管理类
 * Created by apple on 16/6/11.
 */

public class GFMessageControl {

    private List<GFMessage> GFMessages = new ArrayList<>();

    public void setGFMessages(List<GFMessage> msgs) {
        GFMessages = msgs;
    }

    public void addMessage(GFMessage GFMessage) {
        GFMessages.add(GFMessage);
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

    public void remove(List<GFMessage> msgs) {
        GFMessages.removeAll(msgs);
    }

    public GFMessage getMessage(int position) {
        return GFMessages.get(position);
    }

    public int getMessageSize() {
        return GFMessages.size();
    }

    public void addAll(int position, List<GFMessage> msgs) {
        GFMessages.addAll(position, msgs);
    }
}
