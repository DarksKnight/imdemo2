package com.GF.platform.uiview.factory.view;

import com.GF.platform.uiview.messagelist.GFChatListView;

import android.content.Context;
import android.view.View;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFChatListViewFactory extends GFViewFactory {
    @Override
    public View createView(Context context) {
        return new GFChatListView(context);
    }
}
