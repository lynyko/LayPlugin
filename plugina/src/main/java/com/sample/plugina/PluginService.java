package com.sample.plugina;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class PluginService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("PLUGIN:" + getClass().getSimpleName() + "-->onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("PLUGIN:" + getClass().getSimpleName() + "-->onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
