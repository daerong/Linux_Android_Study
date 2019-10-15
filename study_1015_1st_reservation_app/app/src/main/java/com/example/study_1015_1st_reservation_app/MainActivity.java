package com.example.study_1015_1st_reservation_app;

import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometerView;
    Button startBtn;
    Button endBtn;
    RadioGroup rdoSelect;
    TimePicker tPicker;
    DatePicker dPicker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometerView = findViewById(R.id.chronometer_view);
        startBtn = findViewById(R.id.startBtn);
        endBtn = findViewById(R.id.endBtn);
        rdoSelect = findViewById(R.id.rdoSelect);
        tPicker = findViewById(R.id.tPicker);
        dPicker = findViewById(R.id.dPicker);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometerView.setBase(SystemClock.elapsedRealtime());
                chronometerView.start();
                chronometerView.setTextColor(Color.RED);
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometerView.stop();
                chronometerView.setTextColor(Color.BLUE);
            }
        });

        rdoSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) { ;
                if(i == R.id.rdoCal){
                    tPicker.setVisibility(View.GONE);
                    dPicker.setVisibility(View.VISIBLE);
                }else if(i == R.id.rdoTime){
                    tPicker.setVisibility(View.VISIBLE);
                    dPicker.setVisibility(View.GONE);
                }
            }
        });
    }
}
