package com.gf.platform.gfplatform.widget.swipeback;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.gf.platform.gfplatform.base.Global;

class SwipeBackView extends SlidingPaneLayout {
    public SwipeBackView(Context context) {
        super(context);
        ViewConfiguration config = ViewConfiguration.get(context);
        mEdgeSlop = config.getScaledEdgeSlop();
    }
    boolean disallowIntercept = false;
    boolean isEdgeMode = false;
    private float mInitialMotionX;
    private float mEdgeSlop;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_MOVE) {
            requestDisallowInterceptTouchEvent(disallowIntercept);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!Global.canBack) {
                    return false;
                }
                // The user should always be able to "close" the pane, so we only check
                // for child scrollability if the pane is currently closed.
                if (isEdgeMode && mInitialMotionX > mEdgeSlop) {
                    // How do we set super.mIsUnableToDrag = true?
                    // send the parent a cancel event
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onInterceptTouchEvent(cancelEvent);
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
