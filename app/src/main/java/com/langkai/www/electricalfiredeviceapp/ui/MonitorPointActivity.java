package com.langkai.www.electricalfiredeviceapp.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.langkai.www.electricalfiredeviceapp.R;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPoint;
import com.langkai.www.electricalfiredeviceapp.bean.MonitorPointChannel;
import com.langkai.www.electricalfiredeviceapp.ui.fragment.DataRecordFragment;
import com.langkai.www.electricalfiredeviceapp.ui.fragment.DataShowFragment;
import com.langkai.www.electricalfiredeviceapp.ui.fragment.SettingConfigFragment;
import com.langkai.www.electricalfiredeviceapp.utils.Constant;

public class MonitorPointActivity extends BaseActivity implements View.OnClickListener {

    private static int FRAGMENT_DATA_SHOW = 1;
    private static int FRAGMENT_SETTING_CONFIG = 2;
    private static int FRAGMENT_DATA_RECORD = 3;

    private int fragmentShowing;

    private String TAG = MonitorPointActivity.class.getSimpleName();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageButton dataShowButton;
    private ImageButton settingConfigButton;
    private ImageButton dataRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_point);

        Bundle bundle = getIntent().getBundleExtra("data");
        MonitorPoint mp = (MonitorPoint) bundle.getSerializable("mp");

        MonitorPointChannel ch = mp.getMonitorPointChannel(1);
        if(ch != null){
            Log.d(TAG , "CH_DEF: " + ch.getChannelDefine());
        }

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new DataShowFragment());
        fragmentTransaction.commit();
        fragmentShowing = FRAGMENT_DATA_SHOW;

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
    public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.data_show_button:
                if(fragmentShowing != FRAGMENT_DATA_SHOW) {
                    fragmentTransaction.replace(R.id.frame_layout, new DataShowFragment());
                    dataShowButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_show_chosen));
                    settingConfigButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_setting_config_notchosen));
                    dataRecordButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_record_notchosen));
                    fragmentShowing = FRAGMENT_DATA_SHOW;
                }
                break;
            case R.id.setting_config_button:
                if(fragmentShowing != FRAGMENT_SETTING_CONFIG){
                    fragmentShowing = FRAGMENT_SETTING_CONFIG;
                    fragmentTransaction.replace(R.id.frame_layout, new SettingConfigFragment());
                    dataShowButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_show_notchosen));
                    settingConfigButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_setting_config_chosen));
                    dataRecordButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_record_notchosen));
                }

                break;
            case R.id.data_record_button:
                if(fragmentShowing != FRAGMENT_DATA_RECORD){
                    fragmentShowing = FRAGMENT_DATA_RECORD;
                    fragmentTransaction.replace(R.id.frame_layout, new DataRecordFragment());
                    dataShowButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_show_notchosen));
                    settingConfigButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_setting_config_notchosen));
                    dataRecordButton.setImageDrawable(getResources().getDrawable(R.mipmap.ic_fragment_data_record_chosen));
                }

                break;
        }

        fragmentTransaction.commit();
    }
}
