#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareMap_jniShareMapFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pMap = reinterpret_cast<ShareMap *>(pObject);
    share_map_free(pMap);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareMap_jniShareMapGetKeys(JNIEnv *env, jobject jthis,
                                                                jlong ptr,
                                                                jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pMap = reinterpret_cast<ShareMap *>(ptr);
    char *pResult = share_map_get_share_keys(pMap, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareMap_jniShareMapGetShareByKey(JNIEnv *env, jobject jthis,
                                                                      jlong ptr, jstring key,
                                                                      jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pMap = reinterpret_cast<ShareMap *>(ptr);
    const char *pKey = env->GetStringUTFChars(key, JNI_FALSE);
    char *pResult = share_map_get_share_by_key(pMap, const_cast<char *>(pKey), error_ptr);
    env->ReleaseStringUTFChars(key, pKey);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}