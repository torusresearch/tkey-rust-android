#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_LocalMetadataTransitions_jniLocalMetadataTransitionsFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pTransitions = reinterpret_cast<LocalMetadataTransitions *>(pObject);
    local_metadata_transitions_free(pTransitions);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_LocalMetadataTransitions_jniLocalMetadataTranstionsFromJson(
        JNIEnv *env, __attribute__((unused)) jobject jthiz, jstring json, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pJson = env->GetStringUTFChars(json, JNI_FALSE);
    auto *pResult = local_metadata_transitions_from_json(const_cast<char *>(pJson), error_ptr);
    env->ReleaseStringUTFChars(json, pJson);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_LocalMetadataTransitions_jniLocalMetadataTranstionsToJson(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pLocalMetadataTransitions = reinterpret_cast<LocalMetadataTransitions *>(pObject);
    char *pResult = local_metadata_transitions_to_json(pLocalMetadataTransitions, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}