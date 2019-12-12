//
// Created by Yoo on 2019-12-11.
//

#include "static-values.h"

using namespace std;

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
Java_com_example_termproject_12011144024_1uds_MainActivity_ReadPushSwitch(JNIEnv *env,
                                                                          jobject instance){
    jint ret = 0;
    unsigned char stat[PUSH_SWITCH_MAX_BUTTON];
    jint result;
    PushSwitch pushSwitch;

    ret = pushSwitch.pushSwitchOpen();
    if(ret < 0) return -1;

    ret = pushSwitch.pushSwitchRead();
    if(ret < 0) return -1;

    ret = pushSwitch.pushSwitchClose();
    if(ret < 0) return -1;

    return result;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_12011144024_1uds_MainActivity_ReadDipSwitch(JNIEnv *env,
                                                                         jobject instance){
    jint ret = 0;
    jint result;
    DipSwitch dipSwitch;

    ret = dipSwitch.dipSwitchOpen();
    if(ret < 0) return -1;

    result = dipSwitch.dipSwitchRead();
    if(ret < 0) return -1;

    ret = dipSwitch.dipSwitchClose();
    if(ret < 0) return -1;

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