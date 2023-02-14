#include <jni.h>
#include "tkey.h"
#include "../common/jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleRequestNewShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring agent, jstring indexes,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pAgent = env->GetStringUTFChars(agent, JNI_FALSE);
    const char *pIndexes = env->GetStringUTFChars(indexes, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    char *pResult =
            share_transfer_request_new_share(pointer,
                                             const_cast<char *>(pAgent),
                                             const_cast<char *>(pIndexes),
                                             const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(agent, pAgent);
    env->ReleaseStringUTFChars(indexes, pIndexes);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleAddCustomInfoToRequest(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring enc_pub_key_x, jstring custom_info,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pEnc = env->GetStringUTFChars(enc_pub_key_x, JNI_FALSE);
    const char *pCustom = env->GetStringUTFChars(custom_info, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    share_transfer_add_custom_info_to_request(pointer,
                                              const_cast<char *>(pEnc),
                                              const_cast<char *>(pCustom),
                                              const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(custom_info, pCustom);
    env->ReleaseStringUTFChars(enc_pub_key_x, pEnc);
    env->ReleaseStringUTFChars(curve_n, pCurve);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleLookForRequest(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    char *pResult =
            share_transfer_look_for_request(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleApproveRequest(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring enc_pub_key_x, jobject share_store,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pEnc = env->GetStringUTFChars(enc_pub_key_x, JNI_FALSE);
    auto *pStore = reinterpret_cast<ShareStore *>(GetPointerField(env, share_store));
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    share_transfer_approve_request(pointer,
                                   const_cast<char *>(pEnc),
                                   pStore,
                                   const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(enc_pub_key_x, pEnc);
    env->ReleaseStringUTFChars(curve_n, pCurve);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleApproveRequestWithShareIndex(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring enc_pub_key_x, jstring indexes,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pEnc = env->GetStringUTFChars(enc_pub_key_x, JNI_FALSE);
    const char *pIndexes = env->GetStringUTFChars(indexes, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    share_transfer_approve_request_with_share_indexes(pointer,
                                                      const_cast<char *>(pEnc),
                                                      const_cast<char *>(pIndexes),
                                                      const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(indexes, pIndexes);
    env->ReleaseStringUTFChars(enc_pub_key_x, pEnc);
    env->ReleaseStringUTFChars(curve_n, pCurve);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleGetStore(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    ShareTransferStore *result =
            share_transfer_get_store(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(result);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleSetStore(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jobject share_store, jstring curveN, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    auto *pStore = reinterpret_cast<ShareTransferStore *>(GetPointerField(env,
                                                                          share_store));
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    bool result = share_transfer_set_store(pointer, pStore, const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleDeleteStore(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring enc_pub_key_x, jstring curveN, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pEnc = env->GetStringUTFChars(enc_pub_key_x, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    bool result = share_transfer_delete_store(pointer, const_cast<char *>(pEnc),
                                              const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(enc_pub_key_x, pEnc);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleGetCurrentEncryptionKey(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    char *pResult =
            share_transfer_get_current_encryption_key(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleCleanupRequest(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    share_transfer_cleanup_request(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SharetransferModule_jniSharetransferModuleRequestStatusCheck(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring enc_pub_key_x,
        jboolean delete_on_completion, jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                        threshold_key));
    const char *pEnc = env->GetStringUTFChars(enc_pub_key_x, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    ShareStore *result = share_transfer_request_status_check(pointer, const_cast<char *>(pEnc),
                                                             delete_on_completion,
                                                             const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(enc_pub_key_x, pEnc);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(result);
}