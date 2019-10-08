package com.example.study_1008_1st_table_grid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button[] tb_btn = new Button[15];
    Button[] gd_btn = new Button[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb_btn[0] = findViewById(R.id.tb_0);
        gd_btn[0] = findViewById(R.id.gd_0);
        tb_btn[1] = findViewById(R.id.tb_1);
        gd_btn[1] = findViewById(R.id.gd_1);
        tb_btn[2] = findViewById(R.id.tb_2);
        gd_btn[2] = findViewById(R.id.gd_2);
        tb_btn[3] = findViewById(R.id.tb_3);
        gd_btn[3] = findViewById(R.id.gd_3);
        tb_btn[4] = findViewById(R.id.tb_4);
        gd_btn[4] = findViewById(R.id.gd_4);
        tb_btn[5] = findViewById(R.id.tb_5);
        gd_btn[5] = findViewById(R.id.gd_5);
        tb_btn[6] = findViewById(R.id.tb_6);
        gd_btn[6] = findViewById(R.id.gd_6);
        tb_btn[7] = findViewById(R.id.tb_7);
        gd_btn[7] = findViewById(R.id.gd_7);
        tb_btn[8] = findViewById(R.id.tb_8);
        gd_btn[8] = findViewById(R.id.gd_8);
        tb_btn[9] = findViewById(R.id.tb_9);
        gd_btn[9] = findViewById(R.id.gd_9);
        tb_btn[10] = findViewById(R.id.tb_dot);
        gd_btn[10] = findViewById(R.id.gd_dot);
        tb_btn[11] = findViewById(R.id.tb_plus);
        gd_btn[11] = findViewById(R.id.gd_plus);
        tb_btn[12] = findViewById(R.id.tb_minus);
        gd_btn[12] = findViewById(R.id.gd_minus);
        tb_btn[13] = findViewById(R.id.tb_multiple);
        gd_btn[13] = findViewById(R.id.gd_multiple);
        tb_btn[14] = findViewById(R.id.tb_divide);
        gd_btn[14] = findViewById(R.id.gd_divide);

        for(int i = 0 ; i < 15 ; i++)
        {
            final int finalI = i;
            tb_btn[finalI].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        gd_btn[finalI].setBackgroundColor(Color.TRANSPARENT);
                    } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        gd_btn[finalI].setBackgroundColor(Color.RED);
                    }

                    return false;
                }
            });

            gd_btn[finalI].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        gd_btn[finalI].setBackgroundColor(Color.TRANSPARENT);
                    } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        gd_btn[finalI].clearAnimation();
                    }

                    return false;
                }
            });
        }

    }
}
