package com.langkai.www.electricalfiredeviceapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorPointAdapter extends RecyclerView.Adapter<MonitorPointAdapter.ViewHolder> {

    private Context context;
    private List<String> mData;
    private Map<String, MonitorPoint> mMap;

    public MonitorPointAdapter(Context context, List<String> data, Map<String, MonitorPoint> map){
        this.context = context;
        mData = data;
        mMap = map;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.monitor_point_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String deviceId = mData.get(position);
        if(!mMap.containsKey(deviceId))
            return;

        MonitorPoint mp = mMap.get(deviceId);
        holder.idTextView.setText(mp.getDeviceId());
        holder.nameTextView.setText(mp.getMonitorPointName());
        holder.channelTextView.setText(String.valueOf(mp.getChannelNumber()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView idTextView;
        private TextView nameTextView;
        private TextView channelTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            channelTextView = itemView.findViewById(R.id.channel_number_text_view);
        }
    }
}
