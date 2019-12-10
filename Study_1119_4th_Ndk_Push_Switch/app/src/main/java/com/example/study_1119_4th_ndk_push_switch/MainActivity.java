package com.example.study_1119_4th_ndk_push_switch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        mPushSwitchState = findViewById(R.id.my_ptr_state);;
        mPushSwitchValue = findViewById(R.id.my_ptr_value);;

        startPushSwitchThread();
    }

    @Override
    protected void onStop() {
        super.onStop();

        int ret = -1;
        ret = endPushSwitchThread();
    }

    public static void ReadPushSwitch(int stat) {
        Log.d("result", "stat = " + stat);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int startPushSwitchThread();
    public native int endPushSwitchThread();

}
