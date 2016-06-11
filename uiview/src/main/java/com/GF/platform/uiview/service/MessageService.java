package com.GF.platform.uiview.service;

import com.GF.platform.uikit.util.LogProxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 消息服务
 * Created by apple on 16/6/11.
 */

public class MessageService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LogProxy.i("onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogProxy.i("onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
