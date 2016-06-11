package com.GF.platform.uikit.util;

import android.util.Log;



/**
 * Created by sunhaoyang on 2016/3/17.
 */
public class LogProxy {

    private static String TAG = LogProxy.class.getSimpleName();
    public static boolean DEBUG = true;

    public static void i(String log) {
        if (DEBUG) {
            Log.i(TAG, log);
        }
    }
}
