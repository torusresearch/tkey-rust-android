#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayNew(JNIEnv *env, jobject jthis) {
    auto *pArr = key_point_array_new();
    return reinterpret_cast<jlong>(pArr);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    key_point_array_free(pArr);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayRemoveAt(JNIEnv *env,
                                                                           jobject jthis, jint index,
                                                                           jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    key_point_array_remove(pArr,index, error_ptr);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayInsert(JNIEnv *env, jobject jthis,
                                                                         jobject point,
                                                                         jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    jlong pPointObject = GetPointerField(env, point);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    auto *pPoint = reinterpret_cast<KeyPoint *>(pPointObject);
    key_point_array_insert(pArr,pPoint, error_ptr);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayUpdateAt(JNIEnv *env,
                                                                           jobject jthis,
                                                                           jobject point,
                                                                           jint index,
                                                                           jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    jlong pPointObject = GetPointerField(env, point);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    auto *pPoint = reinterpret_cast<KeyPoint *>(pPointObject);
    key_point_array_update_at_index(pArr, index,pPoint, error_ptr);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayGetAt(JNIEnv *env, jobject jthis,
                                                                        jint index,
                                                                        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    auto *pResult = key_point_array_get_value_by_index(pArr,index, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayLen(JNIEnv *env, jobject jthis,
                                                                      jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    unsigned int result = key_point_array_get_len(pArr, error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayLagrange(JNIEnv *env,
                                                                           jobject jthis,
                                                                           jstring curveN,
                                                                           jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pArr = reinterpret_cast<KeyPointArray *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    auto *pResult = lagrange_interpolate_polynomial(pArr,const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}