package com.example.study_1112_1st_ndk_fnd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public EditText mGetEditText;
    public TextView mPrtTextView;
    public TextView mPutTextView;
    public Button mBtGo;
    public Button mBt01;
    public Button mBt02;
    public int result;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-fnd-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGetEditText = findViewById(R.id.my_edit_text);
        mPrtTextView = findViewById(R.id.my_prt_string);
        mPutTextView = findViewById(R.id.my_put_string);
        mBtGo = findViewById(R.id.my_button_go);
        mBtGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString = mGetEditText.getText().toString();
                mPrtTextView.setText("");

                result = ReceiveFndValue(getString);
                switch (result) {
                    case -1:
                        getString = "Device Open Error!";
                        break;
                    case 0:
                        getString = "OK [" + mGetEditText.getText().toString() + "]";
                        break;
                    case 1:
                        getString = "Check number in Textbox.";
                }
                mPutTextView.setText(getString);
            }
        });
        mBt01 = findViewById(R.id.BT01);
        mBt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetEditText.setText("8888");
                String getString = mGetEditText.getText().toString();
                mPrtTextView.setText("");

                result = ReceiveFndValue(getString);
                switch (result) {
                    case -1:
                        getString = "Device Open Error!";
                        break;
                    case 0:
                        getString = "OK [" + mGetEditText.getText().toString() + "]";
                        break;
                    case 1:
                        getString = "Check number in Textbox.";
                }
                mPutTextView.setText(getString);
            }
        });
        mBt02 = findViewById(R.id.BT02);
        mBt02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetEditText.setText("0000");
                String getString = mGetEditText.getText().toString();
                mPrtTextView.setText("");

                result = ReceiveFndValue(getString);
                switch (result) {
                    case -1:
                        getString = "Device Open Error!";
                        break;
                    case 0:
                        getString = "OK [" + mGetEditText.getText().toString() + "]";
                        break;
                    case 1:
                        getString = "Check number in Textbox.";
                }
                mPutTextView.setText(getString);
            }
        });
    }

    private native int ReceiveFndValue(String ptr);
}
