package com.lay.pluge.pluginlib;

import android.app.Activity;

public class ProxyActivity extends Activity {
    private static String PROXY = null;

    public static String getProxyPackage() {
        return PROXY_PACKAGE;
    }

    public static void setProxyPackage(String proxyPackage) {
        PROXY_PACKAGE = proxyPackage;
    }

    private static String PROXY_PACKAGE = null;
    public static String getPROXY() {
        String p = PROXY;
        PROXY = null;
        return p;
    }

    public static void setPROXY(String PROXY) {
        ProxyActivity.PROXY = PROXY;
    }

}
