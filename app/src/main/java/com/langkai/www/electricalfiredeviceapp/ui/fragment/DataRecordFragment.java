package com.langkai.www.electricalfiredeviceapp.ui.fragment;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;
import com.langkai.www.electricalfiredeviceapp.ui.MqttServiceActivity;
import com.langkai.www.electricalfiredeviceapp.ui.popupWindow.ChannelChoosePopupWindow;
import com.langkai.www.electricalfiredeviceapp.utils.Constant;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRecordFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ImageButton queryImageButton = null;
    private ImageButton channelChooseButton = null;
    private Spinner channelNumberSpinner = null;
    private LineChart dataChart = null;
    private LoadingDialog loadingDialog = null;



    private MonitorPoint monitorPoint = null;

    private List<Entry> dataEntries = new ArrayList<>();
    private List<String> timeStamps = new ArrayList<>();
    private SparseArray<MonitorPointChannel> channels = null;
    private String label = "";

    private Handler handler = new Handler();

    private Context mContext;

    public DataRecordFragment() {

    }


    public void setMonitorPoint(MonitorPoint mp){
        monitorPoint = mp;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_record, container, false);
        channelNumberSpinner = view.findViewById(R.id.channel_number_spinner);
        queryImageButton = view.findViewById(R.id.query_button);
        channelChooseButton = view.findViewById(R.id.channel_choose_button);
        dataChart = view.findViewById(R.id.data_chart);
        mContext = view.getContext();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, getData());
        channelNumberSpinner.setAdapter(adapter);
        channelNumberSpinner.setOnItemSelectedListener(this);

        queryImageButton.setOnClickListener(this);
        channelChooseButton.setOnClickListener(this);

        initLineChart();
        return view;
    }


    private void initLineChart(){
        XAxis xAxis = dataChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)value;
                if(index < 0 || index >= timeStamps.size())
                    return "";

                return timeStamps.get(index);
            }
        });

        YAxis rightAxis = dataChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = dataChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawLabels(true);
        leftAxis.setGranularity(1f);


        dataChart.setVisibleXRangeMaximum(10);
    }

    private List<String> getData(){
        List<String> dataList = new ArrayList<>();

        channels = monitorPoint.getMonitorPointChannels();

        for(int i = 0 ; i < channels.size(); i++){
            int channelNumber = channels.keyAt(i);
            MonitorPointChannel ch = channels.get(channelNumber);
            String channelDef = "";

            switch (ch.getChannelDefine()){
                case Constant.FUNCTION_RESIDUAL_CURRENT:
                    channelDef = getResources().getString(R.string.function_residual_current);
                    break;
                case Constant.FUNCTION_TEMPERATURE:
                    channelDef = getResources().getString(R.string.function_temperature);
                    break;
                case Constant.FUNCTION_RUNNING_CURRENT:
                    channelDef = getResources().getString(R.string.function_running_current);
                    break;
                case Constant.FUNCTION_RUNNING_VOLT:
                    channelDef = getResources().getString(R.string.function_running_volt);
                    break;
            }

            dataList.add("通道: " + String.format(Locale.CHINESE, "%02d", channelNumber) + "---" + channelDef);
        }

        return dataList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.query_button:
                queryData();
                break;
            case R.id.channel_choose_button:
                chooseChannel();
                break;
        }

    }

    private void chooseChannel(){
        /*
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_channel_choose, null, false);

        channelRecyclerView = contentView.findViewById(R.id.channel_recycler_view);
        manager = new LinearLayoutManager(contentView.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        PopupWindow window = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        */

        ChannelChoosePopupWindow window = new ChannelChoosePopupWindow(mContext, channels);
        window.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private void queryData()
    {
        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.setLoadingText("正在查询")
                .setSuccessText("查询成功")
                .setFailedText("查询失败")
                .setLoadSpeed(LoadingDialog.Speed.SPEED_ONE)
                .closeFailedAnim()
                .closeSuccessAnim()
                .show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LineDataSet dataSet = new LineDataSet(dataEntries, label);
                        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        dataSet.setLineWidth(1.5f);
                        dataSet.setCircleRadius(1.5f);
                        dataSet.setColor(R.color.lineColor);

                        dataSet.setDrawFilled(true);

                        LineData lineData = new LineData(dataSet);
                        dataChart.setData(lineData);

                        dataChart.setVisibleXRangeMaximum(10);
                        dataChart.moveViewToX(dataEntries.size() - 10);

                        loadingDialog.loadSuccess();
                    }
                });
            }
        }).start();
    }

    private void initData(){
        Random random = new Random();
        long startTimeStamp = System.currentTimeMillis();

        dataEntries.clear();
        timeStamps.clear();
        for(int i = 0 ; i < 30; i++){

            double val = random.nextDouble()  * 5 + 10;
            dataEntries.add(new Entry( i, (float) val));

            long timestamp = startTimeStamp + 1000 * i;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINESE);
            String timeStampStr = sdf.format(new Date(timestamp));
            timeStamps.add(timeStampStr);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(mContext, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        label = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
