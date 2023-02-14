#include <jni.h>
#include "tkey.h"
#include "../common/jniCommon.cpp"

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_PrivateKeysModule_jniPrivateKeysModuleSetPrivateKey(
        JNIEnv *env, jclass clazz, jobject threshold_key,
        jstring key, jstring format,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,threshold_key));
    const char *pKey = nullptr;
    if (key != NULL) {
        pKey = env->GetStringUTFChars(key, JNI_FALSE);
    }
    const char *pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    bool result = private_keys_set_private_key(pointer,const_cast<char *>(pKey),const_cast<char *>(pFormat),const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(format, pFormat);
    if (pKey != nullptr) {
        env->ReleaseStringUTFChars(key, pKey);
    }
    env->ReleaseStringUTFChars(curve_n, pCurve);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_PrivateKeysModule_jniPrivateKeysModuleGetPrivateKey(
        JNIEnv *env, jclass clazz, jobject threshold_key, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,threshold_key));
    char *pResult = private_keys_get_private_keys(pointer,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_PrivateKeysModule_jniPrivateKeysModuleGetPrivateKeyAccounts(
        JNIEnv *env, jclass clazz, jobject threshold_key, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,threshold_key));
    char *pResult = private_keys_get_accounts(pointer,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}