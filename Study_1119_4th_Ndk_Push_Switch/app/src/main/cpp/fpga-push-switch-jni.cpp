#include <jni.h>
#include <cstring>
#include <pthread.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>
#include <__locale>

#define PUSH_SWITCH_DEVICE "dev/fpga_push_switch"
#define MAX_BUTTON 9

int gFd = -1;
int pushSwitch_thread_msg = 2;
pthread_t pushSwitch_ev_thread;
int pushSwitch_thread_id;




JavaVM *globalJavaVM = nullptr;
jclass globalReferenceMainActivity;
jclass mainActivityClassId;
jmethodID classFunctionID;


int pushSwitch_isAlive = 0;

int fpga_push_open(void){
    int dev;

    dev = open(PUSH_SWITCH_DEVICE, O_RDWR);
    if(dev<0){
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "Device Open Error = %d", dev);
        return -1;
    }else{
        gFd = dev;
    }
    return 0;
}

int fpga_push_close(void){
    if(gFd<0){
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "Device not opened! = %d", gFd);
        return 0;
    }else{
        close(gFd);
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "Device Close = %d", gFd);
        return -1;
    }
}

int fpga_push_switch(void){
    int i;
    int dev;
    size_t buff_size;
    int retval;

    unsigned char push_sw_buff[MAX_BUTTON];

    if(gFd<0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", gFd);
        return -1;
    }else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", gFd);
        buff_size = sizeof(push_sw_buff);

        __android_log_print(ANDROID_LOG_INFO, "Device Check 1", "Driver = %d", gFd);

        read(gFd, &push_sw_buff, buff_size);

        __android_log_print(ANDROID_LOG_INFO, "Device Check 2", "Driver = %d", gFd);

        retval = 0;

        for(i = 0; i < MAX_BUTTON; i++){
            if(push_sw_buff[i] != 0){
                retval |= 0x1 << i;
            }
        }
    }
    return retval;
}

void responseFunction(int stat){
    if (!globalJavaVM) {
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "glpVM = 0");
        return;
    }
    if (!classFunctionID) {
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "classFunctionID = 0");
        return;
    }

    JNIEnv* jniInterfacePointer = nullptr;
    globalJavaVM->AttachCurrentThread(&jniInterfacePointer, nullptr);
    if (jniInterfacePointer == nullptr || globalReferenceMainActivity == nullptr) {
        globalJavaVM->DetachCurrentThread();
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "env or jObject is null");
        return;
    }

    jniInterfacePointer->CallStaticVoidMethod(globalReferenceMainActivity, classFunctionID, stat);
    globalJavaVM->DetachCurrentThread();
}

void* pushSwitch_ev_func(void *data){
    int stat;

    while(pushSwitch_isAlive){
        stat = fpga_push_switch();
        __android_log_print(ANDROID_LOG_INFO, "NATIVE", "data = %d", stat);
        responseFunction(stat);

        usleep(1000000);
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_14th_1ndk_1push_1switch_MainActivity_startPushSwitchThread(JNIEnv *env,
                                                                                        jobject instance){
    int retval = fpga_push_open();
    if(retval == -1) return -1;

    pushSwitch_isAlive = 1 ;

    __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Call start thread" ) ;

    mainActivityClassId = env->FindClass("com.example.study_1119_4th_ndk_push_switch/MainActivity");
    if ( mainActivityClassId == nullptr) {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Can't find the class.") ;
    } else {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Successful find the class.") ;
    }

    globalReferenceMainActivity = (jclass)env->NewGlobalRef(mainActivityClassId) ;
    classFunctionID = env->GetStaticMethodID(mainActivityClassId, "ReadPushSwitch", "(I)V") ;

    if ( classFunctionID == nullptr) {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Can't find the function." ) ;
        env->DeleteGlobalRef( globalReferenceMainActivity ) ;
    } else {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Successful find the function." ) ;
    }

    pushSwitch_thread_id = pthread_create(&pushSwitch_ev_thread, nullptr, pushSwitch_ev_func, (void *) &pushSwitch_thread_msg);

    if ( pushSwitch_thread_id < 0 ) {
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Create thread fail." ) ;
        return -1 ;
    }else{
        __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Create thread success." ) ;
    }
    return 0 ;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_14th_1ndk_1push_1switch_MainActivity_endPushSwitchThread(JNIEnv *env,
                                                                                        jobject instance){
    __android_log_print( ANDROID_LOG_INFO, "NATIVE", "Call end thread" ) ;
    pushSwitch_isAlive = 0 ;

    fpga_push_close();
    return 0 ;
}

jint JNI_OnLoad(JavaVM* parameterVM, void* reserved) {

    JNIEnv *env;
    globalJavaVM = parameterVM;

    if(globalJavaVM->GetEnv((void**) &env, JNI_VERSION_1_4 ) != JNI_OK){
        return -1;
    }

    return JNI_VERSION_1_4 ;
}