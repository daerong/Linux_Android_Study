#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>

#define LINE_BUFF 16
#define MAX_BUFF 32

#define TEXT_LCD_DEVICE "/dev/fpga_text_lcd"

int fpga_text_lcd(const char *str1, const char *str2){
    int i;
    int dev;
    size_t str_size;
    int chk_size;

    char string[32];
    memset(string, 0, sizeof(string));

    dev = open(TEXT_LCD_DEVICE, O_RDWR);
    if(dev < 0){
        __android_log_print(ANDROID_LOG_INFO, "Device Open Error", "Driver = %d", dev);
        return -1;
    }else{
        str_size = strlen(str1);
        __android_log_print(ANDROID_LOG_INFO, "LogGo", "Driver = %d", str_size);
        if(str_size > 0){
            strncat(string, str1, str_size);
            memset(string + str_size, ' ', LINE_BUFF - str_size);
        }
        str_size = strlen(str2);
        __android_log_print(ANDROID_LOG_INFO, "LogGo", "Driver = %d", str_size);
        if(str_size > 0){
            strncat(string, str2, str_size);
            memset(string + LINE_BUFF + str_size, ' ', LINE_BUFF - str_size);
            __android_log_print(ANDROID_LOG_INFO, "LogGo", "String = %s", string);
        }
        __android_log_print(ANDROID_LOG_INFO, "LogGo", "String = %s", string);

        write(dev, string, MAX_BUFF - 1);
        close(dev);
    }

    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_study_11119_11st_1ndk_1textlcd_MainActivity_ReceiveTextLcdValue(JNIEnv *env,
                                                                         jobject instance,
                                                                         jstring val1,
                                                                         jstring val2) {
    // TODO
    jint result = 0;
    const char *pstr1 = env->GetStringUTFChars(val1, 0);
    __android_log_print(ANDROID_LOG_INFO, "FpgaTextLcdJniExample", "value = %d", pstr1);
    const char *pstr2 = env->GetStringUTFChars(val2, 0);
    __android_log_print(ANDROID_LOG_INFO, "FpgaTextLcdJniExample", "value = %d", pstr2);

    fpga_text_lcd(pstr1, pstr2);

    __android_log_print(ANDROID_LOG_INFO, "Debug 1", "Deiver = %d", result);

    env->ReleaseStringUTFChars(val1, pstr1);
    env->ReleaseStringUTFChars(val2, pstr2);

    return result;
}
