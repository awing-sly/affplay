//
// Created by awing on 19-7-19.
//

#include <assert.h>
#include "JniRuntime.h"

static JavaVM* sJVM;

JavaVM* getJVM() {
    return sJVM;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm,void* reserved){
    JNIEnv *env;
    sJVM = vm;
    if(vm->GetEnv((void**)&env,JNI_VERSION_1_6)!=JNI_OK){
        return -1;
    }
    return JNI_VERSION_1_6;
}

/*
 * Get the JNIEnv pointer for this thread.
 *
 * Returns NULL if the slot wasn't allocated or populated.
 */
JNIEnv* getJniEnv() {
    JNIEnv *env;
    JavaVM* jvm = getJVM();
    assert(jvm);
    if (jvm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK)
        return NULL;
    return env;
}

