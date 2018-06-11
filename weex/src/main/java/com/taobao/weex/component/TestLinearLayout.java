package com.taobao.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.taobao.weex.R;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by liyazhou on 2018/4/18.
 */

public class TestLinearLayout extends WXComponent<LinearLayout> {
    private static final String TAG = "TestLinearLayout";
    private LinearLayout linearLayout = null;
    public TestLinearLayout(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    @Override
    protected LinearLayout initComponentHostView(@NonNull Context context) {
        LayoutInflater mInflater= LayoutInflater.from(context);
        linearLayout = (LinearLayout)mInflater.inflate(R.layout.component_test_linearlayout, null);
        return linearLayout;
    }

    @WXComponentProp(name="text")
    public void setText(int text) {
        // getHostView返回的即为initComponentHostView返回的对象
        Log.e(TAG, "equal:" + linearLayout.equals(getHostView()));
        Button btn = (Button)getHostView().findViewById(R.id.btnText);
        btn.setText("" + text);

        // 获取控件的实际宽高可以通过 getLayoutWidth()、getLayoutHeight() 获取到
        Log.e(TAG, "WIDTH:" + getLayoutWidth() + " HEIGHT:" + getLayoutHeight());
    }

    @JSMethod
    public void update() {
        Log.e("haha", "update");
    }
}
