package com.GF.platform.gfplatform.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 基类activity
 * Created by sunhaoyang on 2016/2/18.
 */
public abstract class BaseActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeContentView();
        setContentView(getContentView());
        obtainParam(getIntent());
        initView();
        initData();
        initListener();
    }

    protected void obtainParam(Intent intent){}

    protected void beforeContentView() {}

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
}
