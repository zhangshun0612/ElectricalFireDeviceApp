package com.langkai.www.electricalfiredeviceapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;

import java.util.List;

public class MonitorPointChannelAdapter extends RecyclerView.Adapter<MonitorPointChannelAdapter.ViewHolder> {

    private Context mContext;
    private List<MonitorPointChannel> mData;

    public MonitorPointChannelAdapter(Context context, List<MonitorPointChannel> data)
    {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        MonitorPointChannel ch = mData.get(position);

        return ch.getChannelDefine();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
