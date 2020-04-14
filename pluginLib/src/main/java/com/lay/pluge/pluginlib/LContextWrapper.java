package com.lay.pluge.pluginlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

public class LContextWrapper extends ContextWrapper {
    Context mBase;
    public LContextWrapper(Context base) {
        super(base);
        mBase = base;
    }

    @Override
    public ComponentName startService(Intent service) {
        System.out.println("PLUGIN : " + getClass().getSimpleName() + "-->startService");
        return mBase.startService(service);
    }


}
