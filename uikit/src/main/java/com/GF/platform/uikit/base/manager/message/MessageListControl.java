package com.GF.platform.uikit.base.manager.message;

import com.GF.platform.uikit.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表管理类
 * Created by apple on 16/6/11.
 */

public class MessageListControl {

    private static MessageListControl singleton = null;

    public List<Message> messages = new ArrayList<>();

    public static MessageListControl getDefault() {
        if (singleton == null) {
            synchronized (MessageListControl.class) {
                if (singleton == null) {
                    singleton = new MessageListControl();
                }
            }
        }
        return singleton;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMessage(int position, Message message) {
        messages.add(position, message);
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
}
