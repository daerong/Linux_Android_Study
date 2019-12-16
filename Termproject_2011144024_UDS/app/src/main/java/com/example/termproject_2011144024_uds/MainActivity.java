package com.example.termproject_2011144024_uds;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
    private static final int DIP_SWITCH_MAX_BUTTON = 8;
    private static final int STEP_MOTOR_STATE_VOL = 3;
    private static final int STEP_MOTOR_ON = 1;
    private static final int STEP_MOTOR_OFF = 0;
    private static final int STEP_MOTOR_DIR_LEFT = 0;
    private static final int STEP_MOTOR_DIR_RIGHT = 1;
    private static final int STEP_MOTOR_SPDVAL_MIN = 0;
    private static final int STEP_MOTOR_SPDVAL_MAX = 255;

    private static final int PLAYER = 1;
    private static final int TARGET = 2;
    private static final int WORK = 1;
    private static final int END = 0;

    Timer fndTimer;
    TimerTask fndTimerTask;
    Timer pushSwitchTimer;
    TimerTask pushSwitchTimerTask;
    Timer dipSwitchTimer;
    TimerTask dipSwitchTimerTask;
    CountDownTimer dotCountDownTimer;
    CountDownTimer buzzerCountDownTimer;
    CountDownTimer stepMotorCountDownTimer;

    RelativeLayout gameBackground;
    View gamePlayer;
    View gameTarget;
    ImageButton startBtn;
    ImageButton giveUpBtn;
    ImageButton hintBtn;
    ImageView[] dipStatBtnOn = new ImageView[8];
    ImageView[] dipStatBtnOff = new ImageView[8];

    GameBoard gameClass;
    int playerX;
    int playerY;
    int targetX;
    int targetY;
    int tryCount;
    int fndTimerCnt;
    boolean dotIsRunning;
    boolean buzzerIsRunning;
    int buzzerStat;
    boolean stepMotorIsRunning;
    boolean[] dipStat;

    static {
        System.loadLibrary("term-project");
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
        giveUpBtn = findViewById(R.id.give_up_btn);
        giveUpBtn.setOnClickListener(clickListener);
        hintBtn = findViewById(R.id.hint_btn);
        hintBtn.setOnClickListener(clickListener);
        dipStatBtnOn[0] = findViewById(R.id.dip_status_1_on);       // dipStat[0] : 범인 감지 거리 + 1
        dipStatBtnOn[1] = findViewById(R.id.dip_status_2_on);       // dipStat[1] : 범인 감지 거리 + 2
        dipStatBtnOn[2] = findViewById(R.id.dip_status_3_on);       // dipStat[2] : BUZZER 켜기
        dipStatBtnOn[3] = findViewById(R.id.dip_status_4_on);       // dipStat[3] : STEP MOTOR 켜기
        dipStatBtnOn[4] = findViewById(R.id.dip_status_5_on);       // dipStat[4] : 연속 이동 가능
        dipStatBtnOn[5] = findViewById(R.id.dip_status_6_on);       // dipStat[5] : 범인 도망금지
        dipStatBtnOn[6] = findViewById(R.id.dip_status_7_on);       // dipStat[6] : 힌트 항상 켜놓기
        dipStatBtnOn[7] = findViewById(R.id.dip_status_8_on);       // dipStat[7] : 범인 위치 표시
        dipStatBtnOff[0] = findViewById(R.id.dip_status_1_off);
        dipStatBtnOff[1] = findViewById(R.id.dip_status_2_off);
        dipStatBtnOff[2] = findViewById(R.id.dip_status_3_off);
        dipStatBtnOff[3] = findViewById(R.id.dip_status_4_off);
        dipStatBtnOff[4] = findViewById(R.id.dip_status_5_off);
        dipStatBtnOff[5] = findViewById(R.id.dip_status_6_off);
        dipStatBtnOff[6] = findViewById(R.id.dip_status_7_off);
        dipStatBtnOff[7] = findViewById(R.id.dip_status_8_off);

        dotIsRunning = false;
        buzzerIsRunning = false;
        buzzerStat = BUZZER_OFF;
        stepMotorIsRunning = false;
        dipStat = new boolean[8];

        dipSwitchTimerStart();
    }

    @Override
    protected void onDestroy(){
        dipSwitchTimerEnd();
        super.onDestroy();
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
                if(stat > 0 && !dotIsRunning) {
                    Message msg = touchDelayHandler.obtainMessage();
                    touchDelayHandler.sendMessage(msg);
                    MoveStart(stat);
                }
            }
        };
        pushSwitchTimer.schedule(pushSwitchTimerTask, 0, 100); //Timer 실행
    }
    public void pushSwitchTimerEnd(){
        pushSwitchTimer.cancel();//타이머 종료
    }

    public void dipSwitchTimerStart(){
        dipSwitchTimer = new Timer();
        dipSwitchTimerTask = new TimerTask() {
            @Override
            public void run() {
                dipUpdate();
                Message msg = iconChangeHandler.obtainMessage();
                iconChangeHandler.sendMessage(msg);
            }
        };
        dipSwitchTimer.schedule(dipSwitchTimerTask, 0, 1500); //Timer 실행
    }
    public void dipSwitchTimerEnd(){
        dipSwitchTimer.cancel();//타이머 종료
    }

    public void gameInit(){
        gameClass = new GameBoard();

        int selector = (int) (Math.random() * 6 + 1);
        switch (selector){
            case 1:
                gameBackground.setBackgroundResource(R.drawable.board1);
                break;
            case 2:
                gameBackground.setBackgroundResource(R.drawable.board2);
                break;
            case 3:
                gameBackground.setBackgroundResource(R.drawable.board3);
                break;
            case 4:
                gameBackground.setBackgroundResource(R.drawable.board4);
                break;
            case 5:
                gameBackground.setBackgroundResource(R.drawable.board5);
                break;
            case 6:
                gameBackground.setBackgroundResource(R.drawable.board6);
                break;
        }


        playerX = gameClass.getLocateX(PLAYER);
        playerY = gameClass.getLocateY(PLAYER);

        targetX = gameClass.getLocateX(TARGET);
        targetY = gameClass.getLocateY(TARGET);

        tryCount = 0;
        WriteTextLcd("Board Ready", "Press Button");

        WriteLed(0);
        DrowBoard();
    }

    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start_btn:
                    fndTimerStart();
                    pushSwitchTimerStart();
                    startBtn.setVisibility(View.GONE);
                    giveUpBtn.setVisibility(View.VISIBLE);

                    gameInit();
                    break;

                case R.id.give_up_btn:
                    fndTimerEnd();
                    pushSwitchTimerEnd();
                    gameTarget.setVisibility(View.GONE);
                    gamePlayer.setVisibility(View.GONE);
                    giveUpBtn.setVisibility(View.GONE);
                    startBtn.setVisibility(View.VISIBLE);
                    break;

                case R.id.hint_btn:
                    if (gameClass == null) {
                        WriteTextLcd("Play Game First", "Press Button");
                    } else {
                        WriteLed(CalcLED(gameClass.getDistance()));
                    }
                    break;
                }
            }
        };

        public void DrowBoard(){
            Message msg = drawBoardHandler.obtainMessage();
            drawBoardHandler.sendMessage(msg);
        }

        final Handler drawBoardHandler = new Handler(){
            public void handleMessage(Message msg){
                if(!dipStat[7]){
                    gameTarget.setVisibility(View.VISIBLE);
                    gameTarget.setX(gameTarget.getWidth()*targetX);
                    gameTarget.setY(gameTarget.getHeight()*targetY);
                }else{
                    gameTarget.setVisibility(View.GONE);
                }

                gamePlayer.setVisibility(View.VISIBLE);
                gamePlayer.setX(gamePlayer.getWidth()*playerX);
                gamePlayer.setY(gamePlayer.getHeight()*playerY);
            }
        };

        public void dipUpdate(){
            int innerValue = ReadDipSwitch();
            for(int i = 7; i >= 0; i--){
                int innerPow = (int) Math.pow(2, i);
                if(innerValue >= innerPow){
                    dipStat[i] = true;
                    innerValue -= innerPow;
                }else{
                    dipStat[i] = false;
                }
            }
        }

        final Handler iconChangeHandler = new Handler(){
            public void handleMessage(Message msg){
                for(int i = 0; i < DIP_SWITCH_MAX_BUTTON; i++){
                    if(dipStat[i]) {
                        dipStatBtnOn[i].setVisibility(View.GONE);
                        dipStatBtnOff[i].setVisibility(View.VISIBLE);
                    }
                    else {
                        dipStatBtnOn[i].setVisibility(View.VISIBLE);
                        dipStatBtnOff[i].setVisibility(View.GONE);
                    }
                }
            }
        };

        public int CalcLED(int distance){
            switch(distance){
                case 9:
                case 8:
                    return 255;
                case 7:
                    return 127;
                case 6:
                    return 63;
                case 5:
                    return 31;
                case 4:
                    return 15;
                case 3:
                    return 7;
                case 2:
                    return 3;
                case 1:
                    return 1;
            }
            return 0;
        }

        private void MoveStart(int stat) {
            // PLAYER 이동
            if(gameClass.MovePlayer(stat) > 0){
                tryCount++;
                WriteTextLcd("STEP : " + tryCount, " ");
            }

            // 검거 확인
            if(gameClass.getDistance() == 0){
                Message msg1 = arrestTargetHandler.obtainMessage();
                arrestTargetHandler.sendMessage(msg1);

                Message msg2 = gameEndHandler.obtainMessage();
                gameEndHandler.sendMessage(msg2);
            }
            playerX = gameClass.getLocateX(PLAYER);
            playerY = gameClass.getLocateY(PLAYER);

            // 타이머 실행
            // 카운트 다운 타이머


            // TARGET 이동
            if(dipStat[5]){
                int distance = 1;
                if(!dipStat[0]) distance += 1;
                if(!dipStat[1]) distance += 2;

                if(gameClass.RunAway(distance) > 0){       // 도망갔을 때,
                    Message msg = escapeTargetHandler.obtainMessage();
                    escapeTargetHandler.sendMessage(msg);
                }
                targetX = gameClass.getLocateX(TARGET);
                targetY = gameClass.getLocateY(TARGET);
            }

            // 힌트 업데이트
            if(!dipStat[6]) WriteLed(CalcLED(gameClass.getDistance()));

            // 화면 업데이트
            DrowBoard();
        }

        final Handler touchDelayHandler = new Handler(){
            public void handleMessage(Message msg){
                if(!dotIsRunning && dipStat[4]){
                    stepMotorCountDownTimer = new CountDownTimer(1999, 200) {
                        public void onTick(long millisUntilFinished) {
                            dotIsRunning = true;
                            WriteDot((int) (millisUntilFinished / 200));
                        }

                        public void onFinish() {
                            dotIsRunning = false;
                            WriteDot(0);
                        }
                    }.start();
                }
            }
        };

        final Handler escapeTargetHandler = new Handler(){
            public void handleMessage(Message msg){
                if(!stepMotorIsRunning && !dipStat[3]){
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
        };

        final Handler arrestTargetHandler = new Handler(){
            public void handleMessage(Message msg){
                gameTarget.setVisibility(View.GONE);

                if(!buzzerIsRunning && !dipStat[2]){
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
            }
        };

        final Handler gameEndHandler = new Handler(){
            public void handleMessage(Message msg){
                giveUpBtn.performClick();
            }
        };

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
