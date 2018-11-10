package com.langkai.www.electricalfiredeviceapp.ui.popupWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;
import com.langkai.www.electricalfiredeviceapp.utils.Constant;

public class ChannelChoosePopupWindow extends PopupWindow {

    private View contentView = null;
    private Context mContext = null;

    private RecyclerView channelRecyclerView = null;
    private LinearLayoutManager manager = null;

    private ChannelChooseAdapter mAdapter = null;

    private SparseArray<MonitorPointChannel> mData = null;

    public ChannelChoosePopupWindow(Context context, SparseArray<MonitorPointChannel> data){
        mContext = context;
        mData = data;
        contentView = LayoutInflater.from(mContext).inflate(R.layout.view_channel_choose, null, false);

        setWidth(mContext.getResources().getDisplayMetrics().widthPixels * 3 /4);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(contentView);

        setOutsideTouchable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAdapter = new ChannelChooseAdapter();

        channelRecyclerView = contentView.findViewById(R.id.channel_recycler_view);
        manager = new LinearLayoutManager(contentView.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        channelRecyclerView.setLayoutManager(manager);

        channelRecyclerView.setAdapter(mAdapter);
        channelRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
    }

    public class ChannelChooseAdapter extends RecyclerView.Adapter<ChannelChooseAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.channel_choose_item, null, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ViewHolder viewHolder = holder;

            int chnum = mData.keyAt(position);
            viewHolder.channelNumberTextView.setText("通道：" + String.valueOf(chnum));

            MonitorPointChannel channel = mData.get(chnum);
            if(channel == null)
                return;
            /*
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
            */
        }

        @Override
        public int getItemCount() {
            if(mData == null)
                return 0;
            else
                return mData.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder{

            private TextView channelNumberTextView;
            //private TextView channelFunctionTextView;

            private ViewHolder(View itemView) {
                super(itemView);

                channelNumberTextView = itemView.findViewById(R.id.channel_choose_number_tv);
                //channelFunctionTextView = itemView.findViewById(R.id.channel_choose_function_tv);

            }
        }
    }

}
