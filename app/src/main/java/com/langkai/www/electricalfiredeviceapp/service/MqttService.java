package com.langkai.www.electricalfiredeviceapp.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointList;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MqttService extends Service {

    final String brokerUrl = "ssl://iottestbyzs.mqtt.iot.gz.baidubce.com:1884";
    final String clientId = "com_langkai_www" + UUID.randomUUID().toString();
    final String userName = "iottestbyzs/device1";
    final String password = "O060WwWajgLjsLkeVMzscLoOstPdEdmw3rB9Amc24ts=";

    final String mpListTopic = "mpList";
    final String mpDataTopic = "mpData";

    final String requestMpListTopic = "request_mpList";
    final String requestMpDataTopic = "request_mpData";

    final String requestMpListCmd = "GET:request_mpList";
    final String requestMpDataCmd = "GET:request_mpData"; //with args


    private Gson gson = new Gson();
    private MqttAndroidClient mqttClient;
    private List<MqttServiceCallback> mCallbacks = new ArrayList<>();

    private MqttServiceBinder binder = null;

    public MqttService() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        if(mqttClient.isConnected()){
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        */
        mqttClient.unregisterResources();
        mqttClient.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(binder == null){
            binder = new MqttServiceBinder();
        }

        return binder;
    }


    public class MqttServiceBinder extends Binder {

        public void connectIoTService(){

            /*
            new Thread(new Runnable() {
                @Override
                public void run() {


            }).start();
            */


            MqttConnectOptions connOpt = new MqttConnectOptions();
            connOpt.setUserName(userName);
            connOpt.setPassword(password.toCharArray());
            connOpt.setCleanSession(true);
            connOpt.setConnectionTimeout(10);
            connOpt.setKeepAliveInterval(20);

            mqttClient = new MqttAndroidClient(MqttService.this, brokerUrl, clientId);
            mqttClient.setCallback(mqttCallback);

            try {
                mqttClient.connect(connOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }


        public void disconnectIoTService(){

            if(mqttClient.isConnected()){
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

        }

        public void setMqttServiceCallback(MqttServiceCallback callback){
            mCallbacks.add(callback);
        }

        public void removeMqttServiceCallback(MqttServiceCallback callback){
            mCallbacks.remove(callback);
        }

        public void requestMpList(){

            if(mqttClient == null && !mqttClient.isConnected()){
                //后续加上提醒
                return;
            }

            MqttMessage message = new MqttMessage();
            message.setQos(1);
            message.setPayload(requestMpListCmd.getBytes());

            try {
                mqttClient.publish(requestMpListTopic, message);

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        public void requestMpData(String deviceId){
            if(mqttClient == null && !mqttClient.isConnected()){
                //后续加上提醒
                return;
            }

            String messageStr = requestMpDataCmd + " " + deviceId;

            MqttMessage message = new MqttMessage();
            message.setQos(1);
            message.setPayload(messageStr.getBytes());

            try {
                mqttClient.publish(requestMpDataTopic, message);

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Toast.makeText(MqttService.this, "成功连接到Iot服务器", Toast.LENGTH_SHORT).show();
            try {
                mqttClient.subscribe(mpListTopic, 1);
                mqttClient.subscribe(mpDataTopic, 1);
            } catch (MqttException e) {
                Toast.makeText(MqttService.this, "订阅Iot主题失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Toast.makeText(MqttService.this, "无法连接到Iot服务器", Toast.LENGTH_SHORT).show();
        }
    };


    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {

            Toast.makeText(MqttService.this, "服务连接已断开", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            if(topic.equals(mpListTopic)){
                try {
                    String jsonStr = message.toString();

                    MonitorPointList mpList = gson.fromJson(jsonStr, MonitorPointList.class);

                    if(!mCallbacks.isEmpty()){
                        for(int i = 0 ; i < mCallbacks.size(); i++){
                            mCallbacks.get(i).monitorPointListUpdate(mpList);
                        }
                    }

                }catch (JsonSyntaxException exp){
                    exp.printStackTrace();
                }


            }else if(topic.equals(mpDataTopic)){

                try {
                    String jsonStr = message.toString();
                    MonitorPoint mp = gson.fromJson(jsonStr, MonitorPoint.class);

                    if(!mCallbacks.isEmpty()){
                        for(int i = 0 ; i < mCallbacks.size(); i++){
                            mCallbacks.get(i).monitorPointUpdate(mp);
                        }
                    }


                }catch (JsonSyntaxException  e){
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
}
