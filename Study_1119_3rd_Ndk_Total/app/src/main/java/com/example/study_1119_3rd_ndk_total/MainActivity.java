package com.example.study_1119_3rd_ndk_total;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final int LED_MIN = 0;
    private static final int LED_MAX = 255;
    private static final int FND_MAX_DIGIT = 4;
    private static final int DOT_MIN = 0;
    private static final int DOT_MAX = 9;
    private static final int TEXT_LCD_MAX_BUF = 32;
    private static final int TEXT_LCD_LINE_BUF = 16;
    private static final int BUZZER_ON = 1;
    private static final int BUZZER_OFF = 0;
    private static final int PUSH_SWITCH_MAX_BUTTON = 9;
    private static final int STEP_MOTOR_STATE_VOL = 3;
    private static final int STEP_MOTOR_ON = 0;
    private static final int STEP_MOTOR_OFF = 1;
    private static final int STEP_MOTOR_DIR_LEFT = 0;
    private static final int STEP_MOTOR_DIR_RIGHT = 1;
    private static final int STEP_MOTOR_SPDVAL_MIN = 0;
    private static final int STEP_MOTOR_SPDVAL_MAX = 255;

    EditText ledInput;
    TextView ledOutput;
    Button ledRead;
    Button ledWrite;

    EditText fndInput;
    TextView fndOutput;
    Button fndRead;
    Button fndWrite;

    EditText dotInput;
    Button dotWrite;

    EditText textLcdInput1;
    EditText textLcdInput2;
    Button textLcdWrite;

    RadioGroup buzzerRadioGroup;
    CheckBox buzzerCheckBox;
    Button buzzerRead;

    TextView pushSwitchOutput;
    Button pushSwitchRead;

    TextView dipSwitchOutput;
    Button dipSwitchRead;

    EditText stepMotorAction;
    EditText stepMotorDirection;
    EditText stepMotorSpeed;
    Button stepMotorWrite;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("fpga-total-jni");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ledInput = findViewById(R.id.led_input);
        ledOutput = findViewById(R.id.led_output);
        ledRead = findViewById(R.id.led_read);
        ledRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = ReadLed();
                ledOutput.setText(String.format("%d", result));
            }
        });
        ledWrite = findViewById(R.id.led_write);
        ledWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString = ledInput.getText().toString();
                int numReq = Integer.parseInt(getString);

                if(numReq < LED_MIN || numReq > LED_MAX){
                    Toast.makeText(getApplicationContext(), "0~255", Toast.LENGTH_LONG).show();
                    ledInput.setFocusableInTouchMode(true);
                    ledInput.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else{
                    if(WriteLed(numReq) < 0){
                        Toast.makeText(getApplicationContext(), "LED write error", Toast.LENGTH_LONG).show();
                    }else{
                        ledInput.setText(null);
                    }
                }
            }
        });

        fndInput = findViewById(R.id.fnd_input);
        fndOutput = findViewById(R.id.fnd_output);
        fndRead = findViewById(R.id.fnd_read);
        fndRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = ReadFnd();
                fndOutput.setText(result);
            }
        });
        fndWrite = findViewById(R.id.fnd_write);
        fndWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString = fndInput.getText().toString();
                int strLeng = getString.length();

                if(strLeng > FND_MAX_DIGIT){
                    Toast.makeText(getApplicationContext(), "0~9999", Toast.LENGTH_LONG).show();
                    fndInput.setFocusableInTouchMode(true);
                    fndInput.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else{
                    if(WriteFnd(getString) < 0){
                        Toast.makeText(getApplicationContext(), "FND write error", Toast.LENGTH_LONG).show();
                    }else{
                        fndInput.setText(null);
                    }
                }
            }
        });

        dotInput = findViewById(R.id.dot_input);
        dotWrite = findViewById(R.id.dot_write);
        dotWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString = dotInput.getText().toString();
                if(getString == ""){
                    Toast.makeText(getApplicationContext(), "0~9", Toast.LENGTH_LONG).show();
                    dotInput.setFocusableInTouchMode(true);
                    dotInput.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else{
                    int num = Integer.parseInt(getString);
                    if(num > DOT_MAX || num < DOT_MIN){
                        Toast.makeText(getApplicationContext(), "0~9", Toast.LENGTH_LONG).show();
                        dotInput.setFocusableInTouchMode(true);
                        dotInput.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }else{
                        if(WriteDot(num) < 0){
                            Toast.makeText(getApplicationContext(), "Dot Matrix write error", Toast.LENGTH_LONG).show();
                        }else{
                            dotInput.setText(null);
                        }
                    }
                }
            }
        });

        textLcdInput1 = findViewById(R.id.text_lcd_input1);
        textLcdInput2 = findViewById(R.id.text_lcd_input2);
        textLcdWrite = findViewById(R.id.text_lcd_write);
        textLcdWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getString1 = textLcdInput1.getText().toString();
                String getString2 = textLcdInput2.getText().toString();

                int strLeng1 = getString1.length();
                int strLeng2 = getString2.length();

                if(strLeng1 > TEXT_LCD_LINE_BUF){
                    getString1 = "Buffet Overflow";
                }else if(strLeng1 < 1){
                    getString1 = " ";
                }

                if(strLeng2 > TEXT_LCD_LINE_BUF){
                    getString2 = "Buffet Overflow";
                }else if(strLeng2 < 1){
                    getString2 = " ";
                }

                if(WriteTextLcd(getString1, getString2) < 0){
                    Toast.makeText(getApplicationContext(), "Text LCD write error", Toast.LENGTH_LONG).show();
                }else{
                    textLcdInput1.setText(null);
                    textLcdInput2.setText(null);
                }
            }
        });

        buzzerRadioGroup = findViewById(R.id.buzzer_radio_group);
        buzzerRadioGroup.setOnCheckedChangeListener(this);
        buzzerCheckBox = findViewById(R.id.buzzer_check);
        buzzerRead = findViewById(R.id.buzzer_read);
        buzzerRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = ReadBuzzer();
                if(result < 0){
                    Toast.makeText(getApplicationContext(), "Buzzer read error", Toast.LENGTH_LONG).show();
                }else{
                    switch (result){
                        case BUZZER_OFF:
                            buzzerCheckBox.setChecked(false);
                            break;
                        case BUZZER_ON:
                            buzzerCheckBox.setChecked(true);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Buzzer read error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        pushSwitchOutput = findViewById(R.id.push_switch_output);
        pushSwitchRead = findViewById(R.id.push_switch_read);
        pushSwitchRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = ReadPushSwitch();
                pushSwitchOutput.setText(result);
            }
        });

        dipSwitchOutput = findViewById(R.id.dip_switch_output);
        dipSwitchRead = findViewById(R.id.dip_switch_read);
        dipSwitchRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = ReadDipSwitch();
                if(result < 0){
                    Toast.makeText(getApplicationContext(), "Buzzer read error", Toast.LENGTH_LONG).show();
                }else{
                    String binaryType = Integer.toBinaryString(result);
                    dipSwitchOutput.setText(reverseString(binaryType));
                }
            }
        });

        stepMotorAction = findViewById(R.id.step_motor_action);
        stepMotorDirection = findViewById(R.id.step_motor_direction);
        stepMotorSpeed = findViewById(R.id.step_motor_speed);
        stepMotorWrite = findViewById(R.id.step_motor_write);
        stepMotorWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getAction = stepMotorAction.getText().toString();
                String getDirction = stepMotorDirection.getText().toString();
                String getSpeed = stepMotorSpeed.getText().toString();
                if(getAction.equals("")){
                    Toast.makeText(getApplicationContext(), "OFF = 0, ON = 1", Toast.LENGTH_LONG).show();
                    stepMotorAction.setFocusableInTouchMode(true);
                    stepMotorAction.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else if(getDirction.equals("")){
                    Toast.makeText(getApplicationContext(), "RIGHT = 0, LEFT = 1", Toast.LENGTH_LONG).show();
                    stepMotorDirection.setFocusableInTouchMode(true);
                    stepMotorDirection.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else if(getSpeed.equals("")){
                    Toast.makeText(getApplicationContext(), "SPEED : 0~255", Toast.LENGTH_LONG).show();
                    stepMotorSpeed.setFocusableInTouchMode(true);
                    stepMotorSpeed.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else{
                    int action = Integer.parseInt(getAction);
                    int direction = Integer.parseInt(getDirction);
                    int speed = Integer.parseInt(getSpeed);

                    if(action != STEP_MOTOR_ON && action != STEP_MOTOR_OFF){
                        Toast.makeText(getApplicationContext(), "OFF = 0, ON = 1", Toast.LENGTH_LONG).show();
                        stepMotorAction.setFocusableInTouchMode(true);
                        stepMotorAction.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }else if(direction != STEP_MOTOR_DIR_LEFT && direction != STEP_MOTOR_DIR_RIGHT) {
                        Toast.makeText(getApplicationContext(), "RIGHT = 0, LEFT = 1", Toast.LENGTH_LONG).show();
                        stepMotorDirection.setFocusableInTouchMode(true);
                        stepMotorDirection.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }else if(speed < STEP_MOTOR_SPDVAL_MIN || speed > STEP_MOTOR_SPDVAL_MAX) {
                        Toast.makeText(getApplicationContext(), "SPEED : 0~255", Toast.LENGTH_LONG).show();
                        stepMotorSpeed.setFocusableInTouchMode(true);
                        stepMotorSpeed.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }else{
                        if(WriteStepMotor(action, direction, speed) < 0){
                            Toast.makeText(getApplicationContext(), "Step Motor write error", Toast.LENGTH_LONG).show();
                        }else{

                        }
                    }
                }
            }
        });
    }

    public static String reverseString(String s){
        return (new StringBuffer(s)).reverse().toString();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.buzzer_on:
                WriteBuzzer(1);
                break;
            case R.id.buzzer_off:
                WriteBuzzer(0);
                break;
        }
    }




    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int ReadLed();
    public native int WriteLed(int num);
    public native String ReadFnd();
    public native int WriteFnd(String text);
    public native int WriteDot(int num);
    public native int WriteTextLcd(String str1, String str2);
    public native int ReadBuzzer();
    public native int WriteBuzzer(int stat);
    public native String ReadPushSwitch();
    public native int ReadDipSwitch();
    public native int WriteStepMotor(int action, int direction, int speed);
}
