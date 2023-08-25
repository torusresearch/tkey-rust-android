#include <jni.h>
#include "include/tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_KeyPoint_jniKeyPointEncode(
        JNIEnv *env, jobject jthis, jstring format, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    const char *pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    char *pResult = key_point_encode(pKeyPoint, const_cast<char *>(pFormat), error_ptr);
    env->ReleaseStringUTFChars(format, pFormat);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_KeyPoint_jniKeyPointGetX(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    char *pX = key_point_get_x(pKeyPoint, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pX);
    string_free(pX);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_KeyPoint_jniKeyPointGetY(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    char *pY = key_point_get_y(pKeyPoint, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pY);
    string_free(pY);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_KeyPoint_jniKeyPointFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    key_point_free(pKeyPoint);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_KeyPoint_jniKeyPointNew(JNIEnv *env, jobject thiz,
                                                                   jstring x, jstring y,
                                                                   jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pX = env->GetStringUTFChars(x, JNI_FALSE);
    const char *pY = env->GetStringUTFChars(y, JNI_FALSE);
    auto *pPoint = key_point_new(const_cast<char *>(pX), const_cast<char *>(pY), error_ptr);
    env->ReleaseStringUTFChars(x, pX);
    env->ReleaseStringUTFChars(y, pY);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pPoint);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_KeyPoint_jniKeyPointNewAddr(JNIEnv *env, jobject thiz,
                                                                   jstring address,
                                                                   jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pAddress = env->GetStringUTFChars(address, JNI_FALSE);

    auto *pPoint = key_point_new_addr(const_cast<char *>(pAddress), error_ptr);
    
    env->ReleaseStringUTFChars(address, pAddress);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pPoint);
}