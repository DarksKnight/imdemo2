package com.GF.platform.uikit.event;

import com.GF.platform.uikit.GFConstant;
import com.GF.platform.uikit.entity.GFMessage;
import com.GF.platform.uikit.util.GFLogProxy;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 事件分发
 * Created by sunhaoyang on 2016/6/13.
 */

public class GFEventDispatch {

    private static EventBus event = EventBus.getDefault();
    private static GFBaseEvent gfBaseEvent = null;

    public static void post(int eventId, Object... objs) {
        switch (eventId) {
            case GFConstant.EVENT_SEND_MESSAGE:
                gfBaseEvent = new GFSendMessageEvent((GFMessage)objs[0]);
                break;
            case GFConstant.EVENT_DELETE_MESSAGE:
                gfBaseEvent = new GFDeleteMessageEvent();
                break;
            case GFConstant.EVENT_UI:
                gfBaseEvent = new GFBaseEvent();
                break;
            case GFConstant.EVENT_FUNCTION_SELECTED:
                gfBaseEvent = new GFFunctionSelectedEvent(objs[0].toString());
                break;
            case GFConstant.EVENT_IMAGE_SELECT:
                gfBaseEvent = new GFImageSelectEvent((List<String>) objs[0]);
                break;
            default:
                break;
        }

        if (null == gfBaseEvent) {
            GFLogProxy.i("baseEvent is null");
        } else {
            event.post(gfBaseEvent);
        }
    }

    public static void register(Object object) {
        event.register(object);
    }

    public static void unregister(Object object) {
        event.unregister(object);
    }
}
