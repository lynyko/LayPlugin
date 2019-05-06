package com.sample.plugina;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lay.pluge.pluginlib.ActivityImp;
import com.lay.pluge.pluginlib.PluginProxyActivity;

public class MainActivity extends ActivityImp {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("plugin", "MainActivity###onCreate");
        setContentView(R.layout.mainlayout);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("plugin", "MainActivity###onClick");
                startPluginActivity(SecondActivity.class, null);
            }
        });
    }
}
