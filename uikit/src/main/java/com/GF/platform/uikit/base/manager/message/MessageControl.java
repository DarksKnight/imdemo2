package com.GF.platform.uikit.base.manager.message;

import com.GF.platform.uikit.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天消息管理类
 * Created by apple on 16/6/11.
 */

public class MessageControl {

    private List<Message> messages = new ArrayList<>();

    public void setMessages(List<Message> msgs) {
        messages = msgs;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void clear() {
        messages.clear();
    }

    public void remove(int position) {
        messages.remove(position);
    }

    public void remove(Message message) {
        messages.remove(message);
    }

    public Message getMessage(int position) {
        return messages.get(position);
    }

    public int getMessageSize() {
        return messages.size();
    }

    public void addAll(int position, List<Message> msgs) {
        messages.addAll(position, msgs);
    }
}
