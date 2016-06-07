package com.GF.platform.gfplatform.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import im.fir.sdk.FIR;

/**
 * Created by sunhaoyang on 2016/4/25.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        FIR.init(this);
    }
}
