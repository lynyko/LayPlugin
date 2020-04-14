package com.sample.plugina;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.lay.plugin.commonlib.LActivity;

import java.io.IOException;
import java.io.InputStream;

public class PluginActivity extends LActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("plugin", "MainActivity###onCreate");
        setContentView(R.layout.mainlayout);
        try {
            InputStream in = getAssets().open("plugin.txt");
            byte[] b = new byte[in.available()];
            in.read(b);
            ((TextView)findViewById(R.id.tv)).setText(getResources().getString(R.string.tip) + ":" + new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        }

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ComponentName componentName = new ComponentName(packageName, PluginBActivity.class.getName());
//                Intent intent = new Intent();
//                intent.setComponent(componentName);
//                startActivity(intent);
                startActivity(new Intent(PluginActivity.this, PluginBActivity.class));
            }
        });

        findViewById(R.id.btn_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(PluginActivity.this, PluginService.class));
            }
        });
    }

    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
