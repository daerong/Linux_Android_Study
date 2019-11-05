#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>

#define LED_DEVICE "/dev/fpga_led"

int fpga_led(int x){
    int dev;
    unsigned char data;
    unsigned char retval;

    unsigned char val[] = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x00, 0xff};

    dev = open(LED_DEVICE, O_RDWR);
    if(dev<0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", x);
    }
    else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %d", x);
        write(dev, &val[x], sizeof(unsigned char));
        close(dev);
    }

    return 0;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_study_11105_11st_1ndk_1led_MainActivity_ReceiveLedValue(JNIEnv *env,
                                                                         jobject instance, jint val) {

    // TODO

    __android_log_print(ANDROID_LOG_INFO, "FpgaLedJniExample", "led value = %d", val);
    fpga_led(val);

    return NULL;
}