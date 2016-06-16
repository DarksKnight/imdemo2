package com.GF.platform.uikit.widget.chatkeyboard.base.entity;

import android.view.View;
import android.view.ViewGroup;

import com.GF.platform.uikit.widget.chatkeyboard.base.ports.GFPageViewInstantiateListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhaoyang on 2016/4/19.
 */
public class GFPageEntity<T extends GFPageEntity> implements GFPageViewInstantiateListener<T> {

    protected View mRootView;

    protected GFPageViewInstantiateListener mGFPageViewInstantiateListener;

    public enum DelBtnStatus {
        // 0,1,2
        GONE, FOLLOW, LAST;

        public boolean isShow() {
            return ! GONE.toString().equals(this.toString());
        }
    }

    /**
     * 表情数据源
     */
    private List<T> mEmoticonList = new ArrayList<>();
    /**
     * 每页行数
     */
    private int mLine;
    /**
     * 每页列数
     */
    private int mRow;
    /**
     * 删除按钮
     */
    private DelBtnStatus mDelBtnStatus;

    public List<T> getEmoticonList() {
        return mEmoticonList;
    }

    public void setEmoticonList(List<T> emoticonList) {
        this.mEmoticonList.addAll(emoticonList);
    }

    public int getLine() {
        return mLine;
    }

    public void setLine(int line) {
        this.mLine = line;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        this.mRow = row;
    }

    public DelBtnStatus getDelBtnStatus() {
        return mDelBtnStatus;
    }

    public void setDelBtnStatus(DelBtnStatus delBtnStatus) {
        this.mDelBtnStatus = delBtnStatus;
    }

    public void setIPageViewInstantiateItem(GFPageViewInstantiateListener GFPageViewInstantiateListener) { this.mGFPageViewInstantiateListener = GFPageViewInstantiateListener; }

    public View getRootView() {
        return mRootView;
    }

    public void setRootView(View rootView) {
        this.mRootView = rootView;
    }

    public GFPageEntity(){ }

    public GFPageEntity(View view){
        this.mRootView = view;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position, T pageEntity) {
        if(mGFPageViewInstantiateListener != null){
            return mGFPageViewInstantiateListener.instantiateItem(container, position, this);
        }
        return getRootView();
    }
}
