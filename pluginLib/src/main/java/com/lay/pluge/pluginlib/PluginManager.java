package com.lay.pluge.pluginlib;

import android.app.Activity;
import android.app.ActivityThread;
import android.app.Application;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class PluginManager {
    public Context mContext;
    private static PluginManager pluginManager = null;
    public Map<String, PluginPackage> packageMap = new HashMap<>();
    private PluginManager(Context context){
        mContext = context;
        hookInstrumentation();
    }

    public static PluginManager getInstance(Context context){
        if(pluginManager == null){
            synchronized (PluginManager.class){
                if(pluginManager == null){
                    pluginManager = new PluginManager(context);
                }
            }
        }
        return pluginManager;
    }

    public static PluginManager getInstance(){
        if(pluginManager == null){
            throw new IllegalArgumentException("PluginManager.getInstance(Context context) invoked first");
        }
        return pluginManager;
    }

    public PluginPackage loadApk(String path){
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES);

        String dexPath = mContext.getDir("plugin", Context.MODE_PRIVATE).getAbsolutePath();
        ClassLoader dexClassLoader = new DexClassLoader(path, dexPath, null, mContext.getClassLoader());
        AssetManager assetManager = createAssetManager(path);
        Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                mContext.getResources().getConfiguration());

        PluginPackage pluginPackage = new PluginPackage(dexClassLoader, assetManager, resources, packageInfo);
        packageMap.put(packageInfo.packageName, pluginPackage);
        return pluginPackage;
    }

    public PluginPackage getPluginByPackageName(String packageName){
        return packageMap.get(packageName);
    }

    private AssetManager createAssetManager(String path){
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            method.invoke(assetManager, path);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void hookInstrumentation(){
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        Instrumentation instrumentation = activityThread.getInstrumentation();
        Instrumentation lInstrumentation = new LInstrumentation(instrumentation);
        try {
            Field field = activityThread.getClass().getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            field.set(activityThread, lInstrumentation);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void startPlugin(Context context, String packageName){
        PluginPackage pluginPackage = packageMap.get(packageName);
        if(pluginPackage == null){
            throw new IllegalArgumentException("can not find a plugin");
        }
//        startApplicationIfNeed(pluginPackage);
        launchActivity(context, pluginPackage);
        registerReceiverIfNeed(context, pluginPackage);
    }

    private void registerReceiverIfNeed(Context context, PluginPackage pluginPackage){
        if(pluginPackage.packageInfo.receivers == null && pluginPackage.packageInfo.receivers.length > 0){
            return;
        }

        for(ActivityInfo receiver : pluginPackage.packageInfo.receivers){
            try {
                BroadcastReceiver r = (BroadcastReceiver)pluginPackage.classLoader.loadClass(receiver.name).newInstance();
//                context.registerReceiver(r, receiver.applicationInfo.)
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchActivity(Context context, PluginPackage pluginPackage){
        ComponentName componentName = new ComponentName(pluginPackage.packageName, pluginPackage.launchActivity);
        Intent intent = new Intent();
        intent.setComponent(componentName);
        context.startActivity(intent);
    }

    private void startApplicationIfNeed(PluginPackage pluginPackage){
        if(TextUtils.isEmpty(pluginPackage.applicationName)){
            return;
        }
        try {
            Class clz = Class.forName(pluginPackage.applicationName, false, pluginPackage.classLoader);
            Application pluginApp = (Application)clz.newInstance();
            pluginPackage.application = pluginApp;
            Field field = Application.class.getSuperclass().getDeclaredField("mResources");
            field.setAccessible(true);
            field.set(pluginApp, pluginPackage.resources);

            pluginApp.onCreate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
