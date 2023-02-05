#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_Version_jniVersion(JNIEnv *env, jclass clazz,
                                                                 jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    char *pResult = get_version(error_ptr);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pResult);
    string_free(pResult);
    return result;
}