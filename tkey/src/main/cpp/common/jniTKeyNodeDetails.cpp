#include <jni.h>
#include "include/tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_TKeyNodeDetails_jniTKeyNodeDetailsFree(JNIEnv *env,
                                                                          jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pointer = reinterpret_cast<NodeDetails *>(pObject);
    node_details_free(pointer);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_TKeyNodeDetails_jniTKeyNodeDetails(JNIEnv *env, jobject jthis,
                                                                      jstring server_endpoints,
                                                                      jstring server_public_keys,
                                                                      jint server_threshold,
                                                                      jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pEndpoints = env->GetStringUTFChars(server_endpoints, JNI_FALSE);
    const char *pKeys = env->GetStringUTFChars(server_public_keys, JNI_FALSE);
    auto *pResult = node_details(const_cast<char *>(pEndpoints),const_cast<char *>(pKeys), server_threshold, error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(server_endpoints, pEndpoints);
    env->ReleaseStringUTFChars(server_public_keys, pKeys);
    return reinterpret_cast<jlong>(pResult);
}