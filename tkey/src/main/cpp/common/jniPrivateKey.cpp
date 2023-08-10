#include <jni.h>
#include "include/tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_PrivateKey_jniGeneratePrivateKey(
        JNIEnv *env, __attribute__((unused)) jclass jclazz, jstring curve, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pCurve = env->GetStringUTFChars(curve, JNI_FALSE);
    char *pPrivateKey = generate_private_key(const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(curve, pCurve);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pPrivateKey);
    string_free(pPrivateKey);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_PrivateKey_jniPrivateToPublic(
        JNIEnv *env, __attribute__((unused)) jclass jclazz, jstring secret, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pSecret = env->GetStringUTFChars(secret, JNI_FALSE);
    
    char *pPublicKey = private_to_public(const_cast<char *>(pSecret), error_ptr);
    
    env->ReleaseStringUTFChars(secret, pSecret);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pPublicKey);
    string_free(pPublicKey);
    return result;
}