package com.langkai.www.electricalfiredeviceapp.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointList;
import com.langkai.www.electricalfiredeviceapp.service.MqttService;
import com.langkai.www.electricalfiredeviceapp.service.MqttServiceCallback;

public class MqttServiceActivity extends BaseActivity implements MqttServiceCallback {

    private MqttService.MqttServiceBinder mBinder = null;

    private ServiceConnection mqttServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (MqttService.MqttServiceBinder) iBinder;
            mBinder.setMqttServiceCallback(MqttServiceActivity.this);

            onServiceBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
           onServiceUnbound();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mqttServiceConn);
    }


    protected void onServiceBound(){

    }

    protected void onServiceUnbound(){

    }

    private void initService(){
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, mqttServiceConn, BIND_AUTO_CREATE);
    }

    public void connectIotService(){
        mBinder.connectIoTService();
    }

    public void disconnectIotService(){
        mBinder.disconnectIoTService();
    }

    public void requestMonitorPointList(){
        if(mBinder == null)
            return;

        mBinder.requestMpList();
    }

    public void requestMonitorPoint(String id){
        if(mBinder == null)
            return;

        mBinder.requestMpData(id);

    }

    @Override
    public void serviceConnected() {

    }

    @Override
    public void monitorPointListUpdate(MonitorPointList list) {

    }

    @Override
    public void monitorPointUpdate(MonitorPoint mp) {

    }
}
