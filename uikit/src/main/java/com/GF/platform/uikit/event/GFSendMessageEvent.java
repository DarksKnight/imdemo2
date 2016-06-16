package com.GF.platform.uikit.event;

import com.GF.platform.uikit.entity.GFMessage;

/**
 * 发送消息
 * Created by sunhaoyang on 16/6/11.
 */

public class GFSendMessageEvent extends GFBaseEvent {

    public GFMessage GFMessage;

    public GFSendMessageEvent(GFMessage GFMessage) {
        this.GFMessage = GFMessage;
    }

}
