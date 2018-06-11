package com.taobao.weex.demo;

import android.app.Application;

import com.taobao.weex.BuildConfig;
import com.taobao.weex.WeexClient;

/**
 * Created by liyazhou on 2017/12/26.
 */

public class WXApplication extends Application {
    private static final String appkey = "10000000";
    private static final String prefix = "prefix";
    @Override
    public void onCreate() {
        super.onCreate();
        WeexClient.initWeex(this, appkey, BuildConfig.VERSION_NAME, prefix);
    }
}
