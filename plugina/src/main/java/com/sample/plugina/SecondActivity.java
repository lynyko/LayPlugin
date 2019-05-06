package com.sample.plugina;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lay.pluge.pluginlib.ActivityImp;

public class SecondActivity extends ActivityImp {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("plugin", "SecondActivity###onCreate");
        TextView tv = new TextView(activity);
        tv.setText("这是个插件第二个页面");
        setContentView(tv);
    }
}
