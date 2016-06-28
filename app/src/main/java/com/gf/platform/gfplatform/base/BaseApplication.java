package com.GF.platform.gfplatform.base;

import android.app.Application;

import com.GF.platform.uikit.util.GFUtil;

import im.fir.sdk.FIR;

/**
 * Created by sunhaoyang on 2016/4/25.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GFUtil.frescoInit(this);
        FIR.init(this);
    }
}
