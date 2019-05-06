package com.lay.pluge.pluginlib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

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
        DexClassLoader loader = new DexClassLoader(path, dexPath, null, mContext.getClassLoader());
        AssetManager assetManager = createAssetManager(path);
        Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                mContext.getResources().getConfiguration());
        PluginPackage pluginPackage = new PluginPackage(loader, assetManager, resources, packageInfo);
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

    public void startPluginActivity(String packageName, String activityName){
        Intent intent = new Intent(mContext, PluginProxyActivity.class);
        intent.putExtra(Constans.PACKAGE_NAME, packageName);
        intent.putExtra(Constans.ACTIVITY_NAME, activityName);
        mContext.startActivity(intent);
    }
}
