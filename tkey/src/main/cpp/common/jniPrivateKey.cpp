#include <jni.h>
#include "tkey.h"
#include "jniCommon.cpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_Common_PrivateKey_jniGeneratePrivateKey(
        JNIEnv *env, jobject jthis, jstring curve, jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    const char *pCurve = env->GetStringUTFChars(curve, JNI_FALSE);
    char *pPrivateKey = generate_private_key(const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(curve, pCurve);
    setErrorCode(env, error, errorCode);
    jstring result = env->NewStringUTF(pPrivateKey);
    string_free(pPrivateKey);
    return result;
}