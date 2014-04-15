package com.flexworkoid.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by red on 27.01.14.
 */
public class TodayFragment extends Fragment implements View.OnClickListener {

    Button startButton;
    TextView currentTime;
    TextView currentBreak;
    TextView scheduledTime;
    PieChart pieChart;
    Waterbox waterbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        startButton = (Button) view.findViewById(R.id.startbutton);
        startButton.setOnClickListener(this);

//        currentTime = (TextView) view.findViewById(R.id.currentTime);
        currentBreak = (TextView) view.findViewById(R.id.currentBreak);
        scheduledTime = (TextView) view.findViewById(R.id.scheduledTime);

        pieChart = (PieChart) view.findViewById(R.id.overviewPieChart);
        pieChart.setValues(new float[]{10,10});

        waterbox = (Waterbox) view.findViewById(R.id.currentTimeWaterbox);
        waterbox.setValues(120, 240);

        return view;
    }

    @Override
    public void onClick(View view) {
        
    }
}
