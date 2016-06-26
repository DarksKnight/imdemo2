package com.GF.platform.uikit.event;

/**
 * Created by sunhaoyang on 2016/6/22.
 */
public class GFMessageStatusEvent extends GFBaseEvent {

    public boolean isSuccess;

    public GFMessageStatusEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
