package com.lay.pluge.pluginlib;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.lay.plugin.commonlib.Constans;

import java.util.HashMap;
import java.util.Map;

public class ProxyService extends Service {
    private Map<String, Service> map = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("PLUGIN", getClass().getSimpleName() + "-->onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PLUGIN", getClass().getSimpleName() + "-->onStartCommand");
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return super.onStartCommand(intent, flags, startId);
        }
        String packageName = bundle.getString(Constans.PACKAGE_NAME);
        String target = bundle.getString(Constans.SERVICE_NAME);
        Service service = map.get(target);
        if(service != null){
            return service.onStartCommand(intent, flags, startId);
        }
        if(TextUtils.isEmpty(target)){
            return super.onStartCommand(intent, flags, startId);
        }
        PluginPackage pluginPackage = PluginManager.getInstance().packageMap.get(packageName);
        if(pluginPackage == null){
            return super.onStartCommand(intent, flags, startId);
        }
        if(!pluginPackage.serviceList.contains(target)){
            return super.onStartCommand(intent, flags, startId);
        }
        try {
            service = (Service)pluginPackage.classLoader.loadClass(target).newInstance();
            map.put(target, service);
            service.onCreate();
            return service.onStartCommand(intent, flags, startId);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
