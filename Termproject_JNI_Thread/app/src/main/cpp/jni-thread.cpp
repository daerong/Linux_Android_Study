//
// Created by Yoo on 2019-12-11.
//

#include "static-values.h"

using namespace std;

JavaVM *globalJavaVM;
jclass globalReferenceMainActivity;
jmethodID classFunctionID;

int thread_msg = 2;
int pushSwitch_event_stat = 0;

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
    classFunctionID = env->GetStaticMethodID(cls, "ReadPushSwitch", "(I)V") ;
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
        globalJavaVM->AttachCurrentThread(&jniInterfacePointer, nullptr);

        usleep(1000000);
    }

    jniInterfacePointer->CallStaticVoidMethod(globalReferenceMainActivity, classFunctionID, stat);
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

    pushSwitch_thread_id = pthread_create(&pushSwitch_ev_thread, &pushSwitch_attr, pushSwitch_ev_func, (void *) &thread_msg);
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

