package com.GF.platform.uikit.widget.chatkeyboard.base.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.GF.platform.uikit.widget.chatkeyboard.util.KeyBoardUtil;

/**
 * Created by sunhaoyang on 2016/4/19.
 */
public class FuncLayout extends LinearLayout {

    public final int DEF_KEY = Integer.MIN_VALUE;
    private final SparseArray<View> mFuncViewArrayMap = new SparseArray<>();
    private int mCurrentFuncKey = DEF_KEY;
    protected int mHeight = 0;
    private OnFuncChangeListener onFuncChangeListener = null;

    public FuncLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void addFuncView(int key, View view) {
        if (mFuncViewArrayMap.get(key) != null) {
            return;
        }
        mFuncViewArrayMap.put(key, view);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, params);
        view.setVisibility(GONE);
    }

    public void hideAllFuncView() {
        for (int i = 0; i < mFuncViewArrayMap.size(); i++) {
            int keyTemp = mFuncViewArrayMap.keyAt(i);
            mFuncViewArrayMap.get(keyTemp).setVisibility(GONE);
        }
        mCurrentFuncKey = DEF_KEY;
        setVisibility(false);
    }

    public void toggleFuncView(int key, boolean isSoftKeyboardPop, EditText editText) {
        if (getCurrentFuncKey() == key) {
            if (isSoftKeyboardPop) {
                if(KeyBoardUtil.isFullScreen((Activity) getContext())){
                    KeyBoardUtil.closeSoftKeyboard(editText);
                } else {
                    KeyBoardUtil.closeSoftKeyboard(getContext());
                }
                LayoutParams params = (LayoutParams) getLayoutParams();
                params.height = mHeight;
                setLayoutParams(params);
            } else {
                KeyBoardUtil.openSoftKeyboard(editText);
            }
        } else {
            if (isSoftKeyboardPop) {
                if(KeyBoardUtil.isFullScreen((Activity) getContext())){
                    KeyBoardUtil.closeSoftKeyboard(editText);
                } else {
                    KeyBoardUtil.closeSoftKeyboard(getContext());
                }
            }
            showFuncView(key);
        }
    }

    public void showFuncView(int key) {
        if (mFuncViewArrayMap.get(key) == null) {
            return;
        }
        for (int i = 0; i < mFuncViewArrayMap.size(); i++) {
            int keyTemp = mFuncViewArrayMap.keyAt(i);
            if (keyTemp == key) {
                mFuncViewArrayMap.get(keyTemp).setVisibility(VISIBLE);
            } else {
                mFuncViewArrayMap.get(keyTemp).setVisibility(GONE);
            }
        }
        mCurrentFuncKey = key;
        setVisibility(true);

        if (onFuncChangeListener != null) {
            onFuncChangeListener.onFuncChange(mCurrentFuncKey);
        }
    }

    public int getCurrentFuncKey() {
        return mCurrentFuncKey;
    }

    public void setVisibility(boolean b) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (b) {
            setVisibility(VISIBLE);
            params.height = mHeight;
        } else {
            setVisibility(GONE);
            params.height = 0;
        }
        setLayoutParams(params);
    }

    public void updateHeight(int height) {
        this.mHeight = height;
    }

    public void setOnFuncChangeListener(OnFuncChangeListener listener) {
        this.onFuncChangeListener = listener;
    }

    public interface OnFuncChangeListener {
        void onFuncChange(int key);
    }
}
