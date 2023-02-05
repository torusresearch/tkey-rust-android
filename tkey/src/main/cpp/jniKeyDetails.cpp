#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyDetails_jniKeyDetailsFree(JNIEnv *env, jobject jthis) {
    long pObject = GetPointerField(env, jthis);
    KeyDetails *pDetails = reinterpret_cast<KeyDetails *>(pObject);
    key_details_free(pDetails);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyDetails_jniKeyDetailsGetPublicKeyPoint(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyDetails *pDetails = reinterpret_cast<KeyDetails *>(pObject);
    KeyPoint *pResult = key_details_get_pub_key_point(pDetails,error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyDetails_jniKeyDetailsGetThreshold(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyDetails *pDetails = reinterpret_cast<KeyDetails *>(pObject);
    int result = key_details_get_threshold(pDetails,error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyDetails_jniKeyDetailsGetRequiredShares(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyDetails *pDetails = reinterpret_cast<KeyDetails *>(pObject);
    int result = key_details_get_required_shares(pDetails,error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyDetails_jniKeyDetailsGetTotalShares(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyDetails *pDetails = reinterpret_cast<KeyDetails *>(pObject);
    int result = key_details_get_total_shares(pDetails,error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyDetails_jniKeyDetailsGetShareDescriptions(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    KeyDetails *pStore = reinterpret_cast<KeyDetails *>(pObject);
    char *pResult = key_details_get_share_descriptions(pStore,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}