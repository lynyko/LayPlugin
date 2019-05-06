package com.lay.plugin.layplugin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lay.pluge.pluginlib.PluginManager;
import com.lay.pluge.pluginlib.PluginPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String dexPath = Environment.getExternalStorageDirectory() + "/plugina-debug.apk";
                PluginManager pluginManager = PluginManager.getInstance(getApplicationContext());
                PluginPackage pluginPackage = pluginManager.loadApk(dexPath);
                pluginManager.startPluginActivity(pluginPackage.packageInfo.packageName, "com.sample.plugina.MainActivity");
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
}
