package com.langkai.www.electricalfiredeviceapp.utils;

public class Constant {
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

}
