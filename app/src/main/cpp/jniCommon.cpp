#include <jni.h>
#include <android/log.h>
#include <string>
#include <cmath>
#include <android/log.h>

// Standard helper functions

#define LOG_TAG "TKey"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,    LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,     LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,    LOG_TAG, __VA_ARGS__)

// Note: Functions included in multiple source files must be declared inline

inline jlong GetPointerField(JNIEnv *jEnv, jobject jThis) {
    jclass cls = jEnv->GetObjectClass(jThis);
    jfieldID fid = jEnv->GetFieldID(cls, "pointer", "J");
    jlong lByteVector = jEnv->GetLongField(jThis, fid);
    return lByteVector;
}

inline void SetPointerField(JNIEnv *jEnv, jobject jThis, jlong jPointer) {
    jclass cls = jEnv->GetObjectClass(jThis);
    jfieldID fid = jEnv->GetFieldID(cls, "pointer", "J");
    jEnv->SetLongField(jThis, fid, jPointer);
}

inline jbyteArray getBytesFromUnsignedLongLong(JNIEnv *jEnv, unsigned long long value) {
    const size_t size = sizeof(unsigned long long int);
    jbyteArray result = jEnv->NewByteArray((jsize) size);
    if (result != nullptr) {
        jbyte *cBytes = jEnv->GetByteArrayElements(result, nullptr);
        if (cBytes != nullptr) {
            int i;
            for (i = (int) (size - 1); i >= 0; i--) {
                cBytes[i] = (jbyte) (value & 0xFF);
                value >>= 8;
            }
            jEnv->ReleaseByteArrayElements(result, cBytes, 0);
        }
    }
    return result;
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