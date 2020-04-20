package com.lay.plugin.layplugin;

import android.app.Application;
import android.os.Environment;

import com.lay.pluge.pluginlib.PluginManager;
import com.lay.pluge.pluginlib.PluginPackage;

public class App extends Application {
    private PluginPackage pluginPackage;
    @Override
    public void onCreate() {
        super.onCreate();
//        String dexPath = Environment.getExternalStorageDirectory() + "/plugina-debug.apk";
        PluginManager.getInstance(this).init();
    }

//    public PluginPackage getPluginPackage(){
//        return pluginPackage;
//    }
}
