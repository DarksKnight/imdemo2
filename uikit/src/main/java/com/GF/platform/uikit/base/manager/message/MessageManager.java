package com.GF.platform.uikit.base.manager.message;

import com.GF.platform.uikit.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息管理类
 * Created by apple on 16/6/11.
 */

public class MessageManager {

    private static MessageManager singleton = null;

    public List<Message> messages = new ArrayList<>();

    public static MessageManager getInstance() {
        if (singleton == null) {
            synchronized (MessageManager.class) {
                if (singleton == null) {
                    singleton = new MessageManager();
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
