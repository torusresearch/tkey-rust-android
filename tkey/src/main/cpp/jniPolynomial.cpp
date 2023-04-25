#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_Polynomial_jniPolynomialFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pPolynomial = reinterpret_cast<Polynomial *>(pObject);
    polynomial_free(pPolynomial);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Polynomial_jniGetPublicPolynomial(JNIEnv *env, jobject jthis,
                                                                      jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pPolynomial = reinterpret_cast<Polynomial *>(pObject);
    auto *pResult = polynomial_get_public_polynomial(pPolynomial, error_ptr);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);

}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_Polynomial_jniGenerateShares(JNIEnv *env, jobject jthis,
                                                                 jstring indexes, jstring curveN,
                                                                 jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pPolynomial = reinterpret_cast<Polynomial *>(pObject);
    const char *pIndexes = env->GetStringUTFChars(indexes, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    auto *pResult = polynomial_generate_shares(pPolynomial, const_cast<char *>(pIndexes),
                                               const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(indexes, pIndexes);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}