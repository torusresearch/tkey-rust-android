#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ServiceProvider_jniServiceProviderFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pProvider = reinterpret_cast<ServiceProvider *>(pObject);
    service_provider_free(pProvider);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ServiceProvider_jniServiceProvider(
        JNIEnv *env, __attribute__((unused)) jobject jthis, jboolean enable_logging,
        jstring postbox_key, jstring curve_n, jboolean use_tss,
        jstring verifier_name, jstring verifier_id, jobject sss, jobject tss, jobject rss,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pPostbox = env->GetStringUTFChars(postbox_key, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);

    const char *pName = nullptr;
    if (verifier_name != nullptr) {
        pName = env->GetStringUTFChars(verifier_name, JNI_FALSE);
    }

    const char *pId = nullptr;
    if (verifier_id != nullptr) {
        pId = env->GetStringUTFChars(verifier_id, JNI_FALSE);
    }

    NodeDetails* pS = nullptr;
    if (sss != nullptr) {
        pS = reinterpret_cast<NodeDetails*>(GetPointerField(env,sss));
    }

    NodeDetails* pT = nullptr;
    if (tss != nullptr) {
        pT = reinterpret_cast<NodeDetails*>(GetPointerField(env,tss));
    }

    NodeDetails* pR = nullptr;
    if (rss != nullptr) {
        pR = reinterpret_cast<NodeDetails*>(GetPointerField(env,rss));
    }

    auto *pResult = service_provider(enable_logging, const_cast<char *>(pPostbox),
                                     const_cast<char *>(pCurve), use_tss, const_cast<char *>(pName), const_cast<char *>(pId), pT, pR, pS, error_ptr);
    if (verifier_name != nullptr) {
        env->ReleaseStringUTFChars(verifier_name, pName);
    }
    if (verifier_id != nullptr) {
        env->ReleaseStringUTFChars(verifier_id, pId);
    }

    env->ReleaseStringUTFChars(postbox_key, pPostbox);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}