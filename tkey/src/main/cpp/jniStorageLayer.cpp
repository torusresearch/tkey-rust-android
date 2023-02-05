#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

jmethodID getMethodId(JNIEnv *jniEnv, jobject jThis, jstring methodName, jstring methodSignature) {
    jclass jClass = jniEnv->GetObjectClass(jThis);
    const char *method = jniEnv->GetStringUTFChars(methodName, JNI_FALSE);
    const char *signature = jniEnv->GetStringUTFChars(methodSignature, JNI_FALSE);
    jmethodID methodId = jniEnv->GetMethodID(jClass, method, signature);
    jniEnv->ReleaseStringUTFChars(methodSignature, signature);
    jniEnv->ReleaseStringUTFChars(methodName, method);
    return methodId;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_StorageLayer_jniStorageLayerFree(
        JNIEnv *env, jobject jthis) {
    long pObject = GetPointerField(env, jthis);
    FFIStorageLayer *pStorage = reinterpret_cast<FFIStorageLayer *>(pObject);
    storage_layer_free(pStorage);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_StorageLayer_jniStorageLayer(
        JNIEnv *env, jclass clazz, jboolean enable_logging,
        jstring host_url, jint server_time_offset,
        jstring network_interface_method_name, jstring network_interface_method_signature,
        jthrowable error) {
    // TODO: implement jniStorageLayer()
}