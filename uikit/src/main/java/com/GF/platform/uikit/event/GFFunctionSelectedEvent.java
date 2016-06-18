package com.GF.platform.uikit.event;

/**
 * 功能选择
 * Created by sunhaoyang on 16/6/11.
 */

public class GFFunctionSelectedEvent extends GFBaseEvent {
    public String str;

    public GFFunctionSelectedEvent(String str) {
        this.str = str;
    }
}
