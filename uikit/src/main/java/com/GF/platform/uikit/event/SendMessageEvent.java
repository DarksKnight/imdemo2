package com.GF.platform.uikit.event;

import com.GF.platform.uikit.entity.Message;

/**
 * 发送消息
 * Created by sunhaoyang on 16/6/11.
 */

public class SendMessageEvent {

    public Message message;

    public SendMessageEvent(Message message) {
        this.message = message;
    }

}
