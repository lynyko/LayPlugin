package com.lay.pluge.pluginlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

public class PluginProxyActivity extends Activity{
    String packageName;
    String activityName;
    PluginPackage pluginPackage;
    ActivityImp imp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("host", "PluginProxyActivity###onCreate");
        Intent intent = getIntent();
        if(intent == null){
            return;
        }
        packageName = intent.getStringExtra(Constans.PACKAGE_NAME);
        activityName = intent.getStringExtra(Constans.ACTIVITY_NAME);
        if(TextUtils.isEmpty(packageName) || TextUtils.isEmpty(activityName)){
            return;
        }
        pluginPackage = PluginManager.getInstance(this).getPluginByPackageName(packageName);
        if(pluginPackage == null){
            return;
        }

        imp = loadPluginActivity();
        if(imp == null){
            finish();
        } else {
            imp.init(this, pluginPackage);
        }
        imp.onCreate(savedInstanceState);
    }

    private ActivityImp loadPluginActivity(){
        try {
            Class cls = pluginPackage.classLoader.loadClass(activityName);
            Object obj = cls.newInstance();
            if(obj instanceof ActivityImp){
                return (ActivityImp) obj;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        imp.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        imp.onRestart();
    }

    @Override
    public void onPause() {
        super.onPause();
        imp.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        imp.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imp.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imp.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(imp.onKeyDown(keyCode, event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
