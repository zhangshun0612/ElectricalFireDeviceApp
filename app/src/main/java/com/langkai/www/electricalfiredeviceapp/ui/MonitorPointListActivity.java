package com.langkai.www.electricalfiredeviceapp.ui;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.adapter.MonitorPointAdapter;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MonitorPointListActivity extends BaseActivity  {

    private String TAG = MonitorPointListActivity.class.getSimpleName();

    private Map<String, MonitorPoint> monitorPointMap;
    private List<String> mDataList;


    private MonitorPointAdapter mAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_point_list);

        mDataList = new ArrayList<>();
        mDataList.add("0001");
        mDataList.add("0002");
        mDataList.add("0003");
        mDataList.add("0004");
        mDataList.add("0005");
        mDataList.add("0006");
        mDataList.add("0007");
        mDataList.add("0008");


        monitorPointMap = new HashMap<>();
        monitorPointMap.put("0001" , new MonitorPoint("0001", "test1"));
        monitorPointMap.put("0002" ,new MonitorPoint("0002", "test2"));
        monitorPointMap.put("0003" ,new MonitorPoint("0003", "test3"));
        monitorPointMap.put("0004" ,new MonitorPoint("0004", "test4"));
        monitorPointMap.put("0005" ,new MonitorPoint("0005", "test5"));
        monitorPointMap.put("0006" ,new MonitorPoint("0006", "test6"));
        monitorPointMap.put("0007" ,new MonitorPoint("0007", "test7"));
        monitorPointMap.put("0008" ,new MonitorPoint("0008", "test8"));

        mAdapter = new MonitorPointAdapter(this, mDataList, monitorPointMap);

        recyclerView = findViewById(R.id.recycle_view);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(mAdapter);

    }

}
