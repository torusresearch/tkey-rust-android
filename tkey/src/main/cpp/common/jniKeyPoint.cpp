#include <jni.h>
#include "tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_KeyPoint_jniKeyPointEncode(
        JNIEnv *env, jobject jthis, jstring format, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyPoint *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    const char *pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    char *pResult = point_encode(pKeyPoint, const_cast<char *>(pFormat), error_ptr);
    env->ReleaseStringUTFChars(format, pFormat);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_KeyPoint_jniKeyPointGetX(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyPoint *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    char *pX = point_get_x(pKeyPoint, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pX);
    string_free(pX);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_KeyPoint_jniKeyPointGetY(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyPoint *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    char *pY = point_get_y(pKeyPoint, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pY);
    string_free(pY);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_KeyPoint_jniKeyPointFree(
        JNIEnv *env, jobject jthis) {
    long pObject = GetPointerField(env, jthis);
    KeyPoint *pKeyPoint = reinterpret_cast<KeyPoint *>(pObject);
    point_free(pKeyPoint);
}