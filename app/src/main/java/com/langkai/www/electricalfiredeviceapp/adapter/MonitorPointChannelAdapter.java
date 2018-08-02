package com.langkai.www.electricalfiredeviceapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;
import com.langkai.www.electricalfiredeviceapp.utils.Constant;

public class MonitorPointChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = MonitorPointChannelAdapter.class.getSimpleName();

    private Context mContext;
    private SparseArray<MonitorPointChannel> mData = null;

    public MonitorPointChannelAdapter(Context context, SparseArray<MonitorPointChannel> data)
    {
        mContext = context;
        mData = data;
    }

    public void updateData(SparseArray<MonitorPointChannel> data){
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.monitor_point_channel_value_item, parent, false);
        ChannelValueViewHolder viewHolder = new ChannelValueViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChannelValueViewHolder viewHolder = (ChannelValueViewHolder) holder;

        int chnum = mData.keyAt(position);
        viewHolder.channelNumberTextView.setText(String.valueOf(chnum));

        MonitorPointChannel channel = mData.get(chnum);
        if(channel == null)
            return;

        switch (channel.getChannelDefine()){
            case Constant.FUNCTION_RESIDUAL_CURRENT:
                viewHolder.channelFunctionTextView.setText(R.string.function_residual_current);
                break;
            case Constant.FUNCTION_TEMPERATURE:
                viewHolder.channelFunctionTextView.setText(R.string.function_temperature);
                break;
            case Constant.FUNCTION_RUNNING_CURRENT:
                viewHolder.channelFunctionTextView.setText(R.string.function_running_current);
                break;
            case Constant.FUNCTION_RUNNING_VOLT:
                viewHolder.channelFunctionTextView.setText(R.string.function_running_volt);
                break;
        }

        int status = channel.getChannelStatus();
        if(status == Constant.STATUS_OK ){
            viewHolder.descriptionTextView.setText(R.string.status_ok);
            viewHolder.valueTextView.setText(channel.getValueString());
        }else if(status == Constant.STATUS_CREATE){
            viewHolder.descriptionTextView.setText(R.string.status_created);
        }else if(status == Constant.STATUS_DISCONNECTED){
            viewHolder.descriptionTextView.setText(R.string.status_disconnected);
        }else if(status == Constant.STATUS_FAULT){
            switch (channel.getChannelFaultType()){
                case Constant.FAULT_BREAK:
                    viewHolder.descriptionTextView.setText(R.string.fault_break);
                    break;
                case Constant.FAULT_SHORT:
                    viewHolder.descriptionTextView.setText(R.string.fault_short);
                    break;
                case Constant.FAULT_EARTH:
                    viewHolder.descriptionTextView.setText(R.string.fault_earth);
                    break;
                case Constant.FAULT_SENSOR:
                    viewHolder.descriptionTextView.setText(R.string.fault_sensor);
                    break;
            }
        }else if(status == Constant.STATUS_ALARM){
            switch (channel.getChannelAlarmType()){
                case Constant.ALARM_UPPER_LIMIT:
                    viewHolder.descriptionTextView.setText(R.string.alarm_upper_limit);
                    break;
                case Constant.ALARM_LOWER_LIMIT:
                    viewHolder.descriptionTextView.setText(R.string.alarm_lower_limit);
                    break;
                case Constant.ALARM_PHASE_MISSING:
                    viewHolder.descriptionTextView.setText(R.string.alarm_phase_missing);
                    break;
                case Constant.ALARM_PHASE_FAULT:
                    viewHolder.descriptionTextView.setText(R.string.alarm_phase_fault);
                    break;
            }
            viewHolder.valueTextView.setText(channel.getValueString());
        }

    }

    @Override
    public int getItemViewType(int position) {
        int key = mData.keyAt(position);
        MonitorPointChannel ch = mData.get(key);
        if(ch == null){
            return Constant.FUNCTION_UNDEFINED;
        }
        return ch.getChannelDefine();
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        else
            return mData.size();

    }


    private class ChannelValueViewHolder extends RecyclerView.ViewHolder {

        private TextView channelNumberTextView;
        private TextView channelFunctionTextView;
        private TextView descriptionTextView;
        private TextView valueTextView;

        private ChannelValueViewHolder(View itemView) {
            super(itemView);

            channelNumberTextView = itemView.findViewById(R.id.channel_number_text_view);
            channelFunctionTextView = itemView.findViewById(R.id.channel_func_text_view);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            valueTextView = itemView.findViewById(R.id.value_text_view);
        }
    }



}
