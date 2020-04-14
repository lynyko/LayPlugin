package com.sample.plugina;

import android.app.Application;

public class PluginApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println(getClass() + "-->onCreate " + getResources().getString(R.string.application));
    }
}
