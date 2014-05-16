package com.flexworkoid.app;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Chronometer;



/**
 * Created by red on 27.01.14.
 */
public class TodayFragment extends Fragment implements View.OnClickListener {

    public static final String COLOR_STRING_POMODORO = "#fdf700";
    public static final String COLOR_STRING_BREAK = "#04B404";
    public static final String COLOR_STRING_TRACKING = "#800080";
    public static final String COLOR_STRING_SLEEP = "#A4A4A4";
    public static final String COLOR_STRING_BLUE = "#6495ED";
    public static final String COLOR_STRING_RED = "#DC143C";

    public static final int COLOR_POMODORO = Color.parseColor(COLOR_STRING_POMODORO);
    public static final int COLOR_BREAK =Color.parseColor(COLOR_STRING_BREAK);
    public static final int COLOR_TRACKING = Color.parseColor(COLOR_STRING_TRACKING);
    public static final int COLOR_SLEEP = Color.parseColor(COLOR_STRING_SLEEP);
    public static final int COLOR_BLUE = Color.parseColor(COLOR_STRING_BLUE);
    public static final int COLOR_RED = Color.parseColor(COLOR_STRING_RED);

    Button startButton;
    TextView currentTime;
    TextView currentBreak;
    TextView scheduledTime;
    PieChart pieChart;
    Waterbox waterbox;
    Chronometer timeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        startButton = (Button) view.findViewById(R.id.startbutton);
        startButton.setOnClickListener(this);

//        currentTime = (TextView) view.findViewById(R.id.currentTime);
        currentBreak = (TextView) view.findViewById(R.id.currentBreak);
        scheduledTime = (TextView) view.findViewById(R.id.scheduledTime);

        timeText = (Chronometer) view.findViewById(R.id.timeText);
        timeText.setText("00:00");
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/telegrama.otf");///fonts/ Roboto-Black.ttf
        timeText.setTypeface(tf);
        timeText.setTextColor(COLOR_BLUE);

//        pieChart = (PieChart) view.findViewById(R.id.overviewPieChart);
//        pieChart.setValues(new float[]{10,10});

        waterbox = (Waterbox) view.findViewById(R.id.currentTimeWaterbox);
        waterbox.setValues(120, 240);

        return view;
    }

    @Override
    public void onClick(View view) {
        swapClockState();
    }

    private void swapClockState(){
        if(!MainActivity.clockState) {
            startClock();
        } else {
            pauseClock();
        }
        Log.e("Jens", "swapClockState");
    }

    private void startClock(){
        if(!MainActivity.clockState){
            long sysBase = SystemClock.elapsedRealtime();
            timeText.setBase(sysBase - MainActivity.clockTime);
        }
        timeText.start();
        MainActivity.clockState = true;
        startButton.setText("Pause Work");

        Log.e("Jens", "startClock");
    }

    private void pauseClock(){
        timeText.stop();
        MainActivity.clockState = false;

        long sysBase = SystemClock.elapsedRealtime();
        long textBase = timeText.getBase();
        MainActivity.clockTime = (sysBase - textBase);

        startButton.setText("Start Work");

        Log.e("Jens", "pauseClock");
    }

    private void stopClock(){
        timeText.stop();
        MainActivity.clockState = false;
        startButton.setText("Start Work");
        //Datenbank Schreiben!
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        MainActivity.clockState = settings.getBoolean(MainActivity.KEY_CLOCKSTATE, false);
        MainActivity.clockTime = settings.getLong(MainActivity.KEY_CLOCKTIME, 0);

        Log.e("Jens", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.clockState){
            Log.e("Jens", "onResume true CT: "+MainActivity.clockTime);
            timeText.setBase(MainActivity.clockTime);
            startClock();
        } else {
            Log.e("Jens", "onResume false CT: "+MainActivity.clockTime);
            long sysBase = SystemClock.elapsedRealtime();
            timeText.setBase(sysBase - MainActivity.clockTime);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(MainActivity.clockState){
            Log.e("Jens", "onPause true CT: "+MainActivity.clockTime);
            MainActivity.clockTime = timeText.getBase();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(MainActivity.KEY_CLOCKSTATE, MainActivity.clockState);
        editor.putLong(MainActivity.KEY_CLOCKTIME, MainActivity.clockTime);
        editor.commit();

        Log.e("Jens", "onStop");
    }
}
