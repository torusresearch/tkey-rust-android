package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

public final class GenerateShareStoreResult {
    private native String jniGenerateShareStoreResultGetShareIndex(RuntimeError error);

    private native long jniGenerateShareStoreResultGetShareStoreMap(RuntimeError error);

    private native void jniGenerateShareStoreResultFree();

    final long pointer;

    /**
     * Instantiate a GenerateShareStoreResult object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public GenerateShareStoreResult(long ptr) {
        pointer = ptr;
    }

    /**
     * Returns the share index in the GenerateShareStoreResult object.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String getIndex() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String share_index = jniGenerateShareStoreResultGetShareIndex(error);
        if (error.code != 0) {
            throw error;
        }
        return share_index;
    }

    /**
     * Returns the share store map in the GenerateShareStoreResult object.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return ShareStoreMap
     * @see ShareStoreMap
     */
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
