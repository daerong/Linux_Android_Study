package com.example.study_1119_4th_ndk_push_switch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView mPushSwitchState;
    TextView mPushSwitchValue;

    private ImageView gamePlayer;

    private int playerX = 0;
    private int playerY = 0;

    private void ReadPushSwitch(int stat) {
        Log.d("result", "stat = " + stat);

        if(stat == 0) return;

        Log.d("result", "Check 1");

        switch (stat){
            case 1:
                playerX--;
                playerY--;
                break;
            case 2:
                playerY--;
                break;
            case 4:
                playerX++;
                playerY--;
                break;
            case 8:
                playerX--;
                break;
            case 16:
                break;
            case 32:
                playerX++;
                break;
            case 64:
                playerX--;
                playerY++;
                break;
            case 128:
                playerY++;
                break;
            case 256:
                playerX--;
                playerY++;
                break;
        }
        Log.d("result", "Check 2");

        Log.d("result", "x = " + playerX + ", y = " + playerY);

//        Log.d("result", "w = " + gamePlayer.getWidth() + ", h = " + gamePlayer.getHeight());
//        gamePlayer.setX(gamePlayer.getWidth()*playerX);
//        gamePlayer.setY(gamePlayer.getHeight()*playerY);

        Log.d("result", "Check 3");
    }

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

        playerX = 0;
        playerY = 0;

        gamePlayer = findViewById(R.id.game_player);

        startPushSwitchThread();
    }

    @Override
    protected void onStop() {
        super.onStop();

        int ret = -1;
        ret = endPushSwitchThread();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int startPushSwitchThread();
    public native int endPushSwitchThread();

}
