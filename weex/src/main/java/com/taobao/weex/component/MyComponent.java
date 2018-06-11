package com.taobao.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;

/**
 * Created by liyazhou on 2018/4/18.
 */

public class MyComponent extends WXComponent {
    public MyComponent(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
    }

    @Override
    protected View initComponentHostView(@NonNull Context context) {
        TextView view = new TextView(context);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText("my component");
        return view;
    }

    @JSMethod
    public void focus() {
        Toast.makeText(getContext(), "focus", Toast.LENGTH_SHORT).show();
    }
}
