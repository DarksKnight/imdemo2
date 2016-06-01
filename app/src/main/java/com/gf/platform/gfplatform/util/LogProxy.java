package com.gf.platform.gfplatform.util;

import android.util.Log;

import com.gf.platform.gfplatform.BuildConfig;


/**
 * Created by sunhaoyang on 2016/3/17.
 */
public class LogProxy {

    private static String TAG = LogProxy.class.getSimpleName();
    public static boolean DEBUG = BuildConfig.GF_DEBUG;

    public static void i(String log) {
        if (DEBUG) {
            Log.i(TAG, log);
        }
    }
}
