package com.lay.pluge.pluginlib;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.lay.plugin.commonlib.Constans;

import java.lang.reflect.Field;

public class LInstrumentation extends Instrumentation {
    Instrumentation mInstrumentation;
    public LInstrumentation(Instrumentation instrumentation){
        mInstrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        hookActivity(intent);
        ActivityResult ar = mInstrumentation.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
        return ar;
    }

    private void hookActivity(Intent intent){
        String packageName = intent.getComponent().getPackageName();
        String targetName = intent.getComponent().getClassName();
        PluginPackage pluginPackage = PluginManager.getInstance().packageMap.get(packageName);
        if(pluginPackage == null){
            return;
        }
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            bundle = new Bundle();
        }
        ComponentName componentName = new ComponentName(PluginManager.getInstance().mContext.getPackageName(),
                "com.lay.pluge.pluginlib.ProxyActivity");
        intent.setComponent(componentName);
        String launchActivity = pluginPackage.activityList.contains(targetName) ? targetName : pluginPackage.activityList.get(0);
        bundle.putString(Constans.PACKAGE_NAME, packageName);
        bundle.putString(Constans.ACTIVITY_NAME, launchActivity);
        intent.putExtras(bundle);
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            return mInstrumentation.newActivity(cl, className, intent);
        }
        String packageName = bundle.getString(Constans.PACKAGE_NAME);
        String activityName = bundle.getString(Constans.ACTIVITY_NAME);
        final PluginPackage pluginPackage = PluginManager.getInstance().packageMap.get(packageName);
        if(pluginPackage == null){
            return mInstrumentation.newActivity(cl, className, intent);
        }
        Activity activity = mInstrumentation.newActivity(pluginPackage.classLoader, activityName, intent);
        try {
            Field field = Activity.class.getSuperclass().getDeclaredField("mResources");
            field.setAccessible(true);
            field.set(activity, pluginPackage.resources);

            field = Activity.class.getDeclaredField("mApplication");
            field.setAccessible(true);
            field.set(activity, pluginPackage.application);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return activity;
    }

//    public Application newApplication(ClassLoader cl, String className, Context context)
//            throws InstantiationException, IllegalAccessException,
//            ClassNotFoundException {
//        Log.i("PLUGIN", getClass().getSimpleName() + "-->newApplication");
//        return mInstrumentation.newApplication(cl, className, context);
//    }
}
