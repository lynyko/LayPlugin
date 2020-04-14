package com.lay.plugin.commonlib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class LActivity extends Activity {
    public String packageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && !TextUtils.isEmpty(bundle.getString(Constans.PACKAGE_NAME))){
            packageName = bundle.getString(Constans.PACKAGE_NAME);
        } else {
            packageName = getPackageName();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if(TextUtils.isEmpty(packageName)){
            super.startActivity(intent);
        } else {
            String targetName = intent.getComponent().getClassName();
            ComponentName componentName = new ComponentName(packageName, targetName);
            intent.setComponent(componentName);
            super.startActivity(intent);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if(!TextUtils.isEmpty(packageName)){
            ComponentName componentName = new ComponentName(getPackageName(), "com.lay.pluge.pluginlib.ProxyService");
            String target = service.getComponent().getClassName();
            service.setComponent(componentName);
            Bundle bundle = service.getExtras();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(Constans.PACKAGE_NAME, packageName);
            bundle.putString(Constans.SERVICE_NAME, target);
            service.putExtras(bundle);
        }
        return super.startService(service);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.i("PLUGIN", getClass().getSimpleName() + "-->attachBaseContext");
        super.attachBaseContext(newBase);
    }
}
