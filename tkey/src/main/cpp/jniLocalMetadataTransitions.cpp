#include <jni.h>
#include "include/tkey.h"
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_ThresholdKey_LocalMetadataTransitions_jniLocalMetadataTransitionsFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    auto *pTransitions = reinterpret_cast<LocalMetadataTransitions *>(pObject);
    local_metadata_transitions_free(pTransitions);
}