package com.GF.platform.uikit.event;

/**
 * 功能选择
 * Created by sunhaoyang on 16/6/11.
 */

public class GFFunctionSelectedEvent extends GFBaseEvent {
    public int position;

    public GFFunctionSelectedEvent(int position) {
        this.position = position;
    }
}
