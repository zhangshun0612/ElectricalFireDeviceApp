package com.langkai.www.electricalfiredeviceapp.bean;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MonitorPointList {

    @SerializedName("devices")
    List<String> list;

    public MonitorPointList(){
        list = new ArrayList<>();
    }

    public void addDeviceIds(List<String> ids){
        list.addAll(ids);
    }

    public List<String> getDeviceIds(){
        List<String> deviceIds = new ArrayList<>();
        deviceIds.addAll(list);

        return deviceIds;
    }
}
