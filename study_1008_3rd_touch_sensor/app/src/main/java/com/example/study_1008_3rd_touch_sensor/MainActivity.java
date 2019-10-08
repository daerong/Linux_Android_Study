package com.example.study_1008_3rd_touch_sensor;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        String text = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                text = "터치 위치\n x = " + event.getX() + ", y = " + event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                text = "누르고 있는 위치\n x = " + event.getX() + ", y = " + event.getY();
                break;
            case MotionEvent.ACTION_UP:
                text = "뗀 위치\n x = " + event.getX() + ", y = " + event.getY();
                break;
        }

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        return false;
    }
}
