package com.GF.platform.uikit.base.manager.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 16/6/11.
 */

public class GFMessageManager {
    private static GFMessageManager singleton = null;
    private Map<String, GFMessageControl> infos = new HashMap<>();

    public static GFMessageManager getDefault() {
        if (singleton == null) {
            synchronized (GFMessageManager.class) {
                if (singleton == null) {
                    singleton = new GFMessageManager();
                }
            }
        }
        return singleton;
    }

    public void put(String sign, GFMessageControl GFMessageControl) {
        infos.put(sign, GFMessageControl);
    }

    public GFMessageControl getMessageControl(String sign) {
        return infos.get(sign);
    }
}
