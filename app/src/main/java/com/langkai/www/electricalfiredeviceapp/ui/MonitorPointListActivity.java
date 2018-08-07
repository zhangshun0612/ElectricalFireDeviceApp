package com.langkai.www.electricalfiredeviceapp.ui;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    NotificationManager notificationManager = null;

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

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
        notificationManager.cancelAll();
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
            MonitorPointChannel ch1 = new MonitorPointChannel(1, Constant.FUNCTION_RESIDUAL_CURRENT);
            ch1.setChannelStatus(Constant.STATUS_OK);
            ch1.setValue(30.22);
            mp.addMonitorPointChannel(1, ch1);

            MonitorPointChannel ch2 = new MonitorPointChannel(1, Constant.FUNCTION_TEMPERATURE);
            ch2.setChannelStatus(Constant.STATUS_FAULT);
            ch2.setChannelFaultType(Constant.FAULT_BREAK);
            mp.addMonitorPointChannel(2, ch2);


            MonitorPointChannel ch3 = new MonitorPointChannel(1, Constant.FUNCTION_RUNNING_CURRENT);
            ch3.setChannelStatus(Constant.STATUS_ALARM);
            ch3.setChannelAlarmType(Constant.ALARM_UPPER_LIMIT);
            ch3.setValue(20);
            mp.addMonitorPointChannel(3, ch3);


            MonitorPointChannel ch4 = new MonitorPointChannel(1, Constant.FUNCTION_RUNNING_VOLT);
            ch4.setChannelStatus(Constant.STATUS_DISCONNECTED);
            mp.addMonitorPointChannel(4, ch4);


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
        List<MonitorPointList.MonitorPointInfo> infoList = list.getDeviceInfos();

        for(int i = 0 ; i < infoList.size(); i++){
            MonitorPointList.MonitorPointInfo info = infoList.get(i);

            if(!mDataList.contains(info.getId())){
                String id = info.getId();
                mDataList.add(id);
                monitorPointMap.put(id, new MonitorPoint(id, info.getName(), info.getStatus()));
            }
        }

        mAdapter.notifyDataSetChanged();

        pullToRefreshLayout.finishRefresh();
        isRefreshing = false;
    }

    @Override
    public void monitorPointUpdate(MonitorPoint mp) {

        String id = mp.getDeviceId();

        if(!monitorPointMap.containsKey(id))
            return;

        MonitorPoint preMp = monitorPointMap.get(id);
        if(preMp.getMonitorPointStatus() == Constant.STATUS_OK
                && mp.getMonitorPointStatus() == Constant.STATUS_ALARM)
        {
            //报警提示
            setupAlarmNotification(mp);
        }

        monitorPointMap.remove(id);
        monitorPointMap.put(id, mp);


        if(isShown){
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void refresh() {
        /*
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
       */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupAlarmNotification(monitorPointMap.get(mDataList.get(0)));
                pullToRefreshLayout.finishRefresh();
            }
        }, 500);
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

    private void setupAlarmNotification(MonitorPoint mp){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_mp_alarm);

        Bundle bundle = new Bundle();
        bundle.putSerializable("mp", mp);

        Intent intent = new Intent(MonitorPointListActivity.this, MonitorPointActivity.class);
        intent.putExtra("data", bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, "alarm")
                .setSmallIcon(R.mipmap.ic_mp_alarm)
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                /*
                .setStyle(
                        new NotificationCompat.InboxStyle()
                                .addLine(mp.getDeviceId() + "报警")
                .setBigContentTitle("设备报警"))
                */
                .setContentTitle("设备报警")
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        notificationManager.notify(0, notification);

    }
}
