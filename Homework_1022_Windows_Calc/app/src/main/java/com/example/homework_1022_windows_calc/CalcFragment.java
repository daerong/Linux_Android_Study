package com.example.homework_1022_windows_calc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

public class CalcFragment extends Fragment {

    private Button b0;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b_claer;
    private Button b_back;
    private Button b_devide;
    private Button b_plus;
    private Button b_minus;
    private Button b_multi;
    private Button b_equal;
    private Button b_dot;
    private Button b_save;
    private Button b_read;

    private TextView formula;
    private TextView result;

    Queue<String> operatorQueue;
    Queue<String> numberQueue;
    private String nowString;

    private Context context;

    String flashMem = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View linearLayout = inflater.inflate(R.layout.fragment_calc, container,false);

        context = container.getContext();

        result = linearLayout.findViewById( R.id.text_result );
        formula = linearLayout.findViewById( R.id.text_formula );

        operatorQueue = new LinkedList<>();
        numberQueue = new LinkedList<>();
        nowString = "";

        b0 = linearLayout.findViewById(R.id.button_0);
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nowString.equals("0")) {
                    nowString += "0";
                    formula.setText(formula.getText() + "0");
                }
            }
        });
        b1 = linearLayout.findViewById(R.id.button_1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "1";
                }
                formula.setText(formula.getText() + "1");
            }
        });
        b2 = linearLayout.findViewById(R.id.button_2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "2";
                }
                formula.setText(formula.getText() + "2");
            }
        });
        b3 = linearLayout.findViewById(R.id.button_3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "3";
                }
                formula.setText(formula.getText() + "3");
            }
        });
        b4 = linearLayout.findViewById(R.id.button_4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "4";
                }
                formula.setText(formula.getText() + "4");
            }
        });
        b5 = linearLayout.findViewById(R.id.button_5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "5";
                }
                formula.setText(formula.getText() + "5");
            }
        });
        b6 = linearLayout.findViewById(R.id.button_6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "6";
                }
                formula.setText(formula.getText() + "6");
            }
        });
        b7 = linearLayout.findViewById(R.id.button_7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "7";
                }
                formula.setText(formula.getText() + "7");
            }
        });
        b8 = linearLayout.findViewById(R.id.button_8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "8";
                }
                formula.setText(formula.getText() + "8");
            }
        });
        b9 = linearLayout.findViewById(R.id.button_9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.equals("0")) {
                    nowString = "9";
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }else{
                    nowString += "9";
                }
                formula.setText(formula.getText() + "9");
            }
        });
        b_claer = linearLayout.findViewById( R.id.button_clear );
        b_claer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowString = "";
                formula.setText("");
                result.setText("");
            }
        });
        b_back = linearLayout.findViewById( R.id.button_back );
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() == 0) {
                    if(!operatorQueue.isEmpty()){
                        operatorQueue.poll();
                        nowString = numberQueue.poll();
                        formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                    }
                }else{
                    nowString = nowString.substring(0, nowString.length()-1);
                    formula.setText( formula.getText().subSequence( 0 , formula.getText().length()-1));
                }
            }
        });
        b_devide = linearLayout.findViewById( R.id.button_devide );
        b_devide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() != 0 && nowString.charAt(nowString.length() - 1) != '.') {
                    numberQueue.add(nowString);
                    nowString = "";
                    operatorQueue.add("/");
                    formula.setText(formula.getText()+"/");
                }
            }
        });
        b_plus = linearLayout.findViewById( R.id.button_plus );
        b_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() != 0 && nowString.charAt(nowString.length() - 1) != '.') {
                    numberQueue.add(nowString);
                    nowString = "";
                    operatorQueue.add("+");
                    formula.setText( formula.getText()+"+");
                }
            }
        });
        b_minus = linearLayout.findViewById( R.id.button_minus );
        b_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() != 0 && nowString.charAt(nowString.length() - 1) != '.') {
                    numberQueue.add(nowString);
                    nowString = "";
                    operatorQueue.add("-");
                    formula.setText(formula.getText()+"-");
                }
            }
        });
        b_multi = linearLayout.findViewById( R.id.button_multi );
        b_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() != 0 && nowString.charAt(nowString.length() - 1) != '.') {
                    numberQueue.add(nowString);
                    nowString = "";
                    operatorQueue.add("*");
                    formula.setText(formula.getText()+"*");
                }
            }
        });
        b_equal = linearLayout.findViewById( R.id.button_equal );
        b_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() != 0 && nowString.matches("-?\\d+(\\.\\d+)?")){
                    numberQueue.add(nowString);

                    Log.d("Before numArr.size : ", "" + numberQueue.size());
                    Log.d("Before operArr.size : ", "" + operatorQueue.size());

                    Double calc_result = calc();
                    nowString = String.valueOf(calc_result);

                    ((MainActivity)getActivity()).callHistoryAdd(formula.getText().toString(), nowString);

                    formula.setText(nowString);
                    result.setText(nowString);

                    Log.d("After numArr.size : ", "" + numberQueue.size());
                    Log.d("After operArr.size : ", "" + operatorQueue.size());
                }
            }
        });
        b_dot = linearLayout.findViewById( R.id.button_dot );
        b_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() != 0 && !nowString.contains(".")){
                    nowString += ".";
                    formula.setText(formula.getText()+".");
                }
            }
        });
        b_save = linearLayout.findViewById(R.id.button_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashMem = result.getText().toString();
                Toast.makeText(context, "결과 " + flashMem + "가 메모리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        b_read = linearLayout.findViewById(R.id.button_read);
        b_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowString.length() == 0) {
                    nowString = flashMem;
                    formula.setText(formula.getText()+nowString);
                }else{
                    formula.setText(formula.getText().subSequence( 0 , formula.getText().length()-nowString.length()));
                    nowString = flashMem;
                    formula.setText(formula.getText()+nowString);
                }
                Toast.makeText(context, "메모리 " + flashMem + "를 불러왔습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return linearLayout;
    }

    private Double calc() {
        LinkedList<Double> linkedList = new LinkedList<>();
        // 연결리스트를 이용해서 풀이하기

        while(!numberQueue.isEmpty()){
            linkedList.add(Double.valueOf(numberQueue.poll()));
            Log.d("Array", "add");
        }

        int idx = 0;
        String sam = "";

        while(idx < operatorQueue.size()){
            sam = operatorQueue.peek();
            Log.d("Operate * /", sam +", idx = " + idx);
            switch (sam){
                case "+":
                    idx++;
                    break;
                case "-":
                    idx++;
                    break;
                case "*":
                    operatorQueue.poll();
                    linkedList.set(idx, linkedList.get(idx) * linkedList.get(idx+1));
                    linkedList.remove(idx+1);
                    break;
                case "/":
                    operatorQueue.poll();
                    linkedList.set(idx, linkedList.get(idx) / linkedList.get(idx+1));
                    linkedList.remove(idx+1);
                    break;
                default:
                    idx++;
                    break;
            }
        }

        idx = 0;
        while(idx < operatorQueue.size()){
            sam = operatorQueue.peek();
            Log.d("Operate + -", sam +", idx = " + idx);
            switch (sam){
                case "+":
                    operatorQueue.poll();
                    linkedList.set(idx, linkedList.get(idx) + linkedList.get(idx+1));
                    linkedList.remove(idx+1);
                    break;
                case "-":
                    operatorQueue.poll();
                    linkedList.set(idx, linkedList.get(idx) - linkedList.get(idx+1));
                    linkedList.remove(idx+1);
                    break;
                default:
                    idx++;
                    break;
            }
        }

        return linkedList.get(0);
    }
}
