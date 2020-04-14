package com.lay.plugin.layplugin;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lay.pluge.pluginlib.PluginManager;
import com.lay.pluge.pluginlib.PluginPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    Context mContext;
    LinearLayout llContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        llContainer = findViewById(R.id.ll_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContainer.removeAllViews();
                List<PluginInfo> pluginInfoList = loadPlugins();
                for(final PluginInfo p : pluginInfoList){
                    View item = LayoutInflater.from(mContext).inflate(R.layout.layout_item, null);
                    llContainer.addView(item);
                    ((ImageView)item.findViewById(R.id.iv_icon)).setImageDrawable(p.icon);
                    ((TextView)item.findViewById(R.id.tv_title)).setText(p.name);
                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PluginPackage plugin =  PluginManager.getInstance(mContext).loadApk(p.path);
                            Log.i(TAG, "启动：[包名：" + plugin.packageInfo.packageName + "]");
                            Log.i(TAG, "\u3000\u3000\u3000[activity：" + plugin.packageInfo.activities[0].name + "]");
                            PluginManager.getInstance().startPlugin(mContext, plugin.packageName);
//                            startActivity(new Intent(mContext, SecondActivity.class));
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            String path = Environment.getExternalStorageDirectory() + "/test.txt";
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                String line = reader.readLine();
                while (line != null){
                    Log.d("MainActivity", line);
                    line = reader.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<PluginInfo> loadPlugins(){
        List<PluginInfo> pluginInfoList = new ArrayList<>();

        String dexDir = Environment.getExternalStorageDirectory() + "/plugins";
        File dir = new File(dexDir);
        if(!dir.exists()){
            return pluginInfoList;
        }
        String[] list = dir.list();
        for(String item : list){
            if(item.toLowerCase().endsWith(".apk")){
                String path = dexDir + "/" + item;
                PackageInfo packageInfo =  mContext.getPackageManager().getPackageArchiveInfo(path, 0);
                PluginInfo pluginInfo = new PluginInfo();
                int nameRes = packageInfo.applicationInfo.labelRes;
                AssetManager assetManager = createAssetManager(path);
                Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                        mContext.getResources().getConfiguration());
                pluginInfo.path = path;
                pluginInfo.name = resources.getString(nameRes);
                pluginInfo.icon = resources.getDrawable(packageInfo.applicationInfo.icon);
                pluginInfo.packageName = packageInfo.applicationInfo.packageName;
                pluginInfoList.add(pluginInfo);
            }
        }
        return pluginInfoList;
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
}
