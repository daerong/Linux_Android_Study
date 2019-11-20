#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>
#include <__locale>

#define BUZZER_DEVICE "/dev/fpga_buzzer"

int fpga_buzzer(int x){
    int dev;
    unsigned char data;
    unsigned char retval;

    data = static_cast<unsigned char>(x);

    dev = open(BUZZER_DEVICE, O_RDWR);
    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    }else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", dev);
        write(dev, &data, 1);
        close(dev);
        return 0;
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_12nd_1ndk_1buzzer_MainActivity_ReceiveBuzzerValue(JNIEnv *env,
                                                                                 jobject instance,
                                                                                 jint val) {
    // TODO
    jint result;
    __android_log_print(ANDROID_LOG_INFO, "FpgaBUzzerExample", "value = %d", val);
    result = fpga_buzzer(val);
    return result;
}
