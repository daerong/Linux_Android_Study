package com.example.study_1105_1st_ndk_led;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements  RadioGroup.OnCheckedChangeListener {
    RadioGroup mRadioGroupData;
    RadioGroup mRadioGroupCtrl;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-led-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadioGroupData = findViewById(R.id.RadioGroup01);
        mRadioGroupData.setOnCheckedChangeListener(this);

        mRadioGroupCtrl = findViewById(R.id.RadioGroup02);
        mRadioGroupCtrl.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.RB01:
                ReceiveLedValue(0);
                break;
            case R.id.RB02:
                ReceiveLedValue(1);
                break;
            case R.id.RB03:
                ReceiveLedValue(2);
                break;
            case R.id.RB04:
                ReceiveLedValue(3);
                break;
            case R.id.RB05:
                ReceiveLedValue(4);
                break;
            case R.id.RB06:
                ReceiveLedValue(5);
                break;
            case R.id.RB07:
                ReceiveLedValue(6);
                break;
            case R.id.RB08:
                ReceiveLedValue(7);
                break;
            case R.id.RB10:
                ReceiveLedValue(8);
                break;
            case R.id.RB20:
                ReceiveLedValue(9);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public native String ReceiveLedValue(int x);
}
