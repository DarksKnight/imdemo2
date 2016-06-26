package com.GF.platform.uiview.factory.view;

import com.GF.platform.uiview.message.GFChatRoomView;

import android.content.Context;
import android.view.View;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFChatRoomViewFactory extends GFViewFactory {
    @Override
    public View createView(Context context) {
        return new GFChatRoomView(context);
    }
}
