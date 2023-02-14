#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"


extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareStorePolyIdIndexMap_jniShareStorePolyIdIndexMapGetKeys(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pMap = reinterpret_cast<ShareStorePolyIDShareIndexMap *>(pObject);
    char *pResult = share_store_poly_id_index_map_get_keys(pMap, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareStorePolyIdIndexMap_jniShareStorePolyIdIndexMapMapGetValueByKey(
        JNIEnv *env, jobject jthis, jstring key, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pMap = reinterpret_cast<ShareStorePolyIDShareIndexMap *>(pObject);
    const char *pKey = env->GetStringUTFChars(key, JNI_FALSE);
    auto *pResult = share_store_poly_id_index_map_get_value_by_key(pMap,
                                                                            const_cast<char *>(pKey),
                                                                            error_ptr);
    env->ReleaseStringUTFChars(key, pKey);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareStorePolyIdIndexMap_jniShareStorePolyIdIndexMapMapFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pMap = reinterpret_cast<ShareStorePolyIDShareIndexMap *>(pObject);
    share_store_poly_id_index_map_free(pMap);
}