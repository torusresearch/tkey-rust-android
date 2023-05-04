#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareStoreArray_jniShareStoreArrayFree(JNIEnv *env,
                                                                           jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<ShareStoreArray *>(pObject);
    share_store_array_free(pArr);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareStoreArray_jniShareStoreArrayGetAt(JNIEnv *env,
                                                                            jobject jthis,
                                                                            jint index,
                                                                            jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<ShareStoreArray *>(pObject);
    auto *pResult = share_store_array_get_value_by_index(pArr, index, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareStoreArray_jniShareStoreArrayLen(JNIEnv *env,
                                                                          jobject jthis,
                                                                          jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<ShareStoreArray *>(pObject);
    unsigned int result = share_store_array_get_len(pArr, error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}