package com.lay.pluge.pluginlib;

import android.app.Activity;
import android.content.Entity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class ActivityImp {
    protected Activity activity;
    protected PluginPackage pluginPackage;
    protected View mView;

    public void init(Activity activity, PluginPackage pluginPackage){
        this.activity = activity;
        this.pluginPackage = pluginPackage;
    }

    public void onCreate(Bundle savedInstanceState){

    }

    public void onStart(){

    }

    public void onRestart(){

    }
    public void onPause(){

    }
    public void onStop(){

    }
    public void onDestroy(){

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void setContentView(int resId){
        XmlResourceParser parser = pluginPackage.resources.getLayout(resId);
        View view = LayoutInflater.from(activity).inflate(parser, null);
        setContentView(view);
    }

    public void setContentView(View view){
        mView = view;
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity.addContentView(mView, lp);
    }

    public void startActivity(Class<? extends Activity> clz, Map<String, Object> params){
        startActivity(clz.getName(), params);
    }

    public void startActivity(String activityName, Map<String, Object> params){
        Class clz = null;
        try {
            clz = Class.forName(activityName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Intent i = new Intent(activity, clz);
        if(params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (Constans.PACKAGE_NAME.equals(entry.getKey()) || Constans.ACTIVITY_NAME.equals(entry.getKey())) {
                    continue;
                }
                Object obj = entry.getValue();
                if (obj instanceof String) {
                    i.putExtra(entry.getKey(), (String) entry.getValue());
                } else if (obj instanceof Integer) {
                    i.putExtra(entry.getKey(), (Integer) entry.getValue());
                } else if (obj instanceof Long) {
                    i.putExtra(entry.getKey(), (Long) entry.getValue());
                } else if (obj instanceof Double) {
                    i.putExtra(entry.getKey(), (Double) entry.getValue());
                } else if (obj instanceof Float) {
                    i.putExtra(entry.getKey(), (Float) entry.getValue());
                } else if (obj instanceof CharSequence) {
                    i.putExtra(entry.getKey(), (CharSequence) entry.getValue());
                } else if (obj instanceof Serializable) {
                    i.putExtra(entry.getKey(), (Serializable) entry.getValue());
                }
            }
        }
        activity.startActivity(i);
    }

    public void startPluginActivity(Class<? extends ActivityImp> clz, Map<String, Object> params){
        String activityName = clz.getName();
        startPluginActivity(activityName, params);
    }

    public void startPluginActivity(String activityName, Map<String, Object> params){
        Intent i = new Intent(activity, PluginProxyActivity.class);
        i.putExtra(Constans.PACKAGE_NAME, pluginPackage.packageInfo.packageName);
        i.putExtra(Constans.ACTIVITY_NAME, activityName);
        if(params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (Constans.PACKAGE_NAME.equals(entry.getKey()) || Constans.ACTIVITY_NAME.equals(entry.getKey())) {
                    continue;
                }
                Object obj = entry.getValue();
                if (obj instanceof String) {
                    i.putExtra(entry.getKey(), (String) entry.getValue());
                } else if (obj instanceof Integer) {
                    i.putExtra(entry.getKey(), (Integer) entry.getValue());
                } else if (obj instanceof Long) {
                    i.putExtra(entry.getKey(), (Long) entry.getValue());
                } else if (obj instanceof Double) {
                    i.putExtra(entry.getKey(), (Double) entry.getValue());
                } else if (obj instanceof Float) {
                    i.putExtra(entry.getKey(), (Float) entry.getValue());
                } else if (obj instanceof CharSequence) {
                    i.putExtra(entry.getKey(), (CharSequence) entry.getValue());
                } else if (obj instanceof Serializable) {
                    i.putExtra(entry.getKey(), (Serializable) entry.getValue());
                }
            }
        }
        activity.startActivity(i);
    }
}
