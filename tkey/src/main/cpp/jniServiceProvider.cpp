#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ServiceProvider_jniServiceProviderFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pProvider = reinterpret_cast<ServiceProvider *>(pObject);
    service_provider_free(pProvider);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ServiceProvider_jniServiceProvider(
        JNIEnv *env, __attribute__((unused)) jobject jthis, jboolean enable_logging,
        jstring postbox_key, jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pPostbox = env->GetStringUTFChars(postbox_key, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    auto *pResult = service_provider(enable_logging, const_cast<char *>(pPostbox),
                                     const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(postbox_key, pPostbox);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}