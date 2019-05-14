package com.lay.plugin.layplugin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class SecondActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Test test = new Test();
        TextView textView = new TextView(this);
        textView.setText(test.getText());
        setContentView(textView);
    }
}
