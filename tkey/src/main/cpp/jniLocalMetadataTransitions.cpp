#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_LocalMetadataTransitions_jniLocalMetadataTransitionsFree(
        JNIEnv *env, jobject jthis) {
    jlong pObject = GetPointerField(env, jthis);
    LocalMetadataTransitions *pTransitions = reinterpret_cast<LocalMetadataTransitions *>(pObject);
    local_metadata_transitions_free(pTransitions);
}