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
        Log.d("PatchPluginA", "patch()");
        ClassLoader classLoader = pluginPackage.classLoader;
        try {
            classLoader.loadClass("com.lay.plugin.layplugin.Test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Log.d("PatchPluginA", "assetManager");
            InputStream in = pluginPackage.assetManager.open("plugin.txt");
            Log.d("PatchPluginA", in.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            Log.d("PatchPluginA", reader.toString());
            String line = reader.readLine();
            Log.d("PatchPluginA", line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
