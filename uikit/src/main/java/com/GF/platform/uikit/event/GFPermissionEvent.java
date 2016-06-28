package com.GF.platform.uikit.event;

/**
 * Created by sunhaoyang on 2016/6/28.
 */
public class GFPermissionEvent extends GFBaseEvent {

    public boolean isExit;

    public GFPermissionEvent(boolean isExit) {
        this.isExit = isExit;
    }
}
