#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>


#define FND_DEVICE "/dev/fpga_fnd"
#define MAX_DIGIT 4

int fpga_fnd(const char* str){
    int dev;
    unsigned char data[4];
    unsigned char retval;
    int i;
    int str_size;

    memset(data, 0, sizeof(data));

    str_size = strlen(str);
    if(str_size > MAX_DIGIT){
        str_size = MAX_DIGIT;
    }

    for(i = 0; i < str_size; i++){
        if((str[i]<0x30)||(str[i]>0x39)){
            return 1;
        }

        data[i] = str[i] - 0x30;
    }

    dev = open(FND_DEVICE, O_RDWR);
    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %s", str);
        return -1;
    }else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %s", str);
        write(dev, &data, 4);
        close(dev);
    }


    return 0;
}

int fpga_cycle(){
    int dev;
    unsigned char data[4];

    dev = open(FND_DEVICE, O_RDWR);
    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "fpga_cycle");
        return -1;
    }else{
        __android_log_print(ANDROID_LOG_INFO, "Device Open Success", "Driver = %s");
        for(int i = 0; i < 40; i++){
            for(int j = 0; j < 4; j++){
                data[j] = i;
            }
            write(dev, &data, 4);
            sleep(1);
        }

        close(dev);
    }
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11112_11st_1ndk_1fnd_MainActivity_ReceiveFndValue(JNIEnv *env,
                                                                         jobject instance,
                                                                         jstring val) {
    jint result;
    const char *str = env->GetStringUTFChars(val, 0);

    // TODO


    __android_log_print(ANDROID_LOG_INFO, "FpgaLedJniExample", "led value = %d", val);
    result = fpga_fnd(str);

    env->ReleaseStringUTFChars(val, str);

    return result;
}