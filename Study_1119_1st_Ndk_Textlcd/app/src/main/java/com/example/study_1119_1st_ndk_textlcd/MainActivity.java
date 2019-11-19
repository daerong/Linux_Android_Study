package com.example.study_1119_1st_ndk_textlcd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText mGetEditText1;
    EditText mGetEditText2;
    TextView mPrtTextView1;
    TextView mPrtTextView2;
    Button mBtGo;
    Button mBtClear;
    public int result;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-text-lcd-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGetEditText1 = findViewById(R.id.edit_text_top);
        mGetEditText2 = findViewById(R.id.edit_text_bot);
        mPrtTextView1 = findViewById(R.id.prt_string_top);
        mPrtTextView2 = findViewById(R.id.prt_string_bot);
        mBtGo = findViewById(R.id.BT01);
        mBtGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString1 = mGetEditText1.getText().toString();
                String getString2 = mGetEditText2.getText().toString();

                int strLeng1 = getString1.length();
                int strLeng2 = getString2.length();

                if(strLeng1 > 16){
                    getString1 = "Buffet Overflow";
                }else if(strLeng1 < 1){
                    getString1 = " ";
                }

                if(strLeng2 > 16){
                    getString2 = "Buffet Overflow";
                }else if(strLeng2 < 1){
                    getString2 = " ";
                }

                ReceiveTextLcdValue(getString1, getString2);
            }
        });
        mBtClear = findViewById(R.id.BT02);
        mBtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetEditText1.setText(null);
                mGetEditText2.setText(null);
            }
        });


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int ReceiveTextLcdValue(String ptr1, String ptr2);
}
