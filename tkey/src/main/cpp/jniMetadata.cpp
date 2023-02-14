#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Metadata_jniMetadataFree(JNIEnv *env,
                                                                                    jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pMeta = reinterpret_cast<Metadata *>(pObject);
    metadata_free(pMeta);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Metadata_jniMetadataFromJson(
        JNIEnv *env, __attribute__((unused)) jobject jthis, jstring json, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pJson = env->GetStringUTFChars(json, JNI_FALSE);
    auto *pResult = metadata_from_json(const_cast<char *>(pJson), error_ptr);
    env->ReleaseStringUTFChars(json, pJson);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Metadata_jniMetadataToJson(JNIEnv *env,
                                                                                      jobject jthis,
                                                                                      jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pMeta = reinterpret_cast<Metadata *>(pObject);
    char *pResult = metadata_to_json(pMeta, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}