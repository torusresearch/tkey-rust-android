#include <jni.h>
#include "tkey.h"
#include "../common/jniCommon.cpp"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SecurityQuestionModule_jniSecurityQuestionModuleGenerateShareStoreResult(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring questions, jstring answer,
        jstring curveN,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pQuestions = env->GetStringUTFChars(questions, JNI_FALSE);
    const char *pAnswer = env->GetStringUTFChars(answer, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    long result = reinterpret_cast<long>(
            security_question_generate_new_share(pointer,
                                                 const_cast<char *>(pQuestions),
                                                 const_cast<char *>(pAnswer),
                                                 const_cast<char *>(pCurve), error_ptr));
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(questions, pQuestions);
    env->ReleaseStringUTFChars(answer, pAnswer);
    env->ReleaseStringUTFChars(curveN, pCurve);
    return result;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SecurityQuestionModule_jniSecurityQuestionModuleInputShare(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring answer, jstring curveN,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pAnswer = env->GetStringUTFChars(answer, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    bool result =
            security_question_input_share(pointer,
                                          const_cast<char *>(pAnswer),
                                          const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(answer, pAnswer);
    env->ReleaseStringUTFChars(curveN, pCurve);
    return result;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SecurityQuestionModule_jniSecurityQuestionModuleChangeQuestionAndAnswer(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring questions, jstring answer,
        jstring curveN,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pQuestions = env->GetStringUTFChars(questions, JNI_FALSE);
    const char *pAnswer = env->GetStringUTFChars(answer, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    bool result =
            security_question_change_question_and_answer(pointer,
                                                         const_cast<char *>(pQuestions),
                                                         const_cast<char *>(pAnswer),
                                                         const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(questions, pQuestions);
    env->ReleaseStringUTFChars(answer, pAnswer);
    env->ReleaseStringUTFChars(curveN, pCurve);
    return result;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SecurityQuestionModule_jniSecurityQuestionModuleStoreAnswer(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jstring answer, jstring curveN,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    const char *pAnswer = env->GetStringUTFChars(answer, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    bool result =
            security_question_store_answer(pointer,
                                           const_cast<char *>(pAnswer),
                                           const_cast<char *>(pCurve), error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(answer, pAnswer);
    env->ReleaseStringUTFChars(curveN, pCurve);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SecurityQuestionModule_jniSecurityQuestionModuleGetAnswer(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    char *pResult =
            security_question_get_answer(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Modules_SecurityQuestionModule_jniSecurityQuestionModuleGetQuestions(
        JNIEnv *env, __attribute__((unused)) jclass clazz, jobject threshold_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    auto *pointer = reinterpret_cast<FFIThresholdKey *>(GetPointerField(env,
                                                                                   threshold_key));
    char *pResult =
            security_question_get_questions(pointer, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}