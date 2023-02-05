#include <jni.h>
#include <tkey.h>
#include "common/jniCommon.cpp"

extern "C"
JNIEXPORT void JNICALL
Java_com_web3auth_tkey_1android_1distribution_ThresholdKey_ShareTransferStore_jniShareTransferStoreFree(
        JNIEnv *env, jobject jthis) {
    long pObject = GetPointerField(env, jthis);
    ShareTransferStore *pStore = reinterpret_cast<ShareTransferStore *>(pObject);
    share_transfer_store_free(pStore);
}