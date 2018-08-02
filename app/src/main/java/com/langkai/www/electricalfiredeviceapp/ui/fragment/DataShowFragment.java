package com.langkai.www.electricalfiredeviceapp.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.adapter.MonitorPointChannelAdapter;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataShowFragment extends Fragment {

    private RecyclerView recyclerView = null;
    private TextView idTextView = null;
    private TextView nameTextView = null;

    private LinearLayoutManager manager = null;
    private MonitorPointChannelAdapter mAdapter = null;

    private MonitorPoint monitorPoint = null;
    private SparseArray<MonitorPointChannel> channelList;

    public DataShowFragment() {

    }

    public void setMonitorPoint(MonitorPoint mp)
    {
        monitorPoint = mp;
        channelList = mp.getMonitorPointChannels();
    }

    private void updateView(){
        idTextView.setText(monitorPoint.getDeviceId());
        nameTextView.setText(monitorPoint.getMonitorPointName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_show, container, false);
        idTextView = view.findViewById(R.id.show_data_id_text_view);
        nameTextView = view.findViewById(R.id.show_data_name_text_view);

        recyclerView = view.findViewById(R.id.show_data_recycler_view);
        manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        mAdapter = new MonitorPointChannelAdapter(view.getContext(), channelList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        updateView();

        mAdapter.notifyDataSetChanged();
        return view;
    }

}
