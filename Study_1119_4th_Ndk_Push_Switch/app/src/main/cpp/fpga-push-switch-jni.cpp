#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>

#define PUSH_SWITCH_DEVICE "dev/fpga_push_switch"
#define MAX_BUTTON 9

int gFd = -1;

int fpga_push_open(void){
    int dev;

    dev = open(PUSH_SWITCH_DEVICE, O_RDWR);
    if(dev<0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    }else{
        gFd = dev;
    }
    return 0;
}

int fpga_push_close(void){
    if(gFd<0){
        __android_log_print(ANDROID_LOG_INFO, "Device not opened!", "Driver = %d", gFd);
        return 0;
    }else{
        close(gFd);
        __android_log_print(ANDROID_LOG_INFO, "Device Close", "Driver = %d", gFd);
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

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_14th_1ndk_1push_1switch_MainActivity_ReceivePushSwitchValue(JNIEnv *env,
                                                                                         jobject instance) {
    int retval;
    retval = fpga_push_switch();
    return retval;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_14th_1ndk_1push_1switch_MainActivity_DeviceOpen(JNIEnv *env,
                                                                             jobject instance){
    int retval;
    retval = fpga_push_open();
    return retval;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_14th_1ndk_1push_1switch_MainActivity_DeviceClose(JNIEnv *env,
                                                                              jobject instance){
    int retval;
    retval = fpga_push_close();
    return retval;
}