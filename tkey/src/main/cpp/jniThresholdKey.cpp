#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetMetadata(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    Metadata *pResult = threshold_key_get_current_metadata(pThreshold, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyReconstruct(
        JNIEnv *env, jobject jthis, jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    KeyReconstructionDetails *pResult = threshold_key_reconstruct(pThreshold,
                                                                  const_cast<char *>(pCurve),
                                                                  error_ptr);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGenerateNewShare(
        JNIEnv *env, jobject jthis, jstring curve_n, jboolean use_tss, jobject tss_options, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    TssOptions *pOptions = nullptr;
    if (tss_options != nullptr) {
        pOptions = reinterpret_cast<TssOptions *>(GetPointerField(env,tss_options));
    }
    GenerateShareStoreResult *pResult = threshold_key_generate_share(pThreshold,
                                                                     const_cast<char *>(pCurve), use_tss,
                                                                     pOptions,
                                                                     error_ptr);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyDeleteShare(
        JNIEnv *env, jobject jthis, jstring share_index,
        jstring curve_n, jboolean use_tss, jobject tss_options, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pIndex = env->GetStringUTFChars(share_index, JNI_FALSE);
    TssOptions *pOptions = nullptr;
    if (tss_options != nullptr) {
        pOptions = reinterpret_cast<TssOptions *>(GetPointerField(env,tss_options));
    }
    threshold_key_delete_share(pThreshold, const_cast<char *>(pIndex), const_cast<char *>(pCurve),
                               use_tss,pOptions,error_ptr);
    env->ReleaseStringUTFChars(share_index, pIndex);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetKeyDetails(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    KeyDetails *pResult = threshold_key_get_key_details(pThreshold, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyOutputShare(
        JNIEnv *env, jobject jthis, jstring share_index,
        jstring share_type, jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pIndex = env->GetStringUTFChars(share_index, JNI_FALSE);
    const char *pShareType = nullptr;
    if (share_type != nullptr) {
        pShareType = env->GetStringUTFChars(share_type, JNI_FALSE);
    }
    char *pResult = threshold_key_output_share(pThreshold, const_cast<char *>(pIndex),
                                               const_cast<char *>(pShareType),
                                               const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(share_index, pIndex);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    if (pShareType != nullptr) {
        env->ReleaseStringUTFChars(share_type, pShareType);
    }
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyOutputShareStore(
        JNIEnv *env, jobject jthis, jstring share_index,
        jstring poly_id, jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pIndex = env->GetStringUTFChars(share_index, JNI_FALSE);
    const char *pPolyId = nullptr;
    if (poly_id != nullptr) {
        pPolyId = env->GetStringUTFChars(poly_id, JNI_FALSE);
    }
    ShareStore *pResult = threshold_key_output_share_store(pThreshold, const_cast<char *>(pIndex),
                                                           const_cast<char *>(pPolyId),
                                                           const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(share_index, pIndex);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    if (pPolyId != nullptr) {
        env->ReleaseStringUTFChars(poly_id, pPolyId);
    }
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyShareToShareStore(
        JNIEnv *env, jobject jthis, jstring share,
        jstring curve_n, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pShare = env->GetStringUTFChars(share, JNI_FALSE);
    ShareStore *pResult = threshold_key_share_to_share_store(pThreshold, const_cast<char *>(pShare),
                                                             const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(share, pShare);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyInputShare(
        JNIEnv *env, jobject jthis, jstring share,
        jstring share_type, jstring curve_n,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pShare = env->GetStringUTFChars(share, JNI_FALSE);
    const char *pShareType = nullptr;
    if (share_type != nullptr) {
        pShareType = env->GetStringUTFChars(share_type, JNI_FALSE);
    }
    threshold_key_input_share(pThreshold, const_cast<char *>(pShare),
                              const_cast<char *>(pShareType), const_cast<char *>(pCurve),
                              error_ptr);
    env->ReleaseStringUTFChars(share, pShare);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    if (pShareType != nullptr) {
        env->ReleaseStringUTFChars(share_type, pShareType);
    }
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyInputShareStore(
        JNIEnv *env, jobject jthis, jobject share, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    auto *pShareStore = reinterpret_cast<ShareStore *>(GetPointerField(env, share));
    threshold_key_input_share_store(pThreshold, pShareStore, error_ptr);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetShareIndexes(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    char *pResult = threshold_key_get_shares_indexes(pThreshold, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetLastFetchedCloudMetadata(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    auto *pResult = threshold_key_get_last_fetched_cloud_metadata(pThreshold, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetLocalMetadataTransitions(
        JNIEnv *env, jobject jthis, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    auto *pResult = threshold_key_get_local_metadata_transitions(pThreshold,
                                                                 error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetTKeyStore(
        JNIEnv *env, jobject jthis, jstring module_name, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pModule = env->GetStringUTFChars(module_name, JNI_FALSE);
    char *pResult = threshold_key_get_tkey_store(pThreshold, const_cast<char *>(pModule),
                                                 error_ptr);
    env->ReleaseStringUTFChars(module_name, pModule);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetTKeyStoreItem(
        JNIEnv *env, jobject jthis, jstring module_name,
        jstring id, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pModule = env->GetStringUTFChars(module_name, JNI_FALSE);
    const char *pId = env->GetStringUTFChars(id, JNI_FALSE);
    char *pResult = threshold_key_get_tkey_store_item(pThreshold, const_cast<char *>(pModule),
                                                      const_cast<char *>(pId), error_ptr);
    env->ReleaseStringUTFChars(module_name, pModule);
    env->ReleaseStringUTFChars(id, pId);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeySyncLocalMetadataTransitions(
        JNIEnv *env, jobject jthis, jstring curveN, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    threshold_key_sync_local_metadata_transitions(pThreshold, const_cast<char *>(pCurve),
                                                  error_ptr);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    threshold_key_free(pThreshold);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKey(JNIEnv *env,
                                                                 __attribute__((unused)) jobject jthis,
                                                                 jobject metadata,
                                                                 jobject shares,
                                                                 jobject storage_layer,
                                                                 jobject service_provider,
                                                                 jobject local_transitions,
                                                                 jobject last_fetched_cloud_metadata,
                                                                 jboolean enable_logging,
                                                                 jboolean manual_sync,
                                                                 jobject rss,
                                                                 jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    Metadata *pMetadata = nullptr;
    if (metadata != nullptr) {
        jlong pObject = GetPointerField(env, metadata);
        pMetadata = reinterpret_cast<Metadata *>(pObject);
    }

    ShareStorePolyIDShareIndexMap *pShares = nullptr;
    if (shares != nullptr) {
        jlong pObject = GetPointerField(env, shares);
        pShares = reinterpret_cast<ShareStorePolyIDShareIndexMap *>(pObject);
    }

    ServiceProvider *pServiceProvider = nullptr;
    if (service_provider != nullptr) {
        jlong pObject = GetPointerField(env, service_provider);
        pServiceProvider = reinterpret_cast<ServiceProvider *>(pObject);
    }

    LocalMetadataTransitions *pTransitions = nullptr;
    if (local_transitions != nullptr) {
        jlong pObject = GetPointerField(env, local_transitions);
        pTransitions = reinterpret_cast<LocalMetadataTransitions *>(pObject);
    }

    Metadata *pCloudMetadata = nullptr;
    if (last_fetched_cloud_metadata != nullptr) {
        jlong pObject = GetPointerField(env, last_fetched_cloud_metadata);
        pCloudMetadata = reinterpret_cast<Metadata *>(pObject);
    }

    jlong pObject = GetPointerField(env, storage_layer);
    auto *pStorage = reinterpret_cast<FFIStorageLayer *>(pObject);

    FFIRssComm *pRss = nullptr;
    if (rss != nullptr) {
        pRss = reinterpret_cast<FFIRssComm *>(GetPointerField(env, rss));
    }

    FFIThresholdKey *pThreshold = threshold_key(pMetadata, pShares, pStorage, pServiceProvider,
                                                pTransitions, pCloudMetadata, enable_logging,
                                                manual_sync, pRss, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pThreshold);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyInitialize(
        JNIEnv *env, jobject jthis, jstring share, jobject input,
        jboolean never_initialized_new_key,
        jboolean include_local_metadata_transitions, jstring curve_n, jboolean use_tss, jstring device_tss_share, jint device_tss_index, jobject factor_pub, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pShare = nullptr;
    if (share != nullptr) {
        pShare = env->GetStringUTFChars(share, JNI_FALSE);
    }
    ShareStore *pInput = nullptr;
    if (input != nullptr) {
        jlong lInput = GetPointerField(env, input);
        pInput = reinterpret_cast<ShareStore *>(lInput);
    }
    const char *pTssShare = nullptr;
    if (device_tss_share != nullptr) {
        pTssShare = env->GetStringUTFChars(device_tss_share, JNI_FALSE);
    }

    KeyPoint *pFactor = nullptr;
    if (factor_pub != nullptr) {
        pFactor = reinterpret_cast<KeyPoint *>(GetPointerField(env, factor_pub));
    }

    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);

    int* pTssIndex = &device_tss_index;

    KeyDetails *pDetails = threshold_key_initialize(pThreshold, const_cast<char *>(pShare), pInput,
                                                    never_initialized_new_key,
                                                    include_local_metadata_transitions,
                                                    const_cast<char *>(pCurve), use_tss, const_cast<char *>(pTssShare), pTssIndex, pFactor,error_ptr);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    if (pShare != nullptr) {
        env->ReleaseStringUTFChars(share, pShare);
    }
    if (pTssShare != nullptr) {
        env->ReleaseStringUTFChars(device_tss_share, pTssShare);
    }
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pDetails);


}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetShares(JNIEnv *env,
                                                                          jobject jthis,
                                                                          jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    auto *pResult = threshold_key_get_shares(pThreshold,
                                             error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyDelete(JNIEnv *env, jobject jthis,
                                                                       jstring curve_n,
                                                                       jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    threshold_key_delete_tkey(pThreshold, const_cast<char *>(pCurve),
                              error_ptr);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyAddShareDescription(JNIEnv *env,
                                                                                    jobject jthis,
                                                                                    jstring key,
                                                                                    jstring description,
                                                                                    jboolean update_metadata,
                                                                                    jstring curve_n,
                                                                                    jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pKey = env->GetStringUTFChars(key, JNI_FALSE);
    const char *pDescription = env->GetStringUTFChars(description, JNI_FALSE);
    threshold_key_add_share_description(pThreshold, const_cast<char *>(pKey),
                                        const_cast<char *>(pDescription), update_metadata,
                                        const_cast<char *>(pCurve),
                                        error_ptr);
    env->ReleaseStringUTFChars(key, pKey);
    env->ReleaseStringUTFChars(description, pDescription);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyDeleteShareDescription(JNIEnv *env,
                                                                                       jobject jthis,
                                                                                       jstring key,
                                                                                       jstring description,
                                                                                       jboolean update_metadata,
                                                                                       jstring curve_n,
                                                                                       jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pKey = env->GetStringUTFChars(key, JNI_FALSE);
    const char *pDescription = env->GetStringUTFChars(description, JNI_FALSE);
    threshold_key_delete_share_description(pThreshold, const_cast<char *>(pKey),
                                           const_cast<char *>(pDescription), update_metadata,
                                           const_cast<char *>(pCurve),
                                           error_ptr);
    env->ReleaseStringUTFChars(key, pKey);
    env->ReleaseStringUTFChars(description, pDescription);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyUpdateShareDescription(JNIEnv *env,
                                                                                       jobject jthis,
                                                                                       jstring key,
                                                                                       jstring old_description,
                                                                                       jstring new_description,
                                                                                       jboolean update_metadata,
                                                                                       jstring curve_n,
                                                                                       jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curve_n, JNI_FALSE);
    const char *pKey = env->GetStringUTFChars(key, JNI_FALSE);
    const char *pOldDescription = env->GetStringUTFChars(old_description, JNI_FALSE);
    const char *pNewDescription = env->GetStringUTFChars(new_description, JNI_FALSE);
    threshold_key_update_share_description(pThreshold, const_cast<char *>(pKey),
                                           const_cast<char *>(pOldDescription),
                                           const_cast<char *>(pNewDescription), update_metadata,
                                           const_cast<char *>(pCurve),
                                           error_ptr);
    env->ReleaseStringUTFChars(key, pKey);
    env->ReleaseStringUTFChars(old_description, pOldDescription);
    env->ReleaseStringUTFChars(new_description, pNewDescription);
    env->ReleaseStringUTFChars(curve_n, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetShareDescriptions(JNIEnv *env,
                                                                                     jobject jthis,
                                                                                     jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    char *pResult = threshold_key_get_share_descriptions(pThreshold, error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyStorageLayerGetMetadata(JNIEnv *env,
                                                                                        jobject jthis,
                                                                                        jstring private_key,
                                                                                        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pKey = nullptr;
    if (private_key != nullptr) {
        pKey = env->GetStringUTFChars(private_key, JNI_FALSE);
    }
    char *pResult = threshold_key_get_metadata(pThreshold, const_cast<char *>(pKey),
                                               error_ptr);
    if (private_key != nullptr) {
        env->ReleaseStringUTFChars(private_key, pKey);
    }
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyStorageLayerSetMetadata(JNIEnv *env,
                                                                                        jobject jthis,
                                                                                        jstring private_key,
                                                                                        jstring json,
                                                                                        jstring curveN,
                                                                                        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pKey = nullptr;
    if (private_key != nullptr) {
        pKey = env->GetStringUTFChars(private_key, JNI_FALSE);
    }
    const char *pJson = env->GetStringUTFChars(json, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    threshold_key_set_metadata(pThreshold, const_cast<char *>(pKey), const_cast<char *>(pJson),
                               const_cast<char *>(pCurve),
                               error_ptr);
    if (private_key != nullptr) {
        env->ReleaseStringUTFChars(private_key, pKey);
    }
    env->ReleaseStringUTFChars(json, pJson);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyStorageLayerSetMetadataStream(
        JNIEnv *env, jobject jthis, jstring private_keys, jstring json, jstring curveN,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pKeys = env->GetStringUTFChars(private_keys, JNI_FALSE);
    const char *pJson = env->GetStringUTFChars(json, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    threshold_key_set_metadata_stream(pThreshold, const_cast<char *>(pKeys),
                                      const_cast<char *>(pJson),
                                      const_cast<char *>(pCurve),
                                      error_ptr);
    env->ReleaseStringUTFChars(private_keys, pKeys);
    env->ReleaseStringUTFChars(json, pJson);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyGetAllShareStoresForLatestPolynomial(
        JNIEnv *env, jobject jthis, jstring curveN, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    auto *pResult = threshold_key_get_all_share_stores_for_latest_polynomial(pThreshold,
                                                                             const_cast<char *>(pCurve),
                                                                             error_ptr);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyReconstructLatestPolynomial(
        JNIEnv *env, jobject jthis, jstring curveN, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    auto *pResult = threshold_key_reconstruct_latest_poly(pThreshold, const_cast<char *>(pCurve),
                                                          error_ptr);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ThresholdKey_jniThresholdKeyServiceProviderAssignPublicKey(
        JNIEnv *env, jobject jthis, jstring tss_tag, jstring tss_nonce, jstring tss_public_key,
        jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pThreshold = reinterpret_cast<FFIThresholdKey *>(pObject);
    const char *pTag = env->GetStringUTFChars(tss_tag, JNI_FALSE);
    const char *pNonce = env->GetStringUTFChars(tss_nonce, JNI_FALSE);
    const char *pKey = env->GetStringUTFChars(tss_public_key, JNI_FALSE);
    threshold_key_service_provider_assign_tss_public_key(pThreshold, const_cast<char *>(pTag), const_cast<char *>(pNonce), const_cast<char *>(pKey),
                                                          error_ptr);
    env->ReleaseStringUTFChars(tss_tag, pTag);
    env->ReleaseStringUTFChars(tss_nonce, pNonce);
    env->ReleaseStringUTFChars(tss_public_key, pKey);
    setErrorCode(env, error, errorCode);
}