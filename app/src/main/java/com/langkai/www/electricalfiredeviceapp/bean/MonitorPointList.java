package com.langkai.www.electricalfiredeviceapp.bean;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MonitorPointList {

    @SerializedName("devices")
    List<MonitorPointInfo> list;

    public MonitorPointList(){
        list = new ArrayList<>();
    }


    public void addDeviceInfos(List<MonitorPointInfo> infos){
        list.addAll(infos);
    }

    public List<MonitorPointInfo> getDeviceInfos(){
        List<MonitorPointInfo> deviceIds = new ArrayList<>();
        deviceIds.addAll(list);

        return deviceIds;
    }

    public static class MonitorPointInfo{
        private String id;
        private String name;
        private int status;

        public MonitorPointInfo(String id, String name, int status){
            this.id = id;
            this.name = name;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


    }
}
