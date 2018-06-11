package com.taobao.weex.module;

import android.util.Log;
import android.widget.Toast;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by liyazhou on 2018/4/18.
 */

public class MyModule extends WXModule {
    //run ui thread
    @JSMethod(uiThread = true)
    public void printLog(String msg) {
        Toast.makeText(mWXSDKInstance.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @JSMethod
    public Object printLog2(int code, String msg) {
        Toast.makeText(mWXSDKInstance.getContext(),code + ":" + msg,Toast.LENGTH_SHORT).show();
        return true;
    }



}
