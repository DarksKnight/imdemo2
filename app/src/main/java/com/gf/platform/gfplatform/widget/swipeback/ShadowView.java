package com.gf.platform.gfplatform.widget.swipeback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.gf.platform.gfplatform.R;

class ShadowView extends View {
    private static int SHADOW_WIDTH = 0;

    public ShadowView(Context context) {
        super(context);
        SHADOW_WIDTH = (int)context.getResources().getDimension(R.dimen.gf_10dp);
    }

    private Drawable leftShadow;
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        int right = getWidth();
        int left = right - SHADOW_WIDTH;
        int top = 0;
        int bot = getHeight();
        getLeftShadow().setBounds(left, top, right, bot);
        leftShadow.draw(canvas);
        canvas.restore();
    }

    private Drawable getLeftShadow() {
        if (leftShadow == null) {
            leftShadow = getResources().getDrawable(R.mipmap.bjmgf_shadow);
        }
        return leftShadow;
    }
}
