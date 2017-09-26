package com.example.jingzhao.addressthreelinkageapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tv_test_three_level;
    BroadcastReceiver MsgReceiver = null;
    public static String BROADCAST_CHANGE_STATE = "InformAndValue";
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerBroadcastPickStyle();
        tv_test_three_level = (TextView) findViewById(R.id.tv_test_three_level);
        tv_test_three_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProvinceActivity.class));
            }
        });
    }

    /*接受三级联动传过来的值*/
    private void registerBroadcastPickStyle() {
        if (MsgReceiver == null) {
            MsgReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    address = intent.getStringExtra("Address");
                    if (address != null) {
                        tv_test_three_level.setText(address);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_CHANGE_STATE);
            registerReceiver(MsgReceiver, intentFilter);
        }

    }
}
