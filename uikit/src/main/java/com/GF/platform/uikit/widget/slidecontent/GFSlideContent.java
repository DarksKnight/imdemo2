package com.GF.platform.uikit.widget.slidecontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by sunhaoyang on 2016/4/26.
 */
public class GFSlideContent extends RelativeLayout {

    private boolean isOpen = false;

    public GFSlideContent(Context context) {
        super(context);
    }

    public GFSlideContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GFSlideContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void slideIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isOpen) {
            return true;
        } else {
            return false;
        }
    }
}
