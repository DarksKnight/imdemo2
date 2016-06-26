package com.GF.platform.uiview.factory.view;

import android.content.Context;
import android.view.View;

import com.GF.platform.uiview.messagelist.GFChatListView;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFChatListViewFactory implements GFViewFactory {
    @Override
    public View createView(Context context) {
        return new GFChatListView(context);
    }
}
