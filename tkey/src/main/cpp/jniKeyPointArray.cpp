#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayNew(JNIEnv *env, jobject thiz) {
    // TODO: implement jniKeyPointArrayNew()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayFree(JNIEnv *env, jobject thiz) {
    // TODO: implement jniKeyPointArrayFree()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayRemoveAt(JNIEnv *env,
                                                                           jobject thiz, jint index,
                                                                           jthrowable error) {
    // TODO: implement jniKeyPointArrayRemoveAt()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayInsert(JNIEnv *env, jobject thiz,
                                                                         jobject point,
                                                                         jthrowable error) {
    // TODO: implement jniKeyPointArrayInsert()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayUpdateAt(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jobject point,
                                                                           jint index,
                                                                           jthrowable error) {
    // TODO: implement jniKeyPointArrayUpdateAt()
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayGetAt(JNIEnv *env, jobject thiz,
                                                                        jint index,
                                                                        jthrowable error) {
    // TODO: implement jniKeyPointArrayGetAt()
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayLen(JNIEnv *env, jobject thiz,
                                                                      jthrowable error) {
    // TODO: implement jniKeyPointArrayLen()
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_web3auth_tkey_ThresholdKey_KeyPointArray_jniKeyPointArrayLagrange(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jstring curve_n,
                                                                           jthrowable error) {
    // TODO: implement jniKeyPointArrayLagrange()
}