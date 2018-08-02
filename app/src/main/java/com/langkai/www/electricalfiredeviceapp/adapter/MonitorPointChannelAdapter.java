package com.langkai.www.electricalfiredeviceapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public class ChannelValueViewHolder extends RecyclerView.ViewHolder {

        public ChannelValueViewHolder(View itemView) {
            super(itemView);
        }
    }



}
