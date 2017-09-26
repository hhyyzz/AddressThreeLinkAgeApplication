package com.example.jingzhao.addressthreelinkageapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by jingzhao on 2017/9/15.
 */
public class ProvinceActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProvinceAdapter adapter;
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private Cityinfo cityinfo;
    BroadcastReceiver MsgReceiver = null;
    public static String BROADCAST_CHANGE_STATE = "InformAndValue";
    private String addressDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        getaddressinfo();
        registerBroadcastPickStyle();
        initView();
        ((TextView)findViewById(R.id.title_name)).setText("选择地址");
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ProvinceAdapter(province_list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ProvinceActivity.this, CityNameActivity.class);
                intent.putExtra("province_ID", province_list.get(position).getId());
                intent.putExtra("province_name", province_list.get(position).getCity_name());
                startActivity(intent);
            }
        });

    }

    /*接受三级联动传过来的值*/
    private void registerBroadcastPickStyle() {
        if (MsgReceiver == null) {
            MsgReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    addressDetail = intent.getStringExtra("Address");

                    if ("null" != addressDetail) {
                        finish();
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_CHANGE_STATE);
            registerReceiver(MsgReceiver, intentFilter);
        }

    }

    private void getaddressinfo() {
        // TODO Auto-generated method stub
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(this, "area_last.json");
        parser.getJSONParserResult(area_str, "area0");
    }

    public class JSONParser {

        public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator<?> iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
                        .next();
                cityinfo = new Cityinfo();

                cityinfo.setCity_name(entry.getValue().getAsString());
                cityinfo.setId(entry.getKey());
                province_list.add(cityinfo);
            }
            return province_list;
        }
    }

}
