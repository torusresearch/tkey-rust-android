#include <jni.h>
#include "include/tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_TssOptions_jniTssOptionsFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pointer = reinterpret_cast<TssOptions *>(pObject);
    tss_options_free(pointer);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_TssOptions_jniTssOptions(JNIEnv *env, __attribute__((unused)) jobject jthis,
                                                                    jstring input_tss_share,
                                                                    jint input_tss_index,
                                                                    jstring auth_signtatures,
                                                                    jobject factor_pub,
                                                                    jstring selected_servers,
                                                                    jint new_tss_index,
                                                                    jobject new_factor_pub,
                                                                    jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pShare = env->GetStringUTFChars(input_tss_share, JNI_FALSE);
    const char *pSig = env->GetStringUTFChars(auth_signtatures, JNI_FALSE);

    auto* pFactor = reinterpret_cast<KeyPoint*>(GetPointerField(env,factor_pub));

    KeyPoint* pNewFactor = nullptr;
    if (factor_pub != nullptr) {
        pNewFactor = reinterpret_cast<KeyPoint*>(GetPointerField(env,new_factor_pub));
    }

    const char *pSelected = nullptr;
    if (selected_servers != nullptr) {
        pSelected = env->GetStringUTFChars(selected_servers, JNI_FALSE);
    }

    int* pNewIndex = &new_tss_index;

    auto *pResult = tss_options(
            const_cast<char *>(pShare),
            input_tss_index,
            pFactor,
            const_cast<char *>(pSig),
            const_cast<char *>(pSelected),
            pNewIndex,
            pNewFactor,
            error_ptr);

    if (pSelected != nullptr) {
        env->ReleaseStringUTFChars(selected_servers, pSelected);
    }
    env->ReleaseStringUTFChars(input_tss_share, pShare);
    env->ReleaseStringUTFChars(auth_signtatures, pSig);
    setErrorCode(env, error, errorCode);

    return reinterpret_cast<jlong>(pResult);
}