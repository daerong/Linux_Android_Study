package com.example.termproject_2011144024_uds;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    void ReadPushSwitch(int stat) {
        mPushSwitchValue.setText("" + stat);
//        switch(stat){
//            case 1:
//                break;
//            case 2:
//                break;
//            case 4:
//                break;
//            case 8:
//                break;
//            case 16:
//                break;
//            case 32:
//                break;
//            case 64:
//                break;
//            case 128:
//                break;
//            case 256:
//                break;
//            default:
//        }
    }

    Button[][] btnArr = new Button[10][10];
    TextView cntBox;
    int mStepCnt = 0;
    int mPastLocateI;
    int mPastLocateJ;
    int mNowLocateI;
    int mNowLocateJ;
    int mTargetI;
    int mTargetJ;

    boolean isFrist = true;

    Timer timer;
    TimerTask timerTask;
    int timerCnt = 0;

    TextView mPushSwitchValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cntBox = findViewById(R.id.test_box);
        mPushSwitchValue = findViewById(R.id.my_ptr_value);

        int ret = -1;
        ret = startPushSwitchThread();

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < 10; i++){
                    for(int j = 0; j < 10; j++){
                        if(btnArr[i][j] == view){
                            mNowLocateI = i;
                            mNowLocateJ = j;
                        }
                    }
                }

                if(isFrist) {
                    view.setBackgroundColor(Color.rgb(0, 0, 255));
                    isFrist = false;

                    mPastLocateI = mNowLocateI;
                    mPastLocateJ = mNowLocateJ;

                    boolean isMatched = false;
                    do{
                        Random rnd = new Random();
                        mTargetI = rnd.nextInt(9);
                        mTargetJ = rnd.nextInt(9);
                        if(mNowLocateI == mTargetI && mNowLocateJ == mTargetJ) isMatched = true;
                    }while(isMatched);

                    btnArr[mTargetI][mTargetJ].setBackgroundColor(Color.rgb(255, 0, 0));

                    timerStart();

                }else{
                    if(canMove(mNowLocateI, mNowLocateJ, mPastLocateI, mPastLocateJ)){
                        if((mPastLocateI+mPastLocateJ)%2 == 1) btnArr[mPastLocateI][mPastLocateJ].setBackgroundColor(Color.rgb(85, 85, 85));
                        else btnArr[mPastLocateI][mPastLocateJ].setBackgroundColor(Color.rgb(0, 0, 0));

                        btnArr[mNowLocateI][mNowLocateJ].setBackgroundColor(Color.rgb(0, 0, 255));

                        mStepCnt++;
                        cntBox.setText("" + mStepCnt);

                        mPastLocateI = mNowLocateI;
                        mPastLocateJ = mNowLocateJ;
                    }

                }

//                if(WriteFnd(mStepCnt) < 0){
//                    Toast.makeText(getApplicationContext(), "FND write error", Toast.LENGTH_LONG).show();
//                }
            }
        };

        btnArr[0][0] = findViewById(R.id.btn_00);        btnArr[0][1] = findViewById(R.id.btn_01);        btnArr[0][2] = findViewById(R.id.btn_02);        btnArr[0][3] = findViewById(R.id.btn_03);        btnArr[0][4] = findViewById(R.id.btn_04);        btnArr[0][5] = findViewById(R.id.btn_05);        btnArr[0][6] = findViewById(R.id.btn_06);        btnArr[0][7] = findViewById(R.id.btn_07);        btnArr[0][8] = findViewById(R.id.btn_08);        btnArr[0][9] = findViewById(R.id.btn_09);
        btnArr[1][0] = findViewById(R.id.btn_10);        btnArr[1][1] = findViewById(R.id.btn_11);        btnArr[1][2] = findViewById(R.id.btn_12);        btnArr[1][3] = findViewById(R.id.btn_13);        btnArr[1][4] = findViewById(R.id.btn_14);        btnArr[1][5] = findViewById(R.id.btn_15);        btnArr[1][6] = findViewById(R.id.btn_16);        btnArr[1][7] = findViewById(R.id.btn_17);        btnArr[1][8] = findViewById(R.id.btn_18);        btnArr[1][9] = findViewById(R.id.btn_19);
        btnArr[2][0] = findViewById(R.id.btn_20);        btnArr[2][1] = findViewById(R.id.btn_21);        btnArr[2][2] = findViewById(R.id.btn_22);        btnArr[2][3] = findViewById(R.id.btn_23);        btnArr[2][4] = findViewById(R.id.btn_24);        btnArr[2][5] = findViewById(R.id.btn_25);        btnArr[2][6] = findViewById(R.id.btn_26);        btnArr[2][7] = findViewById(R.id.btn_27);        btnArr[2][8] = findViewById(R.id.btn_28);        btnArr[2][9] = findViewById(R.id.btn_29);
        btnArr[3][0] = findViewById(R.id.btn_30);        btnArr[3][1] = findViewById(R.id.btn_31);        btnArr[3][2] = findViewById(R.id.btn_32);        btnArr[3][3] = findViewById(R.id.btn_33);        btnArr[3][4] = findViewById(R.id.btn_34);        btnArr[3][5] = findViewById(R.id.btn_35);        btnArr[3][6] = findViewById(R.id.btn_36);        btnArr[3][7] = findViewById(R.id.btn_37);        btnArr[3][8] = findViewById(R.id.btn_38);        btnArr[3][9] = findViewById(R.id.btn_39);
        btnArr[4][0] = findViewById(R.id.btn_40);        btnArr[4][1] = findViewById(R.id.btn_41);        btnArr[4][2] = findViewById(R.id.btn_42);        btnArr[4][3] = findViewById(R.id.btn_43);        btnArr[4][4] = findViewById(R.id.btn_44);        btnArr[4][5] = findViewById(R.id.btn_45);        btnArr[4][6] = findViewById(R.id.btn_46);        btnArr[4][7] = findViewById(R.id.btn_47);        btnArr[4][8] = findViewById(R.id.btn_48);        btnArr[4][9] = findViewById(R.id.btn_49);
        btnArr[5][0] = findViewById(R.id.btn_50);        btnArr[5][1] = findViewById(R.id.btn_51);        btnArr[5][2] = findViewById(R.id.btn_52);        btnArr[5][3] = findViewById(R.id.btn_53);        btnArr[5][4] = findViewById(R.id.btn_54);        btnArr[5][5] = findViewById(R.id.btn_55);        btnArr[5][6] = findViewById(R.id.btn_56);        btnArr[5][7] = findViewById(R.id.btn_57);        btnArr[5][8] = findViewById(R.id.btn_58);        btnArr[5][9] = findViewById(R.id.btn_59);
        btnArr[6][0] = findViewById(R.id.btn_60);        btnArr[6][1] = findViewById(R.id.btn_61);        btnArr[6][2] = findViewById(R.id.btn_62);        btnArr[6][3] = findViewById(R.id.btn_63);        btnArr[6][4] = findViewById(R.id.btn_64);        btnArr[6][5] = findViewById(R.id.btn_65);        btnArr[6][6] = findViewById(R.id.btn_66);        btnArr[6][7] = findViewById(R.id.btn_67);        btnArr[6][8] = findViewById(R.id.btn_68);        btnArr[6][9] = findViewById(R.id.btn_69);
        btnArr[7][0] = findViewById(R.id.btn_70);        btnArr[7][1] = findViewById(R.id.btn_71);        btnArr[7][2] = findViewById(R.id.btn_72);        btnArr[7][3] = findViewById(R.id.btn_73);        btnArr[7][4] = findViewById(R.id.btn_74);        btnArr[7][5] = findViewById(R.id.btn_75);        btnArr[7][6] = findViewById(R.id.btn_76);        btnArr[7][7] = findViewById(R.id.btn_77);        btnArr[7][8] = findViewById(R.id.btn_78);        btnArr[7][9] = findViewById(R.id.btn_79);
        btnArr[8][0] = findViewById(R.id.btn_80);        btnArr[8][1] = findViewById(R.id.btn_81);        btnArr[8][2] = findViewById(R.id.btn_82);        btnArr[8][3] = findViewById(R.id.btn_83);        btnArr[8][4] = findViewById(R.id.btn_84);        btnArr[8][5] = findViewById(R.id.btn_85);        btnArr[8][6] = findViewById(R.id.btn_86);        btnArr[8][7] = findViewById(R.id.btn_87);        btnArr[8][8] = findViewById(R.id.btn_88);        btnArr[8][9] = findViewById(R.id.btn_89);
        btnArr[9][0] = findViewById(R.id.btn_90);        btnArr[9][1] = findViewById(R.id.btn_91);        btnArr[9][2] = findViewById(R.id.btn_92);        btnArr[9][3] = findViewById(R.id.btn_93);        btnArr[9][4] = findViewById(R.id.btn_94);        btnArr[9][5] = findViewById(R.id.btn_95);        btnArr[9][6] = findViewById(R.id.btn_96);        btnArr[9][7] = findViewById(R.id.btn_97);        btnArr[9][8] = findViewById(R.id.btn_98);        btnArr[9][9] = findViewById(R.id.btn_99);

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                btnArr[i][j].setOnClickListener(onClickListener) ;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        int ret = -1;
        ret = endPushSwitchThread();
    }


    boolean canMove(int nowI, int nowJ, int pastI, int pastJ) {
        int iSpan = nowI - pastI;
        int jSpan = nowJ - pastJ;
        if (iSpan > 1) return false;
        else if (iSpan < -1) return false;
        else if (jSpan > 1) return false;
        else if (jSpan < -1) return false;
        else return true;
    }

    public void timerStart(){
        timer = new Timer();
        timerTask= new TimerTask() {
            @Override
            public void run() {
                WriteFnd(timerCnt);
                timerCnt++;
            }

        };
        timer.schedule(timerTask, 0, 1000); //Timer 실행
    }

    public void timerStop(){
        timer.cancel();//타이머 종료
    }

    public native int ReadLed();
    public native int WriteLed(int num);
    public native String ReadFnd();
    public native int WriteFnd(int num);
    public native int WriteDot(int num);
    public native int WriteTextLcd(String str1, String str2);
    public native int ReadBuzzer();
    public native int WriteBuzzer(int stat);
    public native int startPushSwitchThread();
    public native int endPushSwitchThread();
    public native int ReadDipSwitch();
    public native int WriteStepMotor(int action, int direction, int speed);

}
