package com.taobao.weex.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taobao.weex.activity.WeexActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.taobao.weex.demo.R.layout.activity_main);

        TextView textView = (TextView)findViewById(com.taobao.weex.demo.R.id.tvGoWeex);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeexActivity.class));
            }
        });
    }
}
