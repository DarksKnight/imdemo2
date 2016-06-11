package com.GF.platform.uikit.base.manager.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 16/6/11.
 */

public class MessageManager {
    private static MessageManager singleton = null;
    private Map<String, MessageControl> infos = new HashMap<>();

    public static MessageManager getDefault() {
        if (singleton == null) {
            synchronized (MessageManager.class) {
                if (singleton == null) {
                    singleton = new MessageManager();
                }
            }
        }
        return singleton;
    }

    public void put(String sign, MessageControl messageControl) {
        infos.put(sign, messageControl);
    }

    public MessageControl getMessageControl(String sign) {
        return infos.get(sign);
    }
}
