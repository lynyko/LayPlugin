package com.sample.plugina;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PLUGIN", getClass().getSimpleName() + "-->onReceive");
    }
}
