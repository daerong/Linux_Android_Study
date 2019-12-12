//
// Created by Yoo on 2019-12-11.
//

#include "static-values.h"

using namespace std;

JavaVM *globalJavaVM;
jclass globalReferenceMainActivity;
jmethodID classFunctionID;

int fnd_msg = 1;
int pushSwitch_msg = 2;
int dot_msg = 3;

int fnd_event_stat = 0;
int pushSwitch_event_stat = 0;
int dot_event_stat = 0;

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *parameterVM, void *reserved){
    JNIEnv *env;
    jclass cls;
    globalJavaVM = parameterVM;  /* cache the JavaVM pointer */

    if(parameterVM->GetEnv((void**) &env, JNI_VERSION_1_4 ) != JNI_OK){
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Can't get a JNIEnv.");
        return JNI_ERR; /* JNI version not supported */
    } else {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Successful get a JNIEnv.");
    }


    cls = env->FindClass("com.example.termproject_jni_thread/MainActivity");
    if ( cls == nullptr) {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Can't find a jclass.");
        return JNI_ERR;
    } else {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Successful find a jclass.");
    }

    /* Use weak global ref to allow C class to be unloaded */
    globalReferenceMainActivity = (jclass)env->NewGlobalRef(cls);
    if ( globalReferenceMainActivity == nullptr) {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Can't make a global jclass.");
        return JNI_ERR;
    } else {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Successful make a global jclass.");
    }

    /* Compute and cache the method ID */
    classFunctionID = env->GetMethodID(cls, "ReadPushSwitch", "(I)V") ;
    if ( classFunctionID == nullptr) {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Can't get a static method");
        return JNI_ERR;
    } else {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Successful get a static method");
    }
    return JNI_VERSION_1_2;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM *parameterVM, void *reserved){
    JNIEnv *env;

    if (parameterVM->GetEnv((void**) &env, JNI_VERSION_1_4)) {
        return;
    }

    env->DeleteWeakGlobalRef(globalReferenceMainActivity);
}

void* pushSwitch_ev_func(void *data){
    int stat = 0;
    unsigned char push_sw_buff[PUSH_SWITCH_MAX_BUTTON];
    size_t buff_size = sizeof(push_sw_buff);

    int pushSwitch_dev = open(PUSH_SWITCH_DEVICE, O_RDWR);
    if(pushSwitch_dev<0){
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "pushSwitch Open Error");
        return nullptr;
    }

    JNIEnv* jniInterfacePointer = nullptr;
    globalJavaVM->AttachCurrentThread(&jniInterfacePointer, nullptr);

    while(pushSwitch_event_stat){
        stat = read(pushSwitch_dev, &push_sw_buff, buff_size);
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "data = %d", stat);

        for(int i = 0; i < PUSH_SWITCH_MAX_BUTTON; i++){
            if(push_sw_buff[i] != 0){
                stat |= 0x1 << i;
            }
        }

        if(!dot_event_stat && stat) jniInterfacePointer->CallStaticVoidMethod(globalReferenceMainActivity, classFunctionID, stat);

        usleep(1000000);
    }


    globalJavaVM->DetachCurrentThread();
    __android_log_print(ANDROID_LOG_INFO, "NATIVE", "env or jObject is null");

    close(pushSwitch_dev);
    __android_log_print(ANDROID_LOG_INFO, "NATIVE", "pushSwitch Close");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_pushSwitchThreadStart(JNIEnv *env, jobject obj){
    pthread_t pushSwitch_ev_thread;
    pthread_attr_t pushSwitch_attr;
    void *pushSwitch_status;
    int pushSwitch_thread_id;

    pthread_attr_init(&pushSwitch_attr);
    pthread_attr_setdetachstate(&pushSwitch_attr, PTHREAD_CREATE_JOINABLE);

    pushSwitch_event_stat = 1;

    pushSwitch_thread_id = pthread_create(&pushSwitch_ev_thread, &pushSwitch_attr, pushSwitch_ev_func, (void *) &pushSwitch_msg);
    pthread_attr_destroy(&pushSwitch_attr);
    if(pushSwitch_thread_id){
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Error creating thread: " + pushSwitch_thread_id);
    }else{
        pushSwitch_thread_id = pthread_join(pushSwitch_ev_thread, &pushSwitch_status);
        if(pushSwitch_thread_id){
            __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Error returning from join");
        }
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_pushSwitchThreadEnd(JNIEnv *env, jobject obj){
    pushSwitch_event_stat = 0;
}

void* fnd_ev_func(void *data){
    int ret = 0;
    int time = 0;
    FND fnd;

    ret = fnd.fndOpen();
    if(ret < 0) return nullptr;

    while(fnd_event_stat){
        fnd.fndWrite(time);
        sleep(1);
        time++;
    }

    fnd.fndClose();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_fndThreadStart(JNIEnv *env, jobject obj){
    pthread_t fnd_ev_thread;
    pthread_attr_t fnd_attr;
    void *fnd_status;
    int fnd_thread_id;

    pthread_attr_init(&fnd_attr);
    pthread_attr_setdetachstate(&fnd_attr, PTHREAD_CREATE_JOINABLE);

    fnd_event_stat = 1;

    fnd_thread_id = pthread_create(&fnd_ev_thread, &fnd_attr, fnd_ev_func, (void *) &fnd_msg);
    pthread_attr_destroy(&fnd_attr);
    if(fnd_thread_id){
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Error creating thread: " + fnd_thread_id);
    }else{
        fnd_thread_id = pthread_join(fnd_ev_thread, &fnd_status);
        if(fnd_thread_id){
            __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Error returning from join");
        }
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_fndThreadEnd(JNIEnv *env, jobject obj){
    fnd_event_stat = 0;
}

void* dot_ev_func(void *data){
    int ret = 0;
    int time = 3;
    Dot dot;

    ret = dot.dotOpen();
    if(ret < 0) return nullptr;

    while(time < 0){
        dot.dotWrite(time);
        sleep(1);
        time--;
    }

    dot.dotClose();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_dotThreadStart(JNIEnv *env, jobject obj){
    pthread_t dot_ev_thread;
    pthread_attr_t dot_attr;
    void *dot_status;
    int dot_thread_id;

    pthread_attr_init(&dot_attr);
    pthread_attr_setdetachstate(&dot_attr, PTHREAD_CREATE_JOINABLE);

    dot_event_stat = 1;

    dot_thread_id = pthread_create(&dot_ev_thread, &dot_attr, dot_ev_func, (void *) &dot_msg);
    pthread_attr_destroy(&dot_attr);
    if(dot_thread_id){
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Error creating thread: " + dot_thread_id);
    }else{
        dot_thread_id = pthread_join(dot_ev_thread, &dot_status);
        if(dot_thread_id){
            __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Error returning from join");
        }
    }

    dot_event_stat = 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_dotThreadCheck(JNIEnv *env, jobject obj){
    if(dot_event_stat) return -1;
    else return 1;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_ReadLed(JNIEnv *env,
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
Java_com_example_termproject_1jni_1thread_MainActivity_WriteLed(JNIEnv *env,
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
JNIEXPORT jint JNICALL
Java_com_example_termproject_1jni_1thread_MainActivity_WriteTextLcd(JNIEnv *env,
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
Java_com_example_termproject_1jni_1thread_MainActivity_WriteBuzzer(JNIEnv *env,
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
Java_com_example_termproject_1jni_1thread_MainActivity_ReadDipSwitch(JNIEnv *env, jobject instance){
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
Java_com_example_termproject_1jni_1thread_MainActivity_WriteStepMotor(JNIEnv *env,
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


