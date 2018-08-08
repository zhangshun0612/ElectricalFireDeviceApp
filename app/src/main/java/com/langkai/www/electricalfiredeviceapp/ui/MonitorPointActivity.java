package com.langkai.www.electricalfiredeviceapp.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.ui.fragment.DataRecordFragment;
import com.langkai.www.electricalfiredeviceapp.ui.fragment.DataShowFragment;
import com.langkai.www.electricalfiredeviceapp.ui.fragment.SettingConfigFragment;

public class MonitorPointActivity extends MqttServiceActivity implements View.OnClickListener{

    private String TAG = MonitorPointActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageButton dataShowButton;
    private ImageButton settingConfigButton;
    private ImageButton dataRecordButton;

    private DataShowFragment dataShowFragment;
    private SettingConfigFragment settingConfigFragment;
    private DataRecordFragment dataRecordFragment;


    private MonitorPoint monitorPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_point);

        Bundle bundle = getIntent().getBundleExtra("data");
        monitorPoint = (MonitorPoint) bundle.getSerializable("mp");

        dataShowFragment = new DataShowFragment();
        settingConfigFragment = new SettingConfigFragment();
        dataRecordFragment = new DataRecordFragment();

        dataShowFragment.setMonitorPoint(monitorPoint);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, dataShowFragment);
        fragmentTransaction.commit();


        dataShowButton = findViewById(R.id.data_show_button);
        dataRecordButton = findViewById(R.id.data_record_button);
        settingConfigButton = findViewById(R.id.setting_config_button);

        dataShowButton.setOnClickListener(this);
        dataRecordButton.setOnClickListener(this);
        settingConfigButton.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onServiceBound() {
        super.onServiceBound();
        requestMonitorPoint(monitorPoint.getDeviceId());
    }

    @Override
    public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.data_show_button:
                if(!dataShowFragment.isVisible()) {
                    fragmentTransaction.replace(R.id.frame_layout, dataShowFragment);
                    dataShowButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_show_chosen));
                    settingConfigButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_setting_config_notchosen));
                    dataRecordButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_record_notchosen));
                }
                break;
            case R.id.setting_config_button:
                if(!settingConfigFragment.isVisible()){
                    fragmentTransaction.replace(R.id.frame_layout, settingConfigFragment);
                    dataShowButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_show_notchosen));
                    settingConfigButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_setting_config_chosen));
                    dataRecordButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_record_notchosen));
                }

                break;
            case R.id.data_record_button:
                if(!dataRecordFragment.isVisible()){
                    fragmentTransaction.replace(R.id.frame_layout, dataRecordFragment);
                    dataShowButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_show_notchosen));
                    settingConfigButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_setting_config_notchosen));
                    dataRecordButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_record_chosen));
                }

                break;
        }

        fragmentTransaction.commit();
    }


    @Override
    public void monitorPointUpdate(MonitorPoint mp) {
        if(!mp.getDeviceId().equals(monitorPoint.getDeviceId()))
            return;

        dataShowFragment.updateMonitorPoint(mp);
    }
}
