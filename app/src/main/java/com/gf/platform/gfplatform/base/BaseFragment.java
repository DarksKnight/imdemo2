package com.GF.platform.gfplatform.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基类Fragment
 * Created by sunhaoyang on 2016/2/22.
 */
public abstract class BaseFragment extends Fragment {

    protected View mView = null;
    protected DealProxy dealProxy = DealProxy.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getContentView(), container, false);
        initView();
        initData();
        initListener();
        return mView;
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    public final <E extends View> E getView(int id) {
        try {
            return (E) mView.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
}
