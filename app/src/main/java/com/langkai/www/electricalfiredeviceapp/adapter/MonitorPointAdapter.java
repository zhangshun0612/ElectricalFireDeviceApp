package com.langkai.www.electricalfiredeviceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.ui.MonitorPointActivity;
import com.langkai.www.electricalfiredeviceapp.utils.Constant;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String deviceId = mData.get(position);
        if(!mMap.containsKey(deviceId))
            return;

        MonitorPoint mp = mMap.get(deviceId);
        holder.idTextView.setText(mp.getDeviceId());
        holder.nameTextView.setText(mp.getMonitorPointName());

        int status = mp.getMonitorPointStatus();
        int ic_id = R.mipmap.ic_mp_ok;
        switch (status){
            case Constant.STATUS_OK:
            case Constant.STATUS_CREATE:
                ic_id = R.mipmap.ic_mp_ok;
                break;
            case Constant.STATUS_ALARM:
                ic_id = R.mipmap.ic_mp_alarm;
                break;
            case Constant.STATUS_FAULT:
            case Constant.STATUS_DISCONNECTED:
                ic_id = R.mipmap.ic_mp_fault;
                break;
        }
        holder.statusImageView.setImageResource(ic_id);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView idTextView;
        private TextView nameTextView;
        private ImageView statusImageView;
        private ImageButton deleteImageButton;
        private LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            statusImageView = itemView.findViewById(R.id.status_image_view);
            deleteImageButton = itemView.findViewById(R.id.delete_image_button);
            itemLayout = itemView.findViewById(R.id.item_layout);

            deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String id = mData.get(position);
                    mData.remove(position);

                    mMap.remove(id);

                    notifyDataSetChanged();
                }
            });

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String id = mData.get(position);
                    MonitorPoint mp = mMap.get(id);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mp", mp);

                    Intent intent = new Intent(context, MonitorPointActivity.class);
                    intent.putExtra("data", bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
