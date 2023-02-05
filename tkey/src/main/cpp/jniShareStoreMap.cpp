#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareStoreMap_jniShareStoreMapFree(
        JNIEnv *env, jobject jthis) {
    long pObject = GetPointerField(env, jthis);
    ShareStoreMap *pMap = reinterpret_cast<ShareStoreMap *>(pObject);
    share_store_map_free(pMap);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareStoreMap_jniShareStoreMapGetKeys(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    ShareStoreMap *pMap = reinterpret_cast<ShareStoreMap *>(pObject);
    char *pResult = share_store_map_get_keys(pMap,error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareStoreMap_jniShareStoreMapGetValueByKey(
        JNIEnv *env, jobject jthis, jstring key, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    long pObject = GetPointerField(env, jthis);
    ShareStoreMap *pMap = reinterpret_cast<ShareStoreMap *>(pObject);
    const char *pKey = env->GetStringUTFChars(key, JNI_FALSE);
    ShareStore* pResult = share_store_map_get_value_by_key(pMap, const_cast<char*>(pKey),error_ptr);
    env->ReleaseStringUTFChars(key,pKey);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}