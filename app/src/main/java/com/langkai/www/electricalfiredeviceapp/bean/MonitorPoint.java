package com.langkai.www.electricalfiredeviceapp.bean;

import java.util.Date;

public class MonitorPoint {
    //状态类型
    public static final int STATUS_CREATE = 0x00;
    public static final int STATUS_OK = 0x01;
    public static final int STATUS_ALARM = 0x02;
    public static final int STATUS_FAULT = 0x03;
    public static final int STATUS_DISCONNECTED = 0x04;

    //功能类型
    public static final int FUNCTION_UNDEFINED = 0x00;
    public static final int FUNCTION_RESIDUAL_CURRENT = 0x01;
    public static final int FUNCTION_TEMPERATURE = 0x02;
    public static final int FUNCTION_RUNNING_CURRENT = 0x03;
    public static final int FUNCTION_RUNNING_VOLT = 0x04;
    public static final int FUNCTION_SWITCH_OUTPUT = 0x05;
    public static final int FUNCTION_SWITCH_INPUT = 0x06;
    public static final int FUNCTION_DISABLED = 0x0e;

    //故障类型
    public static final int FAULT_SHORT = 0x01;
    public static final int FAULT_BREAK = 0x02;
    public static final int FAULT_EARTH = 0x03;
    public static final int FAULT_SENSOR = 0x04;

    //报警类型
    public static final int ALARM_UPPER_LIMIT = 0x00;
    public static final int ALARM_LOWER_LIMIT = 0x01;
    public static final int ALARM_PHASE_MISSING = 0x02;
    public static final int ALARM_PHASE_FAULT = 0x03;


    private String deviceId;

    private int channelNumber;
    private String monitorPointName;

    private int channelDefine;
    private int channelStatus;
    private int channelAlarmType;
    private int channelFaultType;
    private float value;

    private Date latestUpdateTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getMonitorPointName() {
        return monitorPointName;
    }

    public void setMonitorPointName(String monitorPointName) {
        this.monitorPointName = monitorPointName;
    }

    public int getChannelDefine() {
        return channelDefine;
    }

    public void setChannelDefine(int channelDefine) {
        this.channelDefine = channelDefine;
    }

    public int getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(int channelStatus) {
        this.channelStatus = channelStatus;
    }

    public int getChannelAlarmType() {
        return channelAlarmType;
    }

    public void setChannelAlarmType(int channelAlarmType) {
        this.channelAlarmType = channelAlarmType;
    }

    public int getChannelFaultType() {
        return channelFaultType;
    }

    public void setChannelFaultType(int channelFaultType) {
        this.channelFaultType = channelFaultType;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getLatestUpdateTime() {
        return latestUpdateTime;
    }

    public void setLatestUpdateTime(Date latestUpdateTime) {
        this.latestUpdateTime = latestUpdateTime;
    }


    public MonitorPoint(){
        this.monitorPointName = "未定义";
        this.channelStatus = MonitorPoint.STATUS_CREATE;
        this.channelDefine = MonitorPoint.FUNCTION_UNDEFINED;

    }

    public MonitorPoint(String deviceId, String name){
        this.deviceId = deviceId;
        this.monitorPointName = name;

        this.channelStatus = MonitorPoint.STATUS_CREATE;
        this.channelDefine = MonitorPoint.FUNCTION_UNDEFINED;
    }


}
