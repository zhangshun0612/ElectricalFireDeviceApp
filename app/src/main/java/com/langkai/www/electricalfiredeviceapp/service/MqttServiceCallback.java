package com.langkai.www.electricalfiredeviceapp.service;

import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointList;

public interface MqttServiceCallback {

    void monitorPointListUpdate(MonitorPointList list);
}