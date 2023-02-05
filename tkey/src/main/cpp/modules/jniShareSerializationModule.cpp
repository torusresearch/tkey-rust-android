#include <jni.h>
#include "tkey.h"
#include "../common/jniCommon.cpp"


extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_ShareSerializationModule_jniShareSerializationModuleSerializeShare(
        JNIEnv *env, jclass clazz, jlong threshold_key,
        jstring share, jstring format,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(threshold_key);
    const char * pShare = env->GetStringUTFChars(share, JNI_FALSE);
    const char *pFormat = nullptr;
    if (format != NULL) {
        pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    }
    char *pResult = share_serialization_serialize_share(pointer,const_cast<char*>(pShare), const_cast<char*>(pFormat), error_ptr);
    if (pFormat != nullptr) {
        env->ReleaseStringUTFChars(format, pFormat);
    }
    env->ReleaseStringUTFChars(share, pShare);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_ShareSerializationModule_jniShareSerializationModuleDeserializeShare(
        JNIEnv *env, jclass clazz, jlong threshold_key,
        jstring share, jstring format,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(threshold_key);
    const char * pShare = env->GetStringUTFChars(share, JNI_FALSE);
    const char *pFormat = nullptr;
    if (format != NULL) {
        pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    }
    char *pResult = share_serialization_serialize_share(pointer,const_cast<char*>(pShare), const_cast<char*>(pFormat), error_ptr);
    if (pFormat != nullptr) {
        env->ReleaseStringUTFChars(format, pFormat);
    }
    env->ReleaseStringUTFChars(share, pShare);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}