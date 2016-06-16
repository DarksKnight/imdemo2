package com.GF.platform.uikit.event;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.util.GFLogProxy;

import org.greenrobot.eventbus.EventBus;

/**
 * 事件分发
 * Created by sunhaoyang on 2016/6/13.
 */

public class GFEventDispatch {

    private static EventBus event = EventBus.getDefault();
    private static GFBaseEvent GFBaseEvent = null;

    public static void post(int eventId, Object... objs) {
        switch (eventId) {
            case GFConstant.EVENT_SEND_MESSAGE:
                GFBaseEvent = new GFSendMessageEvent((GFMessage)objs[0]);
                break;
            case GFConstant.EVENT_DELETE_MESSAGE:
                GFBaseEvent = new GFDeleteMessageEvent();
                break;
            case GFConstant.EVENT_UI:
                GFBaseEvent = new GFBaseEvent();
                break;
            case GFConstant.EVENT_FUNCTION_SELECTED:
                GFBaseEvent = new GFFunctionSelectedEvent((int)objs[0]);
                break;
            default:
                break;
        }

        if (null == GFBaseEvent) {
            GFLogProxy.i("baseEvent is null");
        } else {
            event.post(GFBaseEvent);
        }
    }

    public static void register(Object object) {
        event.register(object);
    }

    public static void unregister(Object object) {
        event.unregister(object);
    }
}
