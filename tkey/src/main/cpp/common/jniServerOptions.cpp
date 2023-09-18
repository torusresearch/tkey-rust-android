#include <jni.h>
#include "include/tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_ServerOptions_jniServerOptionsFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pointer = reinterpret_cast<ServerOpts *>(pObject);
    server_opts_free(pointer);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Common_ServerOptions_jniServerOptions(JNIEnv *env, __attribute__((unused)) jobject jthis,
                                                                    jstring server_endpoints,
                                                                    jstring server_pub_keys,
                                                                    jint server_threshold,
                                                                    jstring auth_signatures,
                                                                    jstring selected_servers,
                                                                    jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pServerEndpoints = env->GetStringUTFChars(server_endpoints, JNI_FALSE);
    const char *pServerPubKeys = env->GetStringUTFChars(server_pub_keys, JNI_FALSE);
    const char *pSelectedServers = nullptr;
    if(selected_servers != nullptr) {
        pSelectedServers = env->GetStringUTFChars(selected_servers, JNI_FALSE);
    }
    const char *pAuthSignatures = env->GetStringUTFChars(auth_signatures, JNI_FALSE);
    int* pServerThreshold = &server_threshold;

    auto *pResult = server_opts(
            const_cast<char *>(pServerEndpoints),
            const_cast<char *>(pServerPubKeys),
            *pServerThreshold,
            const_cast<char *>(pSelectedServers),
            const_cast<char *>(pAuthSignatures),
            error_ptr);

    if(selected_servers != nullptr) {
        env->ReleaseStringUTFChars(selected_servers, pSelectedServers);
    }
    env->ReleaseStringUTFChars(server_endpoints, pServerEndpoints);
    env->ReleaseStringUTFChars(server_pub_keys, pServerPubKeys);
    env->ReleaseStringUTFChars(auth_signatures, pAuthSignatures);
    setErrorCode(env, error, errorCode);

    return reinterpret_cast<jlong>(pResult);
}
