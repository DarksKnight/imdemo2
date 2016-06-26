package com.GF.platform.uiview.base;

import android.content.Context;
import android.view.View;

import com.GF.platform.uiview.factory.view.GFViewFactory;

/**
 * Created by sunhaoyang on 2016/6/26.
 */
public class GFViewCreator {

    private View view = null;
    private static GFViewCreator singleton = null;

    private GFViewCreator() {
    }

    public static GFViewCreator getDefault() {
        if (singleton == null) {
            synchronized (GFViewCreator.class) {
                if (singleton == null) {
                    singleton = new GFViewCreator();
                }
            }
        }
        return singleton;
    }

    public <E extends View> E makeView(Context context, GFViewFactory viewFactory) {
        view = viewFactory.createView(context);
        return (E)view;
    }
}
