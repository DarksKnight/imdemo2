package com.GF.platform.uiview.factory.view;

import android.content.Context;
import android.view.View;

import com.GF.platform.uiview.message.GFChatRoomView;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFChatRoomViewFactory implements GFViewFactory {
    @Override
    public View createView(Context context) {
        return new GFChatRoomView(context);
    }
}
