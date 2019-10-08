package com.example.study_1008_4th_image_move;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView iv_smile;
    Vibrator mVibe;
    int screen_width;
    int screen_height;
    int iv_height;
    int iv_width;
    int x;
    int y;
    int previous_x;
    int previous_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        iv_smile = findViewById(R.id.smile);

        mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Display display = getWindowManager().getDefaultDisplay();
        screen_width = display.getWidth();
        screen_height = display.getHeight();

        iv_smile.measure(iv_smile.getMeasuredWidth(), iv_smile.getMeasuredHeight());
        iv_height = iv_smile.getMeasuredHeight();
        iv_width = iv_smile.getMeasuredWidth();

        x = screen_width / 2 - iv_width / 2;
        y = screen_height / 2 - iv_height / 2;
        previous_x = x;
        previous_y = y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int touch_x = (int) event.getX();
                int touch_y = (int) event.getY();
                x = touch_x - iv_width / 2;
                y = touch_y - iv_height / 2;

                ObjectAnimator smileX = ObjectAnimator.ofFloat(iv_smile, "translationX", previous_x, x);
                ObjectAnimator smileY = ObjectAnimator.ofFloat(iv_smile, "translationY", previous_y, y);
                smileX.start();
                smileY.start();

//                mVibe.vibrate(50);

                previous_x = x;
                previous_y = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}
