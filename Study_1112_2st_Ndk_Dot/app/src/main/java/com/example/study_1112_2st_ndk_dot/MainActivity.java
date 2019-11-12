package com.example.study_1112_2st_ndk_dot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    RadioGroup mRadioGroupData;
    RadioGroup mRadioGroupCtrl;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-dot-jni");
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
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId){
            case R.id.RB00:
                ReceiveDotValue(0);
                break;
            case R.id.RB01:
                ReceiveDotValue(1);
                break;
            case R.id.RB02:
                ReceiveDotValue(2);
                break;
            case R.id.RB03:
                ReceiveDotValue(3);
                break;
            case R.id.RB04:
                ReceiveDotValue(4);
                break;
            case R.id.RB05:
                ReceiveDotValue(5);
                break;
            case R.id.RB06:
                ReceiveDotValue(6);
                break;
            case R.id.RB07:
                ReceiveDotValue(7);
                break;
            case R.id.RB08:
                ReceiveDotValue(8);
                break;
            case R.id.RB09:
                ReceiveDotValue(9);
                break;
            case R.id.RB10:
                ReceiveDotValue(10);
                break;
            case R.id.RB20:
                ReceiveDotValue(11);
                break;
        }
    }

    public native String ReceiveDotValue(int x);
}
