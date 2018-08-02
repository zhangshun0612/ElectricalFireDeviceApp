package com.langkai.www.electricalfiredeviceapp.ui;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;
import com.langkai.www.electricalfiredeviceapp.service.MqttService;
import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.adapter.MonitorPointAdapter;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointList;
import com.langkai.www.electricalfiredeviceapp.service.MqttServiceCallback;
import com.langkai.www.electricalfiredeviceapp.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MonitorPointListActivity extends BaseActivity implements MqttServiceCallback, BaseRefreshListener {

    private String TAG = MonitorPointListActivity.class.getSimpleName();

    private Map<String, MonitorPoint> monitorPointMap;
    private List<String> mDataList;

    private boolean isRefreshing = false;
    private boolean isShown = false;

    private MonitorPointAdapter mAdapter = null;

    private RecyclerView recyclerView;
    private PullToRefreshLayout pullToRefreshLayout;
    private LinearLayoutManager manager;

    private MqttService.MqttServiceBinder mBinder = null;
    private ServiceConnection mqttServiceConn = new ServiceConnection() {
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
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        initService();

        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShown = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShown = true;

        if(mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mqttServiceConn);
    }

    private void initService(){

        Intent intent = new Intent(MonitorPointListActivity.this, MqttService.class);

        bindService(intent, mqttServiceConn, BIND_AUTO_CREATE);
    }

    private void initData(){
        for(int i = 1 ; i < 50; i++){
            String id =  String.format(Locale.ENGLISH,"%05d", i);
            mDataList.add(id);

            MonitorPoint mp = new MonitorPoint(id, "未定义");
            mp.addMonitorPointChannel(1, new MonitorPointChannel(1, Constant.FUNCTION_RESIDUAL_CURRENT));
            mp.addMonitorPointChannel(2, new MonitorPointChannel(2, Constant.FUNCTION_RUNNING_CURRENT));
            mp.addMonitorPointChannel(3, new MonitorPointChannel(3, Constant.FUNCTION_RUNNING_VOLT));
            mp.addMonitorPointChannel(4, new MonitorPointChannel(4, Constant.FUNCTION_TEMPERATURE));
            /*
            for(int ch = 2; ch <= 3; ch++){
                mp.addMonitorPointChannel(ch, new MonitorPointChannel(ch));
            }
            */
            monitorPointMap.put(id, mp);
        }

        mAdapter.notifyDataSetChanged();
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

        pullToRefreshLayout.finishRefresh();
        isRefreshing = false;
    }

    @Override
    public void monitorPointUpdate(MonitorPoint mp) {

        String id = mp.getDeviceId();

        if(monitorPointMap.containsKey(id)){
            //monitorPointMap.replace(id, mp);
            monitorPointMap.remove(id);
            monitorPointMap.put(id, mp);
        }

        if(isShown){
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void refresh() {
        isRefreshing = true;

        if(mBinder != null){
            mBinder.requestMpList();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRefreshing){
                    pullToRefreshLayout.finishRefresh();
                    Toast.makeText(MonitorPointListActivity.this, "无法获取到设备列表", Toast.LENGTH_SHORT).show();
                }
            }
        }, 4000);

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
