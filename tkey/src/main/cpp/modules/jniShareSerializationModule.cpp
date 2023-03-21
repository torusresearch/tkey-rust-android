#include <jni.h>
#include "include/tkey.h"
#include "../common/jniCommon.cpp"


extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_ShareSerializationModule_jniShareSerializationModuleSerializeShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring share, jstring format,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pShare = env->GetStringUTFChars(share, JNI_FALSE);
    const char *pFormat = nullptr;
    if (format != nullptr) {
        pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    }
    char *pResult = share_serialization_serialize_share(pointer, const_cast<char *>(pShare),
                                                        const_cast<char *>(pFormat), error_ptr);
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
Java_com_web3auth_tkey_ThresholdKey_Modules_ShareSerializationModule_jniShareSerializationModuleDeserializeShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring share, jstring format,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pShare = env->GetStringUTFChars(share, JNI_FALSE);
    const char *pFormat = nullptr;
    if (format != NULL) {
        pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    }
    char *pResult = share_serialization_deserialize_share(pointer, const_cast<char *>(pShare),
                                                          const_cast<char *>(pFormat), error_ptr);
    if (pFormat != nullptr) {
        env->ReleaseStringUTFChars(format, pFormat);
    }
    env->ReleaseStringUTFChars(share, pShare);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}