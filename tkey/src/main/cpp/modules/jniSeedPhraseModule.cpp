#include <jni.h>
#include "tkey.h"
#include "../common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SeedPhraseModule_jniSeedPhraseModuleSetSeedPhrase(
        JNIEnv *env, jclass clazz, jobject threshold_key,
        jstring format, jstring phrase,
        jint wallets, jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pPhrase = nullptr;
    if (phrase != NULL) {
        pPhrase = env->GetStringUTFChars(phrase, JNI_FALSE);
    }
    const char *pFormat = env->GetStringUTFChars(format, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    seed_phrase_set_phrase(pointer, const_cast<char *>(pFormat), const_cast<char *>(pPhrase),
                           wallets, const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(format, pFormat);
    if (pPhrase != nullptr) {
        env->ReleaseStringUTFChars(phrase, pPhrase);
    }
    env->ReleaseStringUTFChars(curve_n, pCurve);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SeedPhraseModule_jniSeedPhraseModuleChangePhrase(
        JNIEnv *env, jclass clazz, jobject threshold_key,
        jstring old_phrase, jstring new_phrase,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pOld = env->GetStringUTFChars(old_phrase, JNI_FALSE);
    const char *pNew = env->GetStringUTFChars(new_phrase, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    seed_phrase_change_phrase(pointer,
                              const_cast<char *>(pOld),
                              const_cast<char *>(pNew),
                              const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(old_phrase, pOld);
    env->ReleaseStringUTFChars(new_phrase, pNew);
    env->ReleaseStringUTFChars(curve_n, pCurve);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SeedPhraseModule_jniSeedPhraseModuleGetSeedPhrases(
        JNIEnv *env, jclass clazz, jobject threshold_key, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    char *pResult = seed_phrase_get_seed_phrases(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SeedPhraseModule_jniSeedPhraseModuleDeletePhrase(
        JNIEnv *env, jclass clazz, jobject threshold_key,
        jstring phrase, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    FFIThresholdKey *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pPhrase = env->GetStringUTFChars(phrase, JNI_FALSE);
    seed_phrase_delete_seed_phrase(pointer,
                                   const_cast<char *>(pPhrase), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(phrase, pPhrase);
}