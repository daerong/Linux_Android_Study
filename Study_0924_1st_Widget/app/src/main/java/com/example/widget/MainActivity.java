package com.example.widget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText[] EditVar = new EditText[2];
    Button[] BtnCalc = new Button[4];
    TextView TextResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditVar[0] = findViewById(R.id.var1);
        EditVar[1] = findViewById(R.id.var2);
        BtnCalc[0] = findViewById(R.id.calc_addition);
        BtnCalc[1] = findViewById(R.id.calc_subtraction);
        BtnCalc[2] = findViewById(R.id.calc_multiplication);
        BtnCalc[3] = findViewById(R.id.calc_division);
        TextResult = findViewById(R.id.result);

        BtnCalc[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double var1 = Double.parseDouble(EditVar[0].getText().toString());
                double var2 = Double.parseDouble(EditVar[1].getText().toString());
                double result = Double.parseDouble(String.format("%.3f", var1+var2));
                String str = String.valueOf(result);
                TextResult.setText(str);
            }
        });
        BtnCalc[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double var1 = Double.parseDouble(EditVar[0].getText().toString());
                double var2 = Double.parseDouble(EditVar[1].getText().toString());
                double result = Double.parseDouble(String.format("%.3f", var1-var2));
                String str = String.valueOf(result);
                TextResult.setText(str);
            }
        });
        BtnCalc[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double var1 = Double.parseDouble(EditVar[0].getText().toString());
                double var2 = Double.parseDouble(EditVar[1].getText().toString());
                double result = Double.parseDouble(String.format("%.3f", var1*var2));
                String str = String.valueOf(result);
                TextResult.setText(str);
            }
        });
        BtnCalc[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double var1 = Double.parseDouble(EditVar[0].getText().toString());
                double var2 = Double.parseDouble(EditVar[1].getText().toString());
                double result = Double.parseDouble(String.format("%.3f", var1/var2));
                String str = String.valueOf(result);
                TextResult.setText(str);
            }
        });
    }
}
