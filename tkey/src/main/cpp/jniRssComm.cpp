#include "include/tkey.h"
#include "common/jniCommon.cpp"
#include <jni.h>


JavaVM *g_vm;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *) {
    g_vm = vm;
    return JNI_VERSION_1_6;
}

char *network_callback(char *url, char *data, void *obj_ref, int *error) {
    JNIEnv *jniEnv;
    if (g_vm->GetEnv((void **) &jniEnv, JNI_VERSION_1_6) != JNI_OK || obj_ref == nullptr) {
        return nullptr;
    }

    jstring jurl = jniEnv->NewStringUTF(url);
    string_free(url);
    jstring jdata = jniEnv->NewStringUTF(data);
    string_free(data);
    jclass error_class = jniEnv->FindClass("com/web3auth/tkey/RuntimeError");
    jmethodID constructor = jniEnv->GetMethodID(error_class, "<init>", "()V");
    jobject jerror = jniEnv->NewObject(error_class, constructor);
    jobject jparent_ref = reinterpret_cast<jobject>(obj_ref);
    auto networkInterface = reinterpret_cast<jmethodID>(GetPointerField(jniEnv,
                                                                        jparent_ref,
                                                                        "callback_method_id"));
    auto result = (jstring) jniEnv->CallObjectMethod(
            jparent_ref,
            networkInterface,
            jurl, jdata, jerror);
    jclass cls = jniEnv->GetObjectClass(jerror);
    jfieldID fid = jniEnv->GetFieldID(cls, "code", "I");
    jint error_field = jniEnv->GetIntField(jerror, fid);
    *error = error_field;

    char *res = const_cast<char *>(jniEnv->GetStringUTFChars(result, JNI_FALSE));
    jniEnv->DeleteLocalRef(result);
    return res;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_RssComm_jniRssCommFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pComm = reinterpret_cast<FFIRssComm *>(pObject);
    auto instance = rss_comm_free(pComm);
    if (instance != nullptr) {
        auto obj_ref = reinterpret_cast<jobject>(instance);
        env->DeleteGlobalRef(obj_ref);
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_RssComm_jniRssComm(JNIEnv *env, jobject jthis,
                                                       jstring network_interface_method_name,
                                                       jstring network_interface_method_signature,
                                                       jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jobject callbackHandler = env->NewGlobalRef(jthis);
    jmethodID networkInterface = getMethodId(env, jthis, network_interface_method_name,
                                             network_interface_method_signature);
    SetPointerField(env, jthis, reinterpret_cast<jlong>(networkInterface), "callback_method_id");
    auto *comm = rss_comm(network_callback, callbackHandler, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(comm);
}