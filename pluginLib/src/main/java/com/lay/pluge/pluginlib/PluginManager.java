package com.lay.pluge.pluginlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class PluginManager {
    Context mContext;
    private static PluginManager pluginManager = null;
    private Map<String, PluginPackage> packageMap = new HashMap<>();

    private PluginManager(Context context){
        mContext = context;
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

    public PluginPackage loadApk(String path){
        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        String dexPath = mContext.getDir("plugin", Context.MODE_PRIVATE).getAbsolutePath();
        DexClassLoader dexClassLoader = new DexClassLoader(path, dexPath, null, mContext.getClassLoader().getParent());
        hook(mContext.getClassLoader(), dexClassLoader);
        AssetManager assetManager = createAssetManager(path);
        Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                mContext.getResources().getConfiguration());
        PluginPackage pluginPackage = new PluginPackage(dexClassLoader, assetManager, resources, packageInfo);
        packageMap.put(packageInfo.packageName, pluginPackage);
        return pluginPackage;
    }

    public void hook(ClassLoader pathClassLoader, ClassLoader dexClassLoader){
        Class clz = pathClassLoader.getClass().getSuperclass();
        while(clz != ClassLoader.class) {
            clz = clz.getSuperclass();
        }
        try {
            Field parent = clz.getDeclaredField("parent");
            parent.setAccessible(true);
            parent.set(pathClassLoader, dexClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PluginPackage loadLocal(){
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            AssetManager assetManager = mContext.getAssets();
            Resources resources = mContext.getResources();
            PluginPackage pluginPackage = new PluginPackage(mContext.getClassLoader(), assetManager, resources, packageInfo);
            packageMap.put(packageInfo.packageName, pluginPackage);
            return pluginPackage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public void startPluginActivity(Activity activity, String packageName, String activityName){
        Intent intent = new Intent(mContext, PluginProxyActivity.class);
        intent.putExtra(Constans.PACKAGE_NAME, packageName);
        intent.putExtra(Constans.ACTIVITY_NAME, activityName);
        activity.startActivity(intent);
    }
}
