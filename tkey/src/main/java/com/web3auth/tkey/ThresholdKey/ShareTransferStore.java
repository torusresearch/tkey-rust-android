package com.web3auth.tkey.ThresholdKey;

public final class ShareTransferStore {
    private native void jniShareTransferStoreFree();

    final long pointer;

    /**
     * Instantiates a ShareStoreMap object.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public ShareTransferStore(long ptr) {
        this.pointer = ptr;
    }

    // todo: serialize and deserialize from json

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareTransferStoreFree();
    }
}
