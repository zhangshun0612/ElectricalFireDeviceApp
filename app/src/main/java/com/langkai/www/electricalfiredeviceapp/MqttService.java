package com.langkai.www.electricalfiredeviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

public class MqttService extends Service {

    final String brokerUrl = "ssl://iottestbyzs.mqtt.iot.gz.baidubce.com:1884";
    final String clientId = "com_langkai_www" + UUID.randomUUID().toString();
    final String userName = "iottestbyzs/device1";
    final String password = "O060WwWajgLjsLkeVMzscLoOstPdEdmw3rB9Amc24ts=";

    final String mpListTopic = "mpList";
    final String mpDataTopic = "mpData";

    private MqttAndroidClient mqttClient;

    public MqttService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        MqttServiceBinder binder = new MqttServiceBinder();
        return binder;
    }


    public class MqttServiceBinder extends Binder {

        public void connectIoTService(){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    String threadId = Thread.currentThread().getName();
                    Log.d("MqttService", threadId);

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
            }).start();

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
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String threadId = Thread.currentThread().getName();
            Log.d("MqttService", threadId);

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
}
