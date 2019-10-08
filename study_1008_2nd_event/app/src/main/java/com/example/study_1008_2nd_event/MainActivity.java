package com.example.study_1008_2nd_event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_result = findViewById(R.id.tv_result);

        findViewById(R.id.btn_car).setOnClickListener(mClickListener);
        findViewById(R.id.btn_airplane).setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_car :
                    tv_result.setText("Car");
                    break;
                case R.id.btn_airplane :
                    tv_result.setText("Airplane");
                    break;
            }
        }
    };
}
