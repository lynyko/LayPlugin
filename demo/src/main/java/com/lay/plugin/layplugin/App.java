package com.lay.plugin.layplugin;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.lay.pluge.pluginlib.Patch;
import com.lay.pluge.pluginlib.PluginManager;
import com.lay.pluge.pluginlib.PluginPackage;

public class App extends Application {
    private PluginPackage pluginPackage;
    @Override
    public void onCreate() {
        super.onCreate();
        String dexPath = Environment.getExternalStorageDirectory() + "/plugina-debug.apk";
        pluginPackage =  PluginManager.getInstance(this).loadApk(dexPath);
//                PluginPackage pluginPackage = PluginManager.getInstance(this).loadLocal();
        try {
            Class clz = pluginPackage.classLoader.loadClass("com.sample.plugina.PatchPluginA");
            Object obj = clz.newInstance();
            if(obj instanceof Patch) {
                Patch p = (Patch)obj;
                Log.d("PatchPluginA", "Patch");
                p.patch(pluginPackage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PluginPackage getPluginPackage(){
        return pluginPackage;
    }
}
