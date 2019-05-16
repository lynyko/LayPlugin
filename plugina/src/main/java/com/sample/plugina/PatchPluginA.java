package com.sample.plugina;

import android.util.Log;

import com.lay.pluge.pluginlib.Patch;
import com.lay.pluge.pluginlib.PluginPackage;
import com.lay.plugin.layplugin.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PatchPluginA extends Patch {
    @Override
    public void patch(PluginPackage pluginPackage) {
        try {
            InputStream in = pluginPackage.assetManager.open("plugin.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            Log.d("PatchPlugin", "line:" + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
