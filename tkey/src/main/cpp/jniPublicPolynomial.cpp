#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_PublicPolynomial_jniPublicPolynomialFree(JNIEnv *env,
                                                                             jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pPublicPolynomial = reinterpret_cast<PublicPolynomial *>(pObject);
    public_polynomial_free(pPublicPolynomial);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_ThresholdKey_PublicPolynomial_jniGetThreshold(JNIEnv *env, jobject jthis,
                                                                     jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pPublicPolynomial = reinterpret_cast<PublicPolynomial *>(pObject);
    unsigned int result = public_polynomial_get_threshold(pPublicPolynomial, error_ptr);
    setErrorCode(env, error, errorCode);
    return result;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_PublicPolynomial_jniPolyCommitmentEval(JNIEnv *env,
                                                                           jobject jthis,
                                                                           jstring index,
                                                                           jstring curveN,
                                                                           jthrowable error) {
    int errorCode = 0;
    int *error_ptr = &errorCode;
    jlong pObject = GetPointerField(env, jthis);
    auto *pPolynomial = reinterpret_cast<PublicPolynomial *>(pObject);
    const char *pIndexe = env->GetStringUTFChars(index, JNI_FALSE);
    const char *pCurve = env->GetStringUTFChars(curveN, JNI_FALSE);
    auto *pResult = public_polynomial_poly_commitment_eval(pPolynomial, const_cast<char *>(pIndexe),
                                                           const_cast<char *>(pCurve), error_ptr);
    env->ReleaseStringUTFChars(index, pIndexe);
    env->ReleaseStringUTFChars(curveN, pCurve);
    setErrorCode(env, error, errorCode);
    return reinterpret_cast<jlong>(pResult);
}