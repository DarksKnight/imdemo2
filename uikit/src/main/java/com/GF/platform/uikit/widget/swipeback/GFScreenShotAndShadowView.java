package com.GF.platform.uikit.widget.swipeback;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;

class GFScreenShotAndShadowView extends ViewGroup {
    public ImageView imgView;
    public GFShadowView GFShadowView;
    public ImageView imgViewHover;

    public GFScreenShotAndShadowView(Context context) {
        super(context);
        imgView = new ImageView(context);
        GFShadowView = new GFShadowView(context);
        imgViewHover = new ImageView(context);
        imgViewHover.setBackgroundColor(Color.BLACK);
        imgViewHover.setAlpha(0.0f);
        imgViewHover.setVisibility(GONE);
        addView(imgView);
        addView(GFShadowView);
        addView(imgViewHover);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        imgView.layout(i, i1, i2, i3);
        GFShadowView.layout(i, i1, i2, i3);
        imgViewHover.layout(i, i1, i2, i3);
    }
}
