package com.langkai.www.electricalfiredeviceapp.ui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.langkai.www.electricalfiredeviceapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataRecordFragment extends Fragment {


    public DataRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_record, container, false);
    }

}