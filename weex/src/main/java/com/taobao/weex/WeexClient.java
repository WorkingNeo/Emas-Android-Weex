package com.taobao.weex;

import android.app.Application;

import com.taobao.weex.adapter.ImageAdapter;
import com.taobao.weex.common.WXException;
import com.taobao.weex.component.MyComponent;
import com.taobao.weex.component.RichText;
import com.taobao.weex.component.TestLinearLayout;
import com.taobao.weex.module.MyModule;
import com.taobao.weex.module.TestModule;

import org.asialee.weex.component.WXLottie;


/**
 * Created by liyazhou on 2017/12/25.
 */

public class WeexClient {
    public static void initWeex(Application application, String appkey, String appversion, String prefix) {
        if (application != null) {

            // 自定义相关配置
            InitConfig config=new InitConfig.Builder()
                    .setImgAdapter(new ImageAdapter()) // 自定义图片适配器
                    .build();
            WXSDKEngine.initialize(application,config);

            // register module and component
            try {
                WXSDKEngine.registerComponent("richtext", RichText.class);
                WXSDKEngine.registerComponent("lottie", WXLottie.class);
                WXSDKEngine.registerComponent("testlinearlayout", TestLinearLayout.class);
                WXSDKEngine.registerComponent("mycomponent", MyComponent.class);

                WXSDKEngine.registerModule("testmodule", TestModule.class);
                WXSDKEngine.registerModule("MyModule", MyModule.class);


            } catch (WXException e) {
                e.printStackTrace();
            }

        }
    }
}
