package com.lay.pluge.pluginlib;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class PluginPackage {
    public ClassLoader classLoader = null;
    public AssetManager assetManager = null;
    public Resources resources = null;
    public PackageInfo packageInfo = null;
    public List<String> activityList = new ArrayList<>();
    public List<String> serviceList = new ArrayList<>();
    public List<String> receiverList = new ArrayList<>();
    public String packageName = null;
    // 为空表示不用调用application，不为空则要调用application,调用后applicationName被清空
    public String applicationName = null;
    public Application application = null;
    public String launchActivity = null;

    public PluginPackage(ClassLoader classLoader, AssetManager assetManager, Resources resources, PackageInfo packageInfo){
        this.classLoader = classLoader;
        this.assetManager = assetManager;
        this.resources = resources;

        if(packageInfo.activities == null || packageInfo.activities.length == 0){
            throw new IllegalArgumentException("未发现要打开的activity");
        }
        for(ActivityInfo info : packageInfo.activities){
            activityList.add(info.name);
        }
        if(packageInfo.services != null && packageInfo.services.length >= 0) {
            for (ServiceInfo info : packageInfo.services) {
                serviceList.add(info.name);
            }
        }
        if(packageInfo.receivers != null && packageInfo.receivers.length >= 0) {
            for (ActivityInfo info : packageInfo.receivers) {
                receiverList.add(info.name);
            }
        }
        launchActivity = activityList.get(0);
        this.packageInfo = packageInfo;
        packageName = packageInfo.packageName;
        applicationName = packageInfo.applicationInfo.className;
    }
}
