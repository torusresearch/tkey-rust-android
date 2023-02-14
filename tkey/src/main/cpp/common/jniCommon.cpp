#include <jni.h>
#include <android/log.h>
#include <string>
#include <cmath>
#include <android/log.h>

#define LOG_TAG "TKey"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,    LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,     LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,    LOG_TAG, __VA_ARGS__)

inline jmethodID
getMethodId(JNIEnv *jniEnv, jobject jThis, jstring methodName, jstring methodSignature) {
    jclass jClass = jniEnv->GetObjectClass(jThis);
    const char *method = jniEnv->GetStringUTFChars(methodName, JNI_FALSE);
    const char *signature = jniEnv->GetStringUTFChars(methodSignature, JNI_FALSE);
    jmethodID methodId = jniEnv->GetMethodID(jClass, method, signature);
    jniEnv->ReleaseStringUTFChars(methodSignature, signature);
    jniEnv->ReleaseStringUTFChars(methodName, method);
    return methodId;
}

inline jlong GetPointerField(JNIEnv *jEnv, jobject jThis) {
    jclass cls = jEnv->GetObjectClass(jThis);
    jfieldID fid = jEnv->GetFieldID(cls, "pointer", "J");
    jlong lObject = jEnv->GetLongField(jThis, fid);
    return lObject;
}

inline void SetPointerField(JNIEnv *jEnv, jobject jThis, jlong jPointer) {
    jclass cls = jEnv->GetObjectClass(jThis);
    jfieldID fid = jEnv->GetFieldID(cls, "pointer", "J");
    jEnv->SetLongField(jThis, fid, jPointer);
}

inline jboolean setErrorCode(JNIEnv *jEnv, jobject error, jint value) {
    jclass errorClass = jEnv->GetObjectClass(error);
    if (errorClass == nullptr)
        return static_cast<jboolean>(false);
    jfieldID errorField = jEnv->GetFieldID(errorClass, "code", "I");
    if (errorField == nullptr)
        return static_cast<jboolean>(false);
    jEnv->SetIntField(error, errorField, value);
    return static_cast<jboolean>(true);
}