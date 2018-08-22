package com.langkai.www.electricalfiredeviceapp.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointList;
import com.langkai.www.electricalfiredeviceapp.service.MqttService;
import com.langkai.www.electricalfiredeviceapp.service.MqttServiceCallback;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

public class MqttServiceActivity extends BaseActivity implements MqttServiceCallback {

    private MqttService.MqttServiceBinder mBinder = null;

    private LoadingDialog loadingDialog;

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
        loadingDialog = new LoadingDialog(MqttServiceActivity.this);
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

        loadingDialog.setLoadingText("正在连接到Iot服务")
                .setSuccessText("成功连接到Iot服务")
                .setLoadSpeed(LoadingDialog.Speed.SPEED_ONE)
                .closeFailedAnim()
                .closeSuccessAnim()
                .show();

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
        loadingDialog.loadSuccess();
    }

    @Override
    public void serviceConnectFailed() {
        loadingDialog.loadFailed();
    }

    @Override
    public void serviceConnectionLost() {

        Toast.makeText(this, "正在重新连接到服务", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinder.connectIoTService();
            }
        }, 1500);


    }

    @Override
    public void monitorPointListUpdate(MonitorPointList list) {

    }

    @Override
    public void monitorPointUpdate(MonitorPoint mp) {

    }
}
