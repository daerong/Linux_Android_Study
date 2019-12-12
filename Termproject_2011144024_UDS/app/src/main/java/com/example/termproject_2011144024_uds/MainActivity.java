package com.example.termproject_2011144024_uds;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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

    Timer fndTimer;
    TimerTask fndTimerTask;
    Timer pushSwitchTimer;
    TimerTask pushSwitchTimerTask;
    CountDownTimer dotCountDownTimer;
    CountDownTimer buzzerCountDownTimer;
    CountDownTimer stepMotorCountDownTimer;

    RelativeLayout gameBackground;
    ImageView gamePlayer;
    ImageView gameTarget;
    ImageButton startBtn;
    ImageButton hintBtn;

    GameBoard gameClass = new GameBoard();
    int playerX;
    int playerY;
    int targetX;
    int targetY;
    int tryCount;
    int gameStat;
    int hintStat;
    int fndTimerCnt;
    boolean dotIsRunning;
    boolean buzzerIsRunning;
    int buzzerStat;
    boolean stepMotorIsRunning;

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

        dotIsRunning = false;
        buzzerIsRunning = false;
        buzzerStat = BUZZER_OFF;
        stepMotorIsRunning = false;

        tryCount = 0;
        WriteTextLcd(" ", " ");

        gameStat = END;
        hintStat = HINT_OFF;
        DrowBoard();
    }

    public void fndTimerStart(){
        fndTimerCnt = 0;

        fndTimer = new Timer();
        fndTimerTask = new TimerTask() {
            @Override
            public void run() {
                WriteFnd(fndTimerCnt);
                fndTimerCnt++;
            }
        };
        fndTimer.schedule(fndTimerTask, 0, 1000); //Timer 실행
    }
    public void fndTimerEnd(){
        fndTimer.cancel();//타이머 종료
    }

    public void pushSwitchTimerStart(){
        pushSwitchTimer = new Timer();
        pushSwitchTimerTask = new TimerTask() {
            @Override
            public void run() {
                int stat = ReadPushSwitch();
                if(stat > 0 || !dotIsRunning) MoveStart(stat);
            }
        };
        pushSwitchTimer.schedule(pushSwitchTimerTask, 0, 100); //Timer 실행
    }
    public void pushSwitchTimerEnd(){
        pushSwitchTimer.cancel();//타이머 종료
    }


    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.start_btn:
                    if(gameStat == END){
                        gameStat = WORK;
                        fndTimerStart();
                        pushSwitchTimerStart();
                        startBtn.setImageResource(R.drawable.give_up_btn);
                    }else{
                        gameStat = END;
                        fndTimerEnd();
                        pushSwitchTimerEnd();
                        startBtn.setImageResource(R.drawable.start_btn);
                    }
                    break;
                case R.id.hint_btn:
                    if(hintStat == HINT_OFF){
                        hintStat = HINT_ON;
                        WriteLed(CalcLED(gameClass.getDistance()));

                        if(!buzzerIsRunning){
                            buzzerCountDownTimer = new CountDownTimer(2000, 200) {
                                public void onTick(long millisUntilFinished) {
                                    buzzerIsRunning = true;
                                    if(buzzerStat == BUZZER_OFF) {
                                        WriteBuzzer(BUZZER_ON);
                                        buzzerStat = BUZZER_ON;
                                    }else{
                                        WriteBuzzer(BUZZER_OFF);
                                        buzzerStat = BUZZER_OFF;
                                    }
                                }

                                public void onFinish() {
                                    WriteBuzzer(BUZZER_OFF);
                                    buzzerIsRunning = false;
                                }
                            }.start();
                        }

                        hintBtn.setImageResource(R.drawable.hint_off_btn);
                    }else{
                        hintStat = HINT_OFF;
                        hintBtn.setImageResource(R.drawable.hint_on_btn);
                    }
                    break;
            }
        }
    };

    public void DrowBoard(){
        gamePlayer.setX(gamePlayer.getWidth()*playerX);
        gamePlayer.setY(gamePlayer.getHeight()*playerY);

        gameTarget.setX(gameTarget.getWidth()*targetX);
        gameTarget.setY(gameTarget.getHeight()*targetY);
    }

    public int CalcLED(int distance){
        if(distance < 8) return (int) Math.pow(2, distance);
        else return 128;
    }

    private void MoveStart(int stat) {
        // PLAYER 이동
        if(gameClass.MovePlayer(stat) > 0){
            tryCount++;
            WriteTextLcd("STEP : " + tryCount, " ");
        }else if(gameClass.MovePlayer(stat) == 0){
            gameStat = END;
            fndTimerEnd();
            pushSwitchTimerEnd();
            startBtn.setImageResource(R.drawable.start_btn);
        }
        playerX = gameClass.getLocateX(PLAYER);
        playerY = gameClass.getLocateY(PLAYER);

        // 타이머 실행
        if(!dotIsRunning){
            dotCountDownTimer = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                    dotIsRunning = true;
                    WriteDot((int) (millisUntilFinished/1000));
                }

                public void onFinish() {
                    dotIsRunning = false;
                }
            }.start();
        }

        // TARGET 이동
        if(gameClass.RunAway(2) > 0){       // 도망갔을 때,
            if(!stepMotorIsRunning){
                stepMotorCountDownTimer = new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        stepMotorIsRunning = true;
                        WriteStepMotor(STEP_MOTOR_ON, STEP_MOTOR_DIR_RIGHT, 100);
                    }

                    public void onFinish() {
                        stepMotorIsRunning = false;
                        WriteStepMotor(STEP_MOTOR_OFF, STEP_MOTOR_DIR_LEFT, 100);
                    }
                }.start();
            }
        }
        targetX = gameClass.getLocateX(TARGET);
        targetY = gameClass.getLocateY(TARGET);

        // 화면 업데이트
        DrowBoard();
    }

    public native int ReadLed();
    public native int WriteLed(int num);
    public native String ReadFnd();
    public native int WriteFnd(int num);
    public native int WriteDot(int num);
    public native int WriteTextLcd(String str1, String str2);
    public native int ReadBuzzer();
    public native int WriteBuzzer(int stat);
    public native int ReadPushSwitch();
    public native int ReadDipSwitch();
    public native int WriteStepMotor(int action, int direction, int speed);

}
