package com.example.jingzhao.addressthreelinkageapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CityNameActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProvinceAdapter adapter;
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
    private String province_ID, city_ID, province_name, city_name, province_name_one;
    private CitycodeUtil citycodeUtil;
    private boolean isCityName = true;
    private List<Cityinfo> list_city;
    BroadcastReceiver MsgReceiver = null;
    public static String BROADCAST_CHANGE_STATE = "InformAndValue";
    private String addressDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_name);
        Intent intent = getIntent();
        province_ID = intent.getStringExtra("province_ID");
        province_name = intent.getStringExtra("province_name");

        city_ID = intent.getStringExtra("city_ID");
        province_name_one = intent.getStringExtra("pro_name");
        city_name = intent.getStringExtra("city_name");
        isCityName = intent.getBooleanExtra("isCityName", true);
        registerBroadcastPickStyle();
        initView();

        ((TextView) findViewById(R.id.title_name)).setText("选择地址");
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
        getaddressinfo();

    }

    private void getaddressinfo() {
        // TODO Auto-generated method stub
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(this, "area_last.json");
        if (isCityName) {
            city_map = parser.getJSONParserResultArray(area_str, "area1");
            citycodeUtil = CitycodeUtil.getSingleton();
            list_city = city_map.get(province_ID);
            adapter = new ProvinceAdapter(list_city);
            recyclerView.setAdapter(adapter);
        } else {
            couny_map = parser.getJSONParserResultArray(area_str, "area2");
            citycodeUtil = CitycodeUtil.getSingleton();
            list_city = couny_map.get(city_ID);
            adapter = new ProvinceAdapter(list_city);
            recyclerView.setAdapter(adapter);
        }
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isCityName) {
                    Intent intent = new Intent(CityNameActivity.this, CityNameActivity.class);
                    intent.putExtra("city_ID", list_city.get(position).getId());
                    intent.putExtra("city_name", list_city.get(position).getCity_name());
                    intent.putExtra("pro_name", province_name);
                    intent.putExtra("isCityName", false);
                    startActivity(intent);
                } else {
//跳转到要赋值的页面，将要赋的值传到这个页面，关闭这个页面
                    Intent intent = new Intent();
                    intent.putExtra("Address", province_name_one + " " + city_name + " " + list_city.get(position).getCity_name());
                    intent.putExtra("province_three_level_Name", province_name_one);
                    intent.putExtra("city_three_level_Name", city_name);
                    intent.putExtra("area_three_level_Name", list_city.get(position).getCity_name());
                    intent.setAction("InformAndValue");
                    sendBroadcast(intent);
                    finish();
                }


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

    public class JSONParser {

        public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
                String JSONString, String key) {
            HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator<?> iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
                        .next();
                List<Cityinfo> list = new ArrayList<Cityinfo>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
                            .getAsString());
                    cityinfo.setId(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    list.add(cityinfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

}
