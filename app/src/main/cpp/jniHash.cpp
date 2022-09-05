#include <jni.h>
#include <android/log.h>
#include "../jniLibs/tkey.h"
#include <string>
#include <cmath>
#include <android/log.h>
#include "jniCommon.cpp"
#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_FFIHash_jniHash(JNIEnv *jEnv, jclass clazz, jstring jInput) {
    const char *pStr = jEnv->GetStringUTFChars(jInput, JNI_FALSE);
    const char *pHash = sha256_hash(pStr);
    jstring result = jEnv->NewStringUTF(pHash);
    jEnv->ReleaseStringUTFChars(jInput, pStr);
    string_destroy(const_cast<char *>(pHash));
    return result;
}