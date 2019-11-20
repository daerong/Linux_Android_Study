package com.example.study_1119_2nd_ndk_buzzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView mStateTextView;
    Button mBtOn;
    Button mBtOff;
    public int result;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-buzzer-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStateTextView = findViewById(R.id.prt_state);
        mBtOn = findViewById(R.id.BT01);
        mBtOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = ReceiveBuzzerValue(1);
                if(result < 0){
                    mStateTextView.setText("Device Open Error!");
                }else{
                    mStateTextView.setText("Buzzer On!");
                }
            }
        });
        mBtOff = findViewById(R.id.BT02);
        mBtOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = ReceiveBuzzerValue(0);
                if(result < 0){
                    mStateTextView.setText("Device Open Error!");
                }else{
                    mStateTextView.setText("Buzzer Off!");
                }
            }
        });


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int ReceiveBuzzerValue(int x);
}
