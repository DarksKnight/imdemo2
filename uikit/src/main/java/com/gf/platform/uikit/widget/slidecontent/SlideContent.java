package com.gf.platform.uikit.widget.slidecontent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by sunhaoyang on 2016/4/26.
 */
public class SlideContent extends RelativeLayout {

    private boolean isOpen = false;

    public SlideContent(Context context) {
        super(context);
    }

    public SlideContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideContent(Context context, AttributeSet attrs, int defStyleAttr) {
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
