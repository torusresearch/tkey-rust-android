package com.web3auth.tkey_android_distribution.ThresholdKey;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class GenerateShareStoreResult {
    private native String jniGenerateShareStoreResultGetShareIndex(RuntimeError error);

    private native long jniGenerateShareStoreResultGetShareStoreMap(RuntimeError error);

    private native void jniGenerateShareStoreResultFree();

    private final long pointer;

    public GenerateShareStoreResult(long ptr) {
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    public String getIndex() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String share_index = jniGenerateShareStoreResultGetShareIndex(error);
        if (error.code != 0) {
            throw error;
        }
        return share_index;
    }

    public ShareStoreMap getShareStoreMap() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long store_map = jniGenerateShareStoreResultGetShareStoreMap(error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStoreMap(store_map);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniGenerateShareStoreResultFree();
    }
}
