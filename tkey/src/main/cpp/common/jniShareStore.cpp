#include <jni.h>
#include "tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_ShareStore_jniShareStoreFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    ShareStore *pointer = reinterpret_cast<ShareStore *>(pObject);
    share_store_free(pointer);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_ShareStore_jniSharestoreToJson(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    ShareStore *pStore = reinterpret_cast<ShareStore *>(pObject);
    char *pResult = share_store_to_json(pStore,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_ShareStore_jniShareStoreGetShare(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    ShareStore *pStore = reinterpret_cast<ShareStore *>(pObject);
    char *pResult = share_store_get_share(pStore,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_ShareStore_jniShareStoreGetShareIndex(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    ShareStore *pStore = reinterpret_cast<ShareStore *>(pObject);
    char *pResult = share_store_get_share_index(pStore,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_ShareStore_jniShareStoreGetPolynomialId(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    ShareStore *pStore = reinterpret_cast<ShareStore *>(pObject);
    char *pResult = share_store_get_polynomial_id(pStore,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_ShareStore_jniSharestoreFromJson(
        JNIEnv *env, jobject jthis, jstring json, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pJson = env->GetStringUTFChars(json, JNI_FALSE);
    ShareStore *pResult = share_store_from_json(const_cast<char *>(pJson),error_ptr);
    setErrorCode(env, error, errorCode);
    env->ReleaseStringUTFChars(json, pJson);
    return reinterpret_cast<jlong>(pResult);
}