#include <jni.h>
#include <android/log.h>
#include "include/tkey.h"
#include <string>
#include <cmath>
#include <android/log.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_GenerateShareStoreResult_jniGenerateShareStoreResultGetShareIndex(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pStore = reinterpret_cast<GenerateShareStoreResult *>(pObject);
    char *pResult = generate_new_share_store_result_get_shares_index(pStore, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_GenerateShareStoreResult_jniGenerateShareStoreResultGetShareStoreMap(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pStore = reinterpret_cast<GenerateShareStoreResult *>(pObject);
    auto *pResult = generate_new_share_store_result_get_share_store_map(pStore, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_GenerateShareStoreResult_jniGenerateShareStoreResultFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pStore = reinterpret_cast<GenerateShareStoreResult *>(pObject);
    generate_share_store_result_free(pStore);
}