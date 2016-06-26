package com.GF.platform.uikit.base.manager.message;

import com.GF.platform.uikit.entity.GFMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天消息管理类
 * Created by apple on 16/6/11.
 */

public class GFMessageControl {

    private List<GFMessage> gfMessages = new ArrayList<>();
    private Map<String, GFMessage> messages = new HashMap<>();

    public void setGFMessages(List<GFMessage> msgs) {
        gfMessages = msgs;
    }

    public void addMessage(GFMessage gfMessage) {
        gfMessages.add(gfMessage);
        messages.put(gfMessage.getMsgId(), gfMessage);
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

    public void remove(List<GFMessage> msgs) {
        gfMessages.removeAll(msgs);
    }

    public GFMessage getMessage(int position) {
        return gfMessages.get(position);
    }

    public GFMessage getMessage(String msgId) {
        return messages.get(msgId);
    }

    public int getMessageSize() {
        return gfMessages.size();
    }

    public void addAll(int position, List<GFMessage> msgs) {
        gfMessages.addAll(position, msgs);
    }
}
