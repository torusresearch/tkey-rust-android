#include <jni.h>
#include "include/tkey.h"
#include "../common/jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSModuleGetTSSPublicKey(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    
    char *pResult = threshold_key_get_tss_public_key(pointer, error_ptr);
    
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSModuleGetAllTSSTags(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    
    char *pResult = threshold_key_get_all_tss_tags(pointer, error_ptr);
    
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSTagFactorPub(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    
    char *pResult = threshold_key_get_tss_tag_factor_pub(pointer, error_ptr);
    
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniGetExtendedVerifier(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    
    char *pResult = threshold_key_get_extended_verifier_id(pointer, error_ptr);
    
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

// init return type 
// function name
// init params
// conversion for string and int done correctly 
// function called with app. init to result
// free converted params
// set error code
// return result if return type is present(string free for result)

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSModuleSetTSSTag(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring tss_tag,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pTSSTag = env->GetStringUTFChars(tss_tag, JNI_FALSE);

    threshold_key_set_tss_tag(pointer, const_cast<char *>(pTSSTag), error_ptr);

    env->ReleaseStringUTFChars(tss_tag, pTSSTag);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSModuleGetTSSTag(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    
    char *pResult = threshold_key_get_tss_tag(pointer, error_ptr);
    
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSModuleCreateTaggedTSSShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring device_tss_share,
        jstring factor_pub,
        jint device_tss_index,
        jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));


    const char *pDeviceTSSShare = nullptr;
    if(device_tss_share != nullptr) {
        pDeviceTSSShare = env->GetStringUTFChars(device_tss_share, JNI_FALSE);
    }
    const char *pFactorPub = env->GetStringUTFChars(factor_pub, JNI_FALSE);
    const char *pCurveN = env->GetStringUTFChars(curve_n, JNI_FALSE);
    int* pTssIndex = &device_tss_index; // int can go straight through due to it being a simple type, only error_ptr is a pointer to an int (int *) since it functions as an inout parameter

    threshold_key_create_tagged_tss_share(pointer,
                                                          const_cast<char *>(pDeviceTSSShare),
                                                          const_cast<char *>(pFactorPub),
                                                          device_tss_index,
                                                          const_cast<char *>(pCurveN),
                                                          error_ptr);


    setErrorCode(env, error, errorCode);
    if(device_tss_share != nullptr) {
        env->ReleaseStringUTFChars(device_tss_share, pDeviceTSSShare);
    }
    env->ReleaseStringUTFChars(factor_pub, pFactorPub);
    env->ReleaseStringUTFChars(curve_n, pCurveN);

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniTSSModuleGetTSSShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring factor_key,
        jint threshold,
        jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pFactorKey = env->GetStringUTFChars(factor_key, JNI_FALSE);
    const char *pCurveN = env->GetStringUTFChars(curve_n, JNI_FALSE);
    int* pThreshold = &threshold;

    char *pResult = threshold_key_get_tss_share(pointer, const_cast<char *>(pFactorKey), threshold, const_cast<char *>(pCurveN), error_ptr);
    
    env->ReleaseStringUTFChars(factor_key, pFactorKey);
    env->ReleaseStringUTFChars(curve_n, pCurveN);

    setErrorCode(env, error, errorCode);
    
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniGetTSSNonce(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring tss_tag,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    
    const char *pTSSTag = env->GetStringUTFChars(tss_tag, JNI_FALSE);
    unsigned int result = threshold_key_get_tss_nonce(pointer, 
                                                      const_cast<char *>(pTSSTag),
                                                      error_ptr);
    
    env->ReleaseStringUTFChars(tss_tag, pTSSTag);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniCopyFactorPub(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring new_factor_pub,
        jint new_tss_index,
        jstring factor_pub,
        jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
                                                                        
    const char *pNewFactorPub = env->GetStringUTFChars(new_factor_pub, JNI_FALSE);
    const char *pFactorPub = env->GetStringUTFChars(factor_pub, JNI_FALSE);
    const char *pCurveN = env->GetStringUTFChars(curve_n, JNI_FALSE);
    int* pNewTssIndex = &new_tss_index;

    threshold_key_copy_factor_pub(pointer, 
                                    const_cast<char *>(pNewFactorPub),
                                    new_tss_index,
                                    const_cast<char *>(pFactorPub),
                                    const_cast<char *>(pCurveN),
                                    error_ptr);


    env->ReleaseStringUTFChars(new_factor_pub, pNewFactorPub);
    env->ReleaseStringUTFChars(factor_pub, pFactorPub);
    env->ReleaseStringUTFChars(curve_n, pCurveN);

    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniGenerateTSSShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring input_tss_share,
        jint input_tss_index,
        jint new_tss_index,
        jstring new_factor_pub,
        jstring selected_servers,
        jstring auth_signatures,
        jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
                                                                        
    const char *pInputTSSShare = env->GetStringUTFChars(input_tss_share, JNI_FALSE);
    const char *pNewFactorPub = env->GetStringUTFChars(new_factor_pub, JNI_FALSE);
    const char *pSelectedServers = nullptr;
    if(selected_servers != nullptr) {
        pSelectedServers = env->GetStringUTFChars(selected_servers, JNI_FALSE);
    }
    const char *pAuthSignatures = env->GetStringUTFChars(auth_signatures, JNI_FALSE);
    const char *pCurveN = env->GetStringUTFChars(curve_n, JNI_FALSE);
    int* pInputTssIndex = &input_tss_index;
    int* pNewTssIndex = &new_tss_index;

    threshold_key_generate_tss_share(pointer, 
                                    const_cast<char *>(pInputTSSShare),
                                     *pInputTssIndex,
                                     *pNewTssIndex,
                                    const_cast<char *>(pNewFactorPub),
                                    const_cast<char *>(pSelectedServers),
                                    const_cast<char *>(pAuthSignatures),
                                    const_cast<char *>(pCurveN),
                                    error_ptr);


    env->ReleaseStringUTFChars(input_tss_share, pInputTSSShare);
    env->ReleaseStringUTFChars(new_factor_pub, pNewFactorPub);
    env->ReleaseStringUTFChars(auth_signatures, pAuthSignatures);
    env->ReleaseStringUTFChars(curve_n, pCurveN);
    if(selected_servers != nullptr) {
        env->ReleaseStringUTFChars(selected_servers, pSelectedServers);
    }
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniDeleteTSSShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring input_tss_share,
        jint input_tss_index,
        jstring factor_pub,
        jstring selected_servers,
        jstring auth_signatures,
        jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
                                                                        
    const char *pInputTSSShare = env->GetStringUTFChars(input_tss_share, JNI_FALSE);
    const char *pFactorPub = env->GetStringUTFChars(factor_pub, JNI_FALSE);
    const char *pAuthSignatures = env->GetStringUTFChars(auth_signatures, JNI_FALSE);
    const char *pCurveN = env->GetStringUTFChars(curve_n, JNI_FALSE);
    int* pInputTssIndex = &input_tss_index;
    const char *pSelectedServers = nullptr;
    if(selected_servers != nullptr) {
        pSelectedServers = env->GetStringUTFChars(selected_servers, JNI_FALSE);
    }
    threshold_key_delete_tss_share(pointer, 
                                    const_cast<char *>(pInputTSSShare),
                                    *pInputTssIndex,
                                    const_cast<char *>(pFactorPub),
                                    const_cast<char *>(pSelectedServers),
                                    const_cast<char *>(pAuthSignatures),
                                    const_cast<char *>(pCurveN),
                                    error_ptr);


    env->ReleaseStringUTFChars(input_tss_share, pInputTSSShare);
    env->ReleaseStringUTFChars(factor_pub, pFactorPub);
    env->ReleaseStringUTFChars(auth_signatures, pAuthSignatures);
    env->ReleaseStringUTFChars(curve_n, pCurveN);
    if(selected_servers != nullptr) {
        env->ReleaseStringUTFChars(selected_servers, pSelectedServers);
    }
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Modules_TSSModule_jniThresholdKeyServiceProviderAssignPublicKey(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring tss_tag, jstring tss_nonce, jstring tss_public_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;

    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));

    const char *pTag = env->GetStringUTFChars(tss_tag, JNI_FALSE);
    const char *pNonce = env->GetStringUTFChars(tss_nonce, JNI_FALSE);
    const char *pKey = env->GetStringUTFChars(tss_public_key, JNI_FALSE);
    threshold_key_service_provider_assign_tss_public_key(pointer, const_cast<char *>(pTag), const_cast<char *>(pNonce), const_cast<char *>(pKey),
                                                          error_ptr);
    env->ReleaseStringUTFChars(tss_tag, pTag);
    env->ReleaseStringUTFChars(tss_nonce, pNonce);
    env->ReleaseStringUTFChars(tss_public_key, pKey);
    setErrorCode(env, error, errorCode);
}
