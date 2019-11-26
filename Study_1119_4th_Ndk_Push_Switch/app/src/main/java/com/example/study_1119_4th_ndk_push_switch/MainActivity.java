package com.example.study_1119_4th_ndk_push_switch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView mPushSwitchState;
    TextView mPushSwitchValue;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-push-switch-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPushSwitchValue = findViewById(R.id.my_ptr_value);
        mPushSwitchState = findViewById(R.id.my_ptr_state);
        TimerTask timerTask = new TimerTask() {
            Handler handler = new Handler();

            @Override
            public void run() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int value;
                        value = DeviceOpen();

                        if(value != -1) {
                            value = ReceivePushSwitchValue();
                            DeviceClose();
                            String bin = Integer.toBinaryString(value);
                            mPushSwitchValue.setText(bin);
                        }
                    }
                }, 100);
            }
        };

        Timer t = new Timer();
        t.schedule(timerTask, 300, 300);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int DeviceOpen();
    public native int DeviceClose();
    public native int ReceivePushSwitchValue();

}
