//
// Created by awing on 19-7-19.
//

#ifndef AFFPLAY_NLOG_H
#define AFFPLAY_NLOG_H

#include <android/log.h>

#define NLOGV(FORMAT,...) __android_log_print(ANDROID_LOG_VERBOSE, NLOG_TAG, FORMAT, ##__VA_ARGS__)
#define NLOGW(FORMAT,...) __android_log_print(ANDROID_LOG_WARN, NLOG_TAG, FORMAT, ##__VA_ARGS__)
#define NLOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO, NLOG_TAG, FORMAT, ##__VA_ARGS__)
#define NLOGD(FORMAT,...) __android_log_print(ANDROID_LOG_DEBUG, NLOG_TAG, FORMAT, ##__VA_ARGS__)
#define NLOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR, NLOG_TAG, FORMAT, ##__VA_ARGS__)

#endif //AFFPLAY_NLOG_H
