package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;

public final class ShareStoreArray {
    private native void jniShareStoreArrayFree();

    private native long jniShareStoreArrayGetAt(int index, RuntimeError error);

    private native int jniShareStoreArrayLen(RuntimeError error);

    final long pointer;

    /**
     * Instantiates a ShareStoreArray object.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public ShareStoreArray(long ptr) {
        pointer = ptr;
    }

    /**
     * Returns a ShareStore in the collection at the specified index.
     * @param index Index to retrieve the item from.
     * @throws RuntimeError Indicates underlying pointer or index is invalid.
     * @return ShareStore
     * @see ShareStore
     */
    public ShareStore getAt(int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniShareStoreArrayGetAt(index, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(ptr);
    }

    /**
     * Returns the length of the collection.
     * @throws RuntimeError Indicates underlying pointer or index is invalid.
     * @return int
     */
    public int length() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniShareStoreArrayLen(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareStoreArrayFree();
    }
}
