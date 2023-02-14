#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyReconstructionDetails_jniKeyReconstructionDetailsGetPrivateKey(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    KeyReconstructionDetails *pDetails = reinterpret_cast<KeyReconstructionDetails *>(pObject);
    char *pResult = key_reconstruction_get_private_key(pDetails, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyReconstructionDetails_jniKeyReconstructionDetailsGetSeedPhraseLen(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    KeyReconstructionDetails *pDetails = reinterpret_cast<KeyReconstructionDetails *>(pObject);
    int result = key_reconstruction_get_seed_phrase_len(pDetails, error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyReconstructionDetails_jniKeyReconstructionDetailsGetSeedPhraseAt(
        JNIEnv *env, jobject jthis, jint index, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    KeyReconstructionDetails *pDetails = reinterpret_cast<KeyReconstructionDetails *>(pObject);
    char *pResult = key_reconstruction_get_seed_phrase_at(pDetails, index, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyReconstructionDetails_jniKeyReconstructionDetailsGetAllKeysLen(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    KeyReconstructionDetails *pDetails = reinterpret_cast<KeyReconstructionDetails *>(pObject);
    int result = key_reconstruction_get_all_keys_len(pDetails, error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyReconstructionDetails_jniKeyReconstructionDetailsGetAllKeysAt(
        JNIEnv *env, jobject jthis, jint index, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    KeyReconstructionDetails *pDetails = reinterpret_cast<KeyReconstructionDetails *>(pObject);
    char *pResult = key_reconstruction_get_all_keys_at(pDetails, index, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_KeyReconstructionDetails_jniKeyReconstructionDetailsFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    KeyReconstructionDetails *pDetails = reinterpret_cast<KeyReconstructionDetails *>(pObject);
    key_reconstruction_details_free(pDetails);
}