package com.lay.pluge.pluginlib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

public class PluginPackage {
    public DexClassLoader classLoader = null;
    public AssetManager assetManager = null;
    public Resources resources = null;
    public PackageInfo packageInfo = null;

    public PluginPackage(DexClassLoader classLoader, AssetManager assetManager, Resources resources, PackageInfo packageInfo){
        this.classLoader = classLoader;
        this.assetManager = assetManager;
        this.resources = resources;
        this.packageInfo = packageInfo;
    }
}
