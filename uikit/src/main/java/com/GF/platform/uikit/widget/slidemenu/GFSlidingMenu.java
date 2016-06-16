package com.GF.platform.uikit.widget.slidemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.GF.platform.uikit.R;
import com.GF.platform.uikit.util.GFUtil;
import com.GF.platform.uikit.widget.slidecontent.GFSlideContent;
import com.nineoldandroids.view.ViewHelper;

public class GFSlidingMenu extends HorizontalScrollView {
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * dp
     */
    private int mMenuRightPadding;
    /**
     * 菜单的宽度
     */
    private int mMenuWidth;
    private int mHalfMenuWidth;
    private boolean isOpen;
    private float mPrevX = 0;
    private GFSlideContent GFSlideContent = null;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    private boolean once;

    private ViewGroup mMenu;
    private ViewGroup mContent;

    public GFSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GFSlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScreenWidth = GFUtil.getScreenWidth(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.GFSlidingMenu, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.GFSlidingMenu_rightPadding) {
                // 默认50
                mMenuRightPadding = a.getDimensionPixelSize(attr,
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 50f,
                                getResources().getDisplayMetrics()));// 默认为10DP
            }
        }
        a.recycle();
    }

    public GFSlidingMenu(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 显示的设置一个宽度
         */
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) wrapper.getChildAt(0);
            mContent = (ViewGroup) wrapper.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            mHalfMenuWidth = mMenuWidth / 2;
            mMenu.getLayoutParams().width = mMenuWidth;
            mContent.getLayoutParams().width = mScreenWidth;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            once = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                GFSlideContent.slideIsOpen(isOpen);
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float eventX = ev.getX();
                if (mPrevX > eventX) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        float leftScale = 1 - 0.3f * scale;
        float rightScale = 0.8f + scale * 0.2f;

        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, (1 - scale));
        ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);
    }

    public void setGFSlideContent(GFSlideContent GFSlideContent) {
        this.GFSlideContent = GFSlideContent;
    }
}