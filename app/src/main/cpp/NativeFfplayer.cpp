//
// Created by awing on 19-7-18.
//
#include <jni.h>
#include <android/log.h>
#include "JniRuntime.h"

#define NLOG_TAG "NativeFfplayer"
#include "nlog.h"

extern "C" {
int ffplay_get_duration();
void ffplay_toggle_pause();
void ffplay_stop();
void ffplay_seekto(int msec);
void ffplay_start(char* path);
int ffplay_get_play_position();
int ffplay_get_play_state();
void ffplay_init();
void ffplay_deinit();
};


class FfplayerLisner {
public:
    FfplayerLisner(JNIEnv* env, jobject obj) {
        mFfplayerObject = env->NewGlobalRef(obj);
        mFfplayerClass = env->GetObjectClass(obj);
        if (!mFfplayerClass) {
            NLOGE("fail to get Ffplayer class");
            return;
        }
        mMethodNotifyEventFromNative = env->GetMethodID(mFfplayerClass, "notifyEventFromNative", "(IIILjava/lang/Object;)V");
        if (!mMethodNotifyEventFromNative) {
            NLOGE("fail to found methodid for notifyEventFromNative");
            return;
        }
    }
    ~FfplayerLisner() {
        JNIEnv *env = getJniEnv();
        env->DeleteGlobalRef(mFfplayerObject);
    }
    void notifyEvent(int msg, int ext1, int ext2, void* obj);

private:
    jclass mFfplayerClass;
    jobject mFfplayerObject;
    jmethodID mMethodNotifyEventFromNative;

};

void FfplayerLisner::notifyEvent(int msg, int ext1, int ext2, void *obj) {
    JNIEnv *env = getJniEnv();
    env->CallVoidMethod(mFfplayerObject, mMethodNotifyEventFromNative, msg, ext1, ext2, NULL);
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        env->ExceptionClear();
    }
}

static FfplayerLisner* sFfplayerListenr;

extern "C" void notifyEventListener(int msg, int ext1, int ext2, void* obj) {
    NLOGD("notifyEventListener msg:%d", msg);
    if (sFfplayerListenr) {
        sFfplayerListenr->notifyEvent(msg, ext1, ext2, obj);
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_org_awing_affplay_Ffplayer_nativeInit(JNIEnv *env, jobject instance) {
    if (sFfplayerListenr == NULL) {
        sFfplayerListenr = new FfplayerLisner(env, instance);
    }
    ffplay_init();
}

extern "C"
JNIEXPORT void JNICALL
Java_org_awing_affplay_Ffplayer_nativeDeinit(JNIEnv *env, jobject instance) {
    if (sFfplayerListenr) {
        delete sFfplayerListenr;
        sFfplayerListenr = NULL;
    }
    ffplay_deinit();
}

extern "C"
JNIEXPORT void JNICALL
Java_org_awing_affplay_Ffplayer_seekTo(JNIEnv *env, jobject instance, jint msec) {
    ffplay_seekto(msec);
}
extern "C"
JNIEXPORT void JNICALL
Java_org_awing_affplay_Ffplayer_stop(JNIEnv *env, jobject instance) {
    ffplay_stop();
}
extern "C"
JNIEXPORT void JNICALL
Java_org_awing_affplay_Ffplayer_togglePause(JNIEnv *env, jobject instance) {
    ffplay_toggle_pause();
}
extern "C"
JNIEXPORT jint JNICALL
Java_org_awing_affplay_Ffplayer_getDuration(JNIEnv *env, jobject instance) {
    return ffplay_get_duration();
}
extern "C"
JNIEXPORT void JNICALL
Java_org_awing_affplay_Ffplayer_start(JNIEnv *env, jobject instance, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);

    ffplay_start(const_cast<char*>(path));

    env->ReleaseStringUTFChars(path_, path);
}extern "C"
JNIEXPORT jint JNICALL
Java_org_awing_affplay_Ffplayer_getPlayState(JNIEnv *env, jobject instance) {

    return  ffplay_get_play_state();

}extern "C"
JNIEXPORT jint JNICALL
Java_org_awing_affplay_Ffplayer_getPlayPosition(JNIEnv *env, jobject instance) {

    return ffplay_get_play_position();

}