#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareMap_jniShareMapFree(JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pMap = reinterpret_cast<ShareMap *>(pObject);
    share_map_free(pMap);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareMap_jniShareMapGetKeys(JNIEnv *env, jobject jthis,
                                                                jthrowable error) {
    // TODO: implement jniShareMapGetKeys()
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_web3auth_tkey_ThresholdKey_ShareMap_jniShareMapGetShareByKey(JNIEnv *env, jobject jthis,
                                                                      jstring key,
                                                                      jthrowable error) {
    // TODO: implement jniShareMapGetShareByKey()
}