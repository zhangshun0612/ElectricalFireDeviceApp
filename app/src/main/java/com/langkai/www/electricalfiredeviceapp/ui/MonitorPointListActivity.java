package com.langkai.www.electricalfiredeviceapp.ui;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.langkai.www.electricalfiredeviceapp.service.MqttService;
import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.adapter.MonitorPointAdapter;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointList;
import com.langkai.www.electricalfiredeviceapp.service.MqttServiceCallback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MonitorPointListActivity extends BaseActivity implements MqttServiceCallback, BaseRefreshListener {

    private String TAG = MonitorPointListActivity.class.getSimpleName();

    private Map<String, MonitorPoint> monitorPointMap;
    private List<String> mDataList;


    private MonitorPointAdapter mAdapter;

    private RecyclerView recyclerView;
    private PullToRefreshLayout pullToRefreshLayout;
    private LinearLayoutManager manager;

    private MqttService.MqttServiceBinder mBinder = null;
    private ServiceConnection mqqtServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (MqttService.MqttServiceBinder) service;
            mBinder.setMqttServiceCallback(MonitorPointListActivity.this);
            mBinder.connectIoTService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder.disconnectIoTService();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_point_list);

        pullToRefreshLayout = findViewById(R.id.pull_fresh_layout);
        pullToRefreshLayout.setRefreshListener(this);

        mDataList = new ArrayList<>();
        monitorPointMap = new HashMap<>();

        mAdapter = new MonitorPointAdapter(this, mDataList, monitorPointMap);

        recyclerView = findViewById(R.id.recycle_view);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(mAdapter);

        initService();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mqqtServiceConn);
    }

    private void initService(){

        Intent intent = new Intent(MonitorPointListActivity.this, MqttService.class);

        bindService(intent, mqqtServiceConn, BIND_AUTO_CREATE);
    }


    @Override
    public void monitorPointListUpdate(MonitorPointList list) {
        List<String> idList = list.getDeviceIds();

        for(int i = 0 ; i < idList.size(); i++){
            String id = idList.get(i);

            if(!mDataList.contains(id)){
                mDataList.add(id);
                monitorPointMap.put(id, new MonitorPoint(id, "未定义"));
            }
        }


        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        if(mBinder != null){
            mBinder.requestMpList();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.finishRefresh();
            }
        }, 2000);
    }

    @Override
    public void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.finishLoadMore();
            }
        }, 2000);

    }
}
