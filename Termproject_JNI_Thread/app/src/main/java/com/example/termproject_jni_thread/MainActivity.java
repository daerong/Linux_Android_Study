package com.example.termproject_jni_thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
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

    private static final int PLAYER = 1;
    private static final int TARGET = 2;
    private static final int WORK = 1;
    private static final int END = 0;
    private static final int HINT_ON = 1;
    private static final int HINT_OFF = 0;

    RelativeLayout gameBackground;
    ImageView gamePlayer;
    ImageView gameTarget;
    ImageButton startBtn;
    ImageButton hintBtn;

    static GameBoard gameClass = new GameBoard();
    static int playerX;
    static int playerY;
    static int targetX;
    static int targetY;
    static int tryCount;
    static int gameStat;
    static int hintStat;

    static {
        System.loadLibrary("jni-thread");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBackground = findViewById(R.id.game_background);
        gamePlayer = findViewById(R.id.game_player);
        gameTarget = findViewById(R.id.game_target);
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(clickListener);
        hintBtn = findViewById(R.id.hint_btn);
        hintBtn.setOnClickListener(clickListener);

        playerX = gameClass.getLocateX(PLAYER);
        playerY = gameClass.getLocateY(PLAYER);

        targetX = gameClass.getLocateX(TARGET);
        targetY = gameClass.getLocateY(TARGET);

        tryCount = 0;
        WriteTextLCD();

        gameStat = END;
        hintStat = HINT_OFF;
        DrowBoard();

        pushSwitchThreadStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        pushSwitchThreadEnd();
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.start_btn:
                    if(gameStat == END){
                        gameStat = WORK;
                        startBtn.setImageResource(R.drawable.give_up_btn);
                        fndThreadStart();
                    }else{
                        gameStat = END;
                        startBtn.setImageResource(R.drawable.start_btn);
                        fndThreadEnd();
                    }
                    break;
                case R.id.hint_btn:
                    if(hintStat == HINT_OFF){
                        hintStat = HINT_ON;
                        hintBtn.setImageResource(R.drawable.hint_off_btn);
                    }else{
                        hintStat = HINT_OFF;
                        hintBtn.setImageResource(R.drawable.hint_on_btn);
                    }
                    break;
            }
        }
    };

    public void ReadPushSwitch(int stat) {
        Log.d("result", "stat = " + stat);

//        if(dotThreadCheck() == -1) {
//            WriteTextLcd("STEP : " + tryCount, "Thread is working");
//            return;
//        }
//
//        // PLAYER 이동
//        if(gameClass.MovePlayer(stat) > 0){
//            tryCount++;
//            WriteTextLCD();
//        }else if(gameClass.MovePlayer(stat) == 0){
//            gameStat = END;
//            startBtn.setImageResource(R.drawable.start_btn);
//            fndThreadEnd();
//        }
//
//        playerX = gameClass.getLocateX(PLAYER);
//        playerY = gameClass.getLocateY(PLAYER);
//
//        dotThreadStart();
//
//        // TARGET 이동
//        if(gameClass.RunAway(2) > 0){       // 도망갔을 때,
//            WriteStepMotor(STEP_MOTOR_ON, STEP_MOTOR_DIR_RIGHT, 100);
//        }

    }

    public void DrowBoard(){
        gamePlayer.setX(gamePlayer.getWidth()*playerX);
        gamePlayer.setY(gamePlayer.getHeight()*playerY);

        gameTarget.setX(gameTarget.getWidth()*targetX);
        gameTarget.setY(gameTarget.getHeight()*targetY);
    }

    public void WriteTextLCD(){
        WriteTextLcd("STEP : " + tryCount, "");
    }

    public native int pushSwitchThreadStart(); // pushSwitch ON
    public native int pushSwitchThreadEnd();   // pushSwitch OFF
    public native int fndThreadStart();        // 시간 계수, 타이머 시작
    public native int fndThreadEnd();          // 시간 중지, 타이머 종료
    public native int dotThreadStart();        // 5초동안 돌다가 빠져나옴
    public native int dotThreadCheck();         // dotThread가 동작중인지 확인

    public native int ReadLed();
    public native int WriteLed(int num);                                        // 범인 거리를 세팅
    public native int WriteTextLcd(String str1, String str2);                   // 범인이나 선임이 하는 말 넣으면 재미있을 듯
    public native int WriteBuzzer(int stat);                                    // 검거 시 울림
    public native int ReadDipSwitch();                                          // 옵션 확인
    public native int WriteStepMotor(int action, int direction, int speed);     // 도망 방향, 얼마나 근접했는 지.

}
