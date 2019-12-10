#include <jni.h>
#include <cstring>
#include <pthread.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>
#include <__locale>
#include "fpga_dot_font.h"

#define LED_DEVICE "/dev/fpga_led"                      // 260
#define LED_MIN 0
#define LED_MAX 255

#define FND_DEVICE "/dev/fpga_fnd"                      // 261
#define FND_MAX_DIGIT 4

#define DOT_DEVICE "/dev/fpga_dot"                      // 262
#define DOT_MIN 0
#define DOT_MAX 9

#define TEXT_LCD_DEVICE "/dev/fpga_text_lcd"            // 263
#define TEXT_LCD_MAX_BUF 32
#define TEXT_LCD_LINE_BUF 16

#define BUZZER_DEVICE "/dev/fpga_buzzer"                // 264
#define BUZZER_ON 1
#define BUZZER_OFF 0

#define PUSH_SWITCH_DEVICE "/dev/fpga_push_switch"      // 265
#define PUSH_SWITCH_MAX_BUTTON 9

#define DIP_SWITCH_DEVICE "/dev/fpga_dip_switch"        // 266

#define STEP_MOTOR_DEVICE "/dev/fpga_step_motor"        // 267
#define STEP_MOTOR_STATE_VOL 3
#define STEP_MOTOR_ON 0
#define STEP_MOTOR_OFF 1
#define STEP_MOTOR_DIR_LEFT 0
#define STEP_MOTOR_DIR_RIGHT 1
#define STEP_MOTOR_SPDVAL_MIN 0         // min value, fastest
#define STEP_MOTOR_SPDVAL_MAX 255       // max vlaue, slowest


void pushButtonResponse(int pushSwitchStatus);
void *pushSwitchThread(void *data);
int fpga_push_switch(void);

static JNINativeMethod methods[] = {
        {"endThread", "()I", nullptr},
        {"startThread", "()I", nullptr}
};

JavaVM *globalJavaVM = nullptr;
int proceedThread;
jmethodID classFunctionID;
jclass globalReferenceMainActivity;
jclass mainActivityClassId;
int globalPushSwitchIndex = -1;

class LED {
private:
    int led_dev;
    unsigned char led_state;
    ssize_t led_ret;
    unsigned char is_running;
public:
    LED(){
        led_dev = 0;
        led_state = '\0';
        led_ret = 0;
        is_running = 0;
    }
    int ledOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "LED Open Error", "Device is running, Driver = %d", led_dev);
            return -1;
        }

        led_dev = open(LED_DEVICE, O_RDWR);
        if(led_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "LED Open Error", "Failed open, Driver = %d", led_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "LED Open Success", "Successful open, Driver = %d", led_dev);
            is_running = 1;
            return 0;
        }
    }
    int ledClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "LED Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "LED Close Success", "Successful close, Driver = %d", led_dev);
        close(led_dev);
        is_running = 0;
        return 0;
    }
    int ledWrite(int num){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "LED Write Error", "Device is not running");
            return -1;
        }

        if(num > LED_MAX || num < LED_MIN){
            __android_log_print(ANDROID_LOG_INFO, "LED Write Error", "Parameter out of Range, Driver = %d", led_dev);
            return -1;
        }

        led_state = static_cast<unsigned char>(num);
        led_ret = write(led_dev, &led_state, FND_MAX_DIGIT);

        __android_log_print(ANDROID_LOG_INFO, "LED Write Success", "Successful write, Driver = %d", led_dev);
        return 0;
    }
    int ledRead(unsigned char *buf){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "LED Read Error", "Device is not running");
            return -1;
        }

        led_ret = read(led_dev, buf, FND_MAX_DIGIT);
        __android_log_print(ANDROID_LOG_INFO, "LED Read Success", "Successful read, Driver = %d", led_dev);
        return 0;
    }
};

class FND {
private:
    int fnd_dev;
    unsigned char fnd_num[FND_MAX_DIGIT];
    ssize_t fnd_ret;
    unsigned char is_running;
public:
    FND(){
        fnd_dev = 0;
        memset(fnd_num, '\0', sizeof(fnd_num));
        fnd_ret = 0;
        is_running = 0;
    }
    int fndOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "FND Open Error", "Device is running, Driver = %d", fnd_dev);
            return -1;
        }

        fnd_dev = open(FND_DEVICE, O_RDWR);
        if(fnd_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "FND Open Error", "Failed open, Driver = %d", fnd_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "FND Open Success", "Successful open, Driver = %d", fnd_dev);
            is_running = 1;
            return 0;
        }
    }
    int fndClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "FND Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "FND Close Success", "Successful close, Driver = %d", fnd_dev);
        close(fnd_dev);
        is_running = 0;
        return 0;
    }
    int fndWrite(int num){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "FND Write Error", "Device is not running");
            return -1;
        }
        int innerArr[4] = {0, 0, 0, 0};
        int innerNum = num;

        if(innerNum >= 1000){
            innerArr[0] = innerNum/1000;
            innerNum %= 1000;
        }else innerArr[0] = 0;

        if(innerNum >= 100){
            innerArr[1] = innerNum/100;
            innerNum %= 100;
        }else innerArr[1] = 0;

        if(innerNum >= 10){
            innerArr[2] = innerNum/10;
            innerNum %= 10;
        }else innerArr[2] = 0;

        innerArr[3] = innerNum;

        for(int i = 0; i < FND_MAX_DIGIT; i++){
            fnd_num[i] = static_cast<unsigned char>(innerArr[i]);
        }
        fnd_ret = write(fnd_dev, fnd_num, FND_MAX_DIGIT);

        __android_log_print(ANDROID_LOG_INFO, "FND Write Success", "Successful write, Driver = %d", fnd_dev);
        return 0;
    }
    int fndRead(unsigned char *buf){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "FND Read Error", "Device is not running");
            return -1;
        }

        fnd_ret = read(fnd_dev, buf, FND_MAX_DIGIT);
        __android_log_print(ANDROID_LOG_INFO, "FND Read Success", "Successful read, Driver = %d", fnd_dev);
        return 0;
    }
};

class Dot {
private:
    int dot_dev;
    int dot_num;
    ssize_t dot_ret;
    unsigned char is_running;
public:
    Dot(){
        dot_dev = 0;
        dot_num = 0;
        dot_ret = 0;
        is_running = 0;
    }
    int dotOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Open Error", "Device is running, Driver = %d", dot_dev);
            return -1;
        }

        dot_dev = open(DOT_DEVICE, O_WRONLY);
        if(dot_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Open Error", "Failed open, Driver = %d", dot_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Open Success", "Successful open, Driver = %d", dot_dev);
            is_running = 1;
            return 0;
        }
    }
    int dotClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Close Success", "Successful close, Driver = %d", dot_dev);
        close(dot_dev);
        is_running = 0;
        return 0;
    }
    int dotWrite(int num){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Write Error", "Device is not running");
            return -1;
        }

        dot_num = num;
        if(dot_num > DOT_MAX || dot_num < DOT_MIN){
            __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Write Error", "Parameter out of Range, Driver = %d", dot_dev);
            return -1;
        }

        dot_ret = write(dot_dev, fpga_number[num], sizeof(fpga_number[num]));

        __android_log_print(ANDROID_LOG_INFO, "Dot Matrix Write Success", "Successful write, Driver = %d", dot_dev);
        return 0;
    }
};

class TextLCD {
private:
    int text_lcd_dev;
    char text_lcd_data[TEXT_LCD_MAX_BUF];
    ssize_t text_lcd_ret;
    unsigned char is_running;
public:
    TextLCD(){
        text_lcd_dev = 0;
        memset(text_lcd_data, '\0', sizeof(text_lcd_data));
        text_lcd_ret = 0;
        is_running = 0;
    }
    int textLcdOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Open Error", "Device is running, Driver = %d", text_lcd_dev);
            return -1;
        }

        text_lcd_dev = open(TEXT_LCD_DEVICE, O_WRONLY);
        if(text_lcd_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Open Error", "Failed open, Driver = %d", text_lcd_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Open Success", "Successful open, Driver = %d", text_lcd_dev);
            is_running = 1;
            return 0;
        }
    }
    int textLcdClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "Text LCD Close Success", "Successful close, Driver = %d", text_lcd_dev);
        close(text_lcd_dev);
        is_running = 0;
        return 0;
    }
    int textLcdWrite(const char *str1, const char *str2){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Write Error", "Device is not running");
            return -1;
        }

        size_t str_size = strlen(str1);
        if(str_size > TEXT_LCD_LINE_BUF){
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Write Warring", "Parameter out of Range, Line 1 vol = %d", str_size);
        }
        if(str_size > 0){
            strncat(text_lcd_data, str1, str_size);
            memset(text_lcd_data + str_size, ' ', TEXT_LCD_LINE_BUF - str_size);
        }

        str_size = strlen(str2);
        if(str_size > TEXT_LCD_LINE_BUF){
            __android_log_print(ANDROID_LOG_INFO, "Text LCD Write Warring", "Parameter out of Range, Line 2 vol = %d", str_size);
        }
        if(str_size > 0){
            strncat(text_lcd_data, str2, str_size);
            memset(text_lcd_data + TEXT_LCD_LINE_BUF + str_size, ' ', TEXT_LCD_LINE_BUF - str_size);
        }

        text_lcd_ret = write(text_lcd_dev, text_lcd_data, TEXT_LCD_MAX_BUF);
        __android_log_print(ANDROID_LOG_INFO, "Text LCD Write Success", "Successful write, Driver = %d", text_lcd_dev);

        return 0;
    }
};

class Buzzer {
private:
    int buzzer_dev;
    unsigned char buzzer_state;
    ssize_t buzzer_ret;
    unsigned char is_running;
public:
    Buzzer(){
        buzzer_dev = 0;
        buzzer_state = '\0';
        buzzer_ret = 0;
        is_running = 0;
    }
    int buzzerOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Buzzer Open Error", "Device is running, Driver = %d", buzzer_dev);
            return -1;
        }

        buzzer_dev = open(BUZZER_DEVICE, O_RDWR);
        if(buzzer_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "Buzzer Open Error", "Failed open, Driver = %d", buzzer_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "Buzzer Open Success", "Successful open, Driver = %d", buzzer_dev);
            is_running = 1;
            return 0;
        }
    }
    int buzzerClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Buzzer Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "Buzzer Close Success", "Successful close, Driver = %d", buzzer_dev);
        close(buzzer_dev);
        is_running = 0;
        return 0;
    }
    int buzzerWrite(int stat){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Buzzer Write Error", "Device is not running");
            return -1;
        }

        if(stat) buzzer_state = BUZZER_ON;
        else buzzer_state = BUZZER_OFF;

        buzzer_ret = write(buzzer_dev, &buzzer_state, 1);

        __android_log_print(ANDROID_LOG_INFO, "Buzzer Write Success", "Successful write, Driver = %d", buzzer_dev);
        return 0;
    }
    int buzzerRead(unsigned char *buf){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Buzzer Read Error", "Device is not running");
            return -1;
        }

        buzzer_ret = read(buzzer_dev, buf, 1);
        __android_log_print(ANDROID_LOG_INFO, "Buzzer Read Success", "Successful read, Driver = %d", buzzer_dev);
        return 0;
    }
};

class DipSwitch{
private:
    int dip_switch_dev;
    unsigned char dip_switch_state;
    ssize_t dip_switch_ret;
    unsigned char is_running;
public:
    DipSwitch(){
        dip_switch_dev = 0;
        dip_switch_state = '\0';
        dip_switch_ret = 0;
        is_running = 0;
    }
    int dipSwitchOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Dip Switch Open Error", "Device is running, Driver = %d", dip_switch_dev);
            return -1;
        }

        dip_switch_dev = open(DIP_SWITCH_DEVICE, O_RDONLY);
        if(dip_switch_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "Dip Switch Open Error", "Failed open, Driver = %d", dip_switch_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "Dip Switch Open Success", "Successful open, Driver = %d", dip_switch_dev);
            is_running = 1;
            return 0;
        }
    }
    int dipSwitchClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Dip Switch Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "Dip Switch Close Success", "Successful close, Driver = %d", dip_switch_dev);
        close(dip_switch_dev);
        is_running = 0;
        return 0;
    }
    int dipSwitchRead(unsigned char *buf){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Dip Switch Read Error", "Device is not running");
            return -1;
        }

        dip_switch_ret = read(dip_switch_dev, buf, 1);
        __android_log_print(ANDROID_LOG_INFO, "Dip Switch Read Success", "Successful read, Driver = %d", dip_switch_dev);
        return 0;
    }
};

class StepMotor{
private:
    int step_motor_dev;
    unsigned char step_motor_state[STEP_MOTOR_STATE_VOL];
    ssize_t step_motor_ret;
    unsigned char is_running;
public:
    StepMotor(){
        step_motor_dev = 0;
        memset(step_motor_state, '\0', sizeof(step_motor_state));
        step_motor_ret = 0;
        is_running = 0;
    }
    int stepMotorOpen(){
        if(is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Step Motor Open Error", "Device is running, Driver = %d", step_motor_dev);
            return -1;
        }

        step_motor_dev = open(STEP_MOTOR_DEVICE, O_WRONLY);
        if(step_motor_dev < 0){
            __android_log_print(ANDROID_LOG_INFO, "Step Motor Open Error", "Failed open, Driver = %d", step_motor_dev);
            return -1;
        }else {
            __android_log_print(ANDROID_LOG_INFO, "Step Motor Open Success", "Successful open, Driver = %d", step_motor_dev);
            is_running = 1;
            return 0;
        }
    }
    int stepMotorClose(){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Step Motor Close Error", "Device is not running");
            return -1;
        }

        __android_log_print(ANDROID_LOG_INFO, "Step Motor Close Success", "Successful close, Driver = %d", step_motor_dev);
        close(step_motor_dev);
        is_running = 0;
        return 0;
    }
    int stepMotorWrite(int action, int direction, int speed){
        if(!is_running) {
            __android_log_print(ANDROID_LOG_INFO, "Step Motor Write Error", "Device is not running");
            return -1;
        }

//        if(action != STEP_MOTOR_OFF || action != STEP_MOTOR_ON){
//            __android_log_print(ANDROID_LOG_INFO, "Step Motor Write Error", "Parameter out of Range, action = %d", action);
//            return -1;
//        }
//        if(direction != STEP_MOTOR_DIR_LEFT || direction != STEP_MOTOR_DIR_RIGHT){
//            __android_log_print(ANDROID_LOG_INFO, "Step Motor Write Error", "Parameter out of Range, direction = %d", direction);
//            return -1;
//        }
//        if(speed > STEP_MOTOR_SPDVAL_MAX|| speed < STEP_MOTOR_SPDVAL_MIN){
//            __android_log_print(ANDROID_LOG_INFO, "Step Motor Write Error", "Parameter out of Range, speed = %d", speed);
//            return -1;
//        }

        step_motor_state[0] = (unsigned char)action;
        step_motor_state[1] = (unsigned char)direction;
        step_motor_state[2] = (unsigned char)speed;

        step_motor_ret = write(step_motor_dev, step_motor_state, STEP_MOTOR_STATE_VOL);

        __android_log_print(ANDROID_LOG_INFO, "Step Motor Write Success", "Successful write, Driver = %d", step_motor_dev);
        return 0;
    }
};

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_ReadLed(JNIEnv *env,
                                                                   jobject instance){
    jint ret = 0;
    unsigned char stat;
    LED led;

    ret = led.ledOpen();
    if(ret < 0) return ret;

    ret = led.ledRead(&stat);
    if(ret < 0) return ret;

    ret = led.ledClose();
    if(ret < 0) return ret;

    return stat;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_WriteLed(JNIEnv *env,
                                                                    jobject instance,
                                                                    jint num){
    jint ret = 0;
    LED led;

    ret = led.ledOpen();
    if(ret < 0) return ret;

    ret = led.ledWrite(num);
    if(ret < 0) return ret;

    ret = led.ledClose();
    return ret;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_ReadFnd(JNIEnv *env,
                                                                   jobject instance){
    jint ret = 0;
    unsigned char stat[FND_MAX_DIGIT];
    FND fnd;

    ret = fnd.fndOpen();
    if(ret < 0) return nullptr;

    ret = fnd.fndRead(stat);
    if(ret < 0) return nullptr;

    ret = fnd.fndClose();
    if(ret < 0) return nullptr;

    return env->NewStringUTF(reinterpret_cast<const char *>(stat));;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_WriteFnd(JNIEnv *env,
                                                                    jobject instance,
                                                                    jint num){
    jint ret = 0;
    FND fnd;

    ret = fnd.fndOpen();
    if(ret < 0) return ret;

    ret = fnd.fndWrite(num);
    if(ret < 0) return ret;

    ret = fnd.fndClose();
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_WriteDot(JNIEnv *env,
                                                                    jobject instance,
                                                                    jint num){
    jint ret = 0;
    Dot dot;

    ret = dot.dotOpen();
    if(ret < 0) return ret;

    ret = dot.dotWrite(num);
    if(ret < 0) return ret;

    ret = dot.dotClose();
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_WriteTextLcd(JNIEnv *env,
                                                                        jobject instance,
                                                                        jstring str1,
                                                                        jstring str2){
    jint ret = 0;
    TextLCD textLCD;

    ret = textLCD.textLcdOpen();
    if(ret < 0) return ret;

    const char *pstr1 = env->GetStringUTFChars(str1, 0);
    const char *pstr2 = env->GetStringUTFChars(str2, 0);

    ret = textLCD.textLcdWrite(pstr1, pstr2);
    if(ret < 0) return ret;

    env->ReleaseStringUTFChars(str1, pstr1);
    env->ReleaseStringUTFChars(str2, pstr2);

    ret = textLCD.textLcdClose();
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_ReadBuzzer(JNIEnv *env,
                                                                      jobject instance){
    jint ret = 0;
    unsigned char result;
    Buzzer buzzer;

    ret = buzzer.buzzerOpen();
    if(ret < 0) return ret;

    ret = buzzer.buzzerRead(&result);
    if(ret < 0) return ret;

    ret = buzzer.buzzerClose();
    if(ret < 0) return ret;

    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_WriteBuzzer(JNIEnv *env,
                                                                       jobject instance,
                                                                       jint stat){
    jint ret = 0;
    Buzzer buzzer;

    ret = buzzer.buzzerOpen();
    if(ret < 0) return ret;

    ret = buzzer.buzzerWrite(stat);
    if(ret < 0) return ret;

    ret = buzzer.buzzerClose();
    return ret;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_startPushSwitchThread(JNIEnv *env,
                                                                                 jobject instance){
    int threadId;
    pthread_t p_thread;

    proceedThread = 1;

    __android_log_print(ANDROID_LOG_INFO, "startPushSwitchThread", "Call start thread" ) ;

    mainActivityClassId = env->FindClass("com/example/termproject_2011144024_uds/MainActivity") ;
    if (mainActivityClassId == nullptr) {
        __android_log_print(ANDROID_LOG_INFO, "startPushSwitchThread", "Can't find MainActivity") ;
    }

    globalReferenceMainActivity = (jclass)env->NewGlobalRef(mainActivityClassId) ;
    classFunctionID = env->GetStaticMethodID(mainActivityClassId, "ReadPushSwitch", "(I)V") ;
    if (classFunctionID == nullptr) {
        __android_log_print(ANDROID_LOG_INFO, "ReadPushSwitch", "Can't find ReadPushSwitch()" ) ;
        env->DeleteGlobalRef(globalReferenceMainActivity) ;
    }
    else {
        __android_log_print(ANDROID_LOG_INFO, "ReadPushSwitch", "ReadPushSwitch connect success") ;
        env->CallStaticIntMethod(mainActivityClassId, classFunctionID, 0);
    }

    threadId = pthread_create(&p_thread, nullptr, pushSwitchThread, nullptr);
    if (threadId < 0) {
        __android_log_print(ANDROID_LOG_INFO, "ReadPushSwitch", "Create thread fail") ;
        return -1 ;
    }
    return threadId;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_endPushSwitchThread(JNIEnv *env,
                                                                               jobject instance){
    __android_log_print(ANDROID_LOG_INFO, "endPushSwitchThread", "Call end thread");
    proceedThread = 0;
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_ReadDipSwitch(JNIEnv *env,
                                                                         jobject instance){
    jint ret = 0;
    unsigned char stat;
    jint result;
    DipSwitch dipSwitch;

    ret = dipSwitch.dipSwitchOpen();
    if(ret < 0) return -1;

    ret = dipSwitch.dipSwitchRead(&stat);
    if(ret < 0) return -1;

    ret = dipSwitch.dipSwitchClose();
    if(ret < 0) return -1;

    result = stat;
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_WriteStepMotor(JNIEnv *env,
                                                                          jobject instance,
                                                                          jint action,
                                                                          jint direction,
                                                                          jint speed){
    jint ret = 0;
    StepMotor stepMotor;

    ret = stepMotor.stepMotorOpen();
    if(ret < 0) return ret;

    ret = stepMotor.stepMotorWrite(action, direction, speed);
    if(ret < 0) return ret;

    ret = stepMotor.stepMotorClose();
    return ret;
}

jint JNI_OnLoad(JavaVM* parameterVM, void* reserved) {
    JNIEnv* jniInterfacePointer = nullptr;
    jclass classId;

    if (parameterVM->GetEnv((void**)&jniInterfacePointer, JNI_VERSION_1_4) != JNI_OK) {
        __android_log_print(ANDROID_LOG_INFO, "Function JNI_OnLoad Error", "GetEnv failed") ;
        return -1;
    }

    classId = jniInterfacePointer->FindClass("com/example/termproject_2011144024_uds/MainActivity") ;
    if (classId == nullptr) {
        __android_log_print(ANDROID_LOG_INFO, "Function JNI_OnLoad Error", "Native registration unable to find class(AVMJni)");
        return -1;
    }

    methods[0].fnPtr = (void*)Java_com_example_termproject_12011144024_1uds_MainActivity_startPushSwitchThread;
    methods[1].fnPtr = (void*)Java_com_example_termproject_12011144024_1uds_MainActivity_endPushSwitchThread;

    if (jniInterfacePointer->RegisterNatives(classId, methods, sizeof (methods) / sizeof (methods[0]) ) < 0) {
        __android_log_print(ANDROID_LOG_INFO, "Function JNI_OnLoad Error", "RegisterNatives failed" ) ;
        return -1;
    }
    globalJavaVM = parameterVM;
    return JNI_VERSION_1_4;
}

void pushButtonResponse(int pushSwitchStatus) {
    if (!globalJavaVM) {
        __android_log_print(ANDROID_LOG_INFO, "Function Error pushButtonResponse", "glpVM = 0");
        return;
    }
    if (!classFunctionID) {
        __android_log_print(ANDROID_LOG_INFO, "Function Error pushButtonResponse", "classFunctionID = 0");
        return ;
    }

    JNIEnv* jniInterfacePointer = nullptr;
    globalJavaVM->AttachCurrentThread(&jniInterfacePointer, nullptr);
    if (jniInterfacePointer == nullptr || globalReferenceMainActivity == nullptr) {
        globalJavaVM->DetachCurrentThread();
        __android_log_print(ANDROID_LOG_INFO, "Function Error pushButtonResponse", "env or jObject is null");
        return ;
    }

    jniInterfacePointer->CallStaticVoidMethod(globalReferenceMainActivity, classFunctionID, pushSwitchStatus);
    globalJavaVM->DetachCurrentThread();
}


void *pushSwitchThread(void *data){
    int dev;
    int status;

    dev = open(PUSH_SWITCH_DEVICE, O_RDWR);
    if(dev<0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return nullptr;
    }else{
        globalPushSwitchIndex = dev;
    }

    while(proceedThread){
        status = fpga_push_switch();
        if(status) pushButtonResponse(status);
        usleep(100000);     // 100ms
    }

    if(globalPushSwitchIndex<0){
        __android_log_print(ANDROID_LOG_INFO, "Device not opened!", "Driver = %d", globalPushSwitchIndex);
    }else{
        close(globalPushSwitchIndex);
        __android_log_print(ANDROID_LOG_INFO, "Device Close", "Driver = %d", globalPushSwitchIndex);
        return nullptr;
    }
}

int fpga_push_switch(void){
    int i;
    size_t buff_size;
    int retval;

    unsigned char push_sw_buff[PUSH_SWITCH_MAX_BUTTON];

    if(globalPushSwitchIndex<0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", globalPushSwitchIndex);
        return -1;
    }else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", globalPushSwitchIndex);
        buff_size = sizeof(push_sw_buff);

        __android_log_print(ANDROID_LOG_INFO, "Device Check 1", "Driver = %d", globalPushSwitchIndex);

        read(globalPushSwitchIndex, &push_sw_buff, buff_size);

        __android_log_print(ANDROID_LOG_INFO, "Device Check 2", "Driver = %d", globalPushSwitchIndex);

        retval = 0;

        for(i = 0; i < PUSH_SWITCH_MAX_BUTTON; i++){
            if(push_sw_buff[i] != 0){
                retval |= 0x1 << i;
            }
        }
    }
    return retval;
}