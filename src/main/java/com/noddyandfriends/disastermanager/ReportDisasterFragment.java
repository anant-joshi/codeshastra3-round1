package com.noddyandfriends.disastermanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.noddyandfriends.disastermanager.util.OnReportButtonClicked;


public class ReportDisasterFragment extends Fragment {


    public ReportDisasterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_report_disaster, container, false);
        Button reportButton = (Button) rootView.findViewById(R.id.report_disaster_button);
        AppCompatSpinner spinner = (AppCompatSpinner) rootView.findViewById(R.id.report_disaster_disaster_type);
        reportButton.setOnClickListener(OnReportButtonClicked.listenerWithContext(getActivity()).andDisasterType(spinner.getSelectedItem().toString()));
        return rootView;
    }
}
