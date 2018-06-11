package com.taobao.weex.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.R;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.URIAdapter;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.net.URI;

/**
 * Created by liyazhou on 2017/12/25.
 */

public class WeexActivity extends AppCompatActivity {
    private static final String TAG = "WeexActivity";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0x0002;
    WXSDKInstance mWXSDKInstance;
    Uri mUri;

    private String realUrl = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_scan, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weex_activity_main);

        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.onActivityCreate();

        mWXSDKInstance.registerRenderListener(new RenderListener());
        WXSDKEngine.setActivityNavBarSetter(new NavigatorAdapter());
        this.loadJs();

        requestPermission();
    }


    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "no permission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请先申请权限", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }
    /**
     * 加载JS文件函数
     */
    private void loadJs() {
        mUri = getIntent().getData();
        if (mUri == null) {
            mWXSDKInstance.render("home/index.weex.js", WXFileUtils.loadAsset("home/index.weex.js", getApplicationContext()), null, null, WXRenderStrategy.APPEND_ASYNC);
        } else {
            Log.e(TAG, mUri.toString());
            Uri rewrited = mWXSDKInstance.getURIAdapter().rewrite(mWXSDKInstance, URIAdapter.BUNDLE, mUri);

            if (rewrited.getScheme().equals(Constants.Scheme.LOCAL)) {
                Log.e(TAG, "url:" + rewrited.getPath());
                mWXSDKInstance.render(rewrited.getPath().substring(1), WXFileUtils.loadAsset(rewrited.getPath().substring(1), getApplicationContext()), null, null, WXRenderStrategy.APPEND_ASYNC);
            } else if (rewrited.getScheme().equals(Constants.Scheme.HTTP) || rewrited.getScheme().equals(Constants.Scheme.HTTPS)){
                // 接入DynamicConfig
                //WeexCrashListener.url = realUrl;
                mWXSDKInstance.renderByUrl(mUri.toString(), mUri.toString(), null, null, WXRenderStrategy.APPEND_ASYNC);
            }
        }
    }

    private void handleScanCodeResult(String result) {
        Uri mUri = Uri.parse(result);
        Log.e(TAG, "*******" + mUri.toString());
        if (mUri == null) {
            Log.e(TAG, "scan result null, return");
        } else {
            if (mUri.getQueryParameterNames().contains("_wx_devtool")) {
                WXEnvironment.sDebugServerConnectable = true;
                WXEnvironment.sRemoteDebugProxyUrl = mUri.getQueryParameter("_wx_devtool");
                WXSDKEngine.reload();
            } else if (mUri.getQueryParameterNames().contains("_wx_tpl")) {
                String url = mUri.toString().substring(mUri.toString().indexOf("_wx_tpl=") + "_wx_tpl=".length());
                Intent activityIntent = new Intent(WeexActivity.this, WeexActivity.class);
                Log.e("haha", url);
                activityIntent.setData(Uri.parse(url));
                activityIntent.setAction("com.taobao.android.intent.action.WEEX");
                startActivity(activityIntent);
            } else {
                Intent activityIntent = new Intent(WeexActivity.this, WeexActivity.class);
                activityIntent.setData(mUri);
                activityIntent.setAction("com.taobao.android.intent.action.WEEX");
                startActivity(activityIntent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //WeexCrashListener.url = realUrl;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityPause();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityDestroy();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_scan) {
            Log.e(TAG, "scan");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "no permission");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "please give me the permission", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            } else {
                Log.e(TAG, "got permission");
                //startActivity(new Intent(this, CaptureActivity.class));
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setPrompt("请扫描"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setBeepEnabled(false); //扫描成功的「哔哔」声，默认开启
                integrator.setCaptureActivity(ScanActivity.class);
                integrator.initiateScan();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d(TAG, "Cancelled");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // 获取到扫码结果后跳转
                Log.d(TAG, "Scanned: " + result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                handleScanCodeResult(result.getContents());
            }
        }

        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private static class NavigatorAdapter implements IActivityNavBarSetter {

        @Override
        public boolean push(String param) {
            Log.e(TAG, "push:" + param);
            return false;
        }

        @Override
        public boolean pop(String param) {
            Log.e(TAG, "pop:" + param);
            return false;
        }

        @Override
        public boolean setNavBarRightItem(String param) {
            return false;
        }

        @Override
        public boolean clearNavBarRightItem(String param) {
            return false;
        }

        @Override
        public boolean setNavBarLeftItem(String param) {
            return false;
        }

        @Override
        public boolean clearNavBarLeftItem(String param) {
            return false;
        }

        @Override
        public boolean setNavBarMoreItem(String param) {
            return false;
        }

        @Override
        public boolean clearNavBarMoreItem(String param) {
            return false;
        }

        @Override
        public boolean setNavBarTitle(String param) {
            return false;
        }
    }

    private  class RenderListener implements IWXRenderListener {

        @Override
        public void onViewCreated(WXSDKInstance instance, View view) {
            setContentView(view);
        }

        @Override
        public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        }

        @Override
        public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

        }

        @Override
        public void onException(WXSDKInstance instance, String errCode, String msg) {
            if (instance != null && mUri != null) {
                try {
                } catch (Throwable e) {
                    //Return
                }
            }
        }
    }
}
