//
// Created by Yoo on 2019-12-11.
//
#include <jni.h>
#include <cstring>
#include <pthread.h>
#include <android/log.h>
#include <fcntl.h>
#include <unistd.h>
#include <__locale>
#include "fpga_dot_font.h"

#ifndef TERMPROJECT_JNI_THREAD_STATIC_VALUES_H
#define TERMPROJECT_JNI_THREAD_STATIC_VALUES_H

#endif //TERMPROJECT_JNI_THREAD_STATIC_VALUES_H

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