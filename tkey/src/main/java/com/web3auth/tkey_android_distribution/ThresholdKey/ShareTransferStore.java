package com.web3auth.tkey_android_distribution.ThresholdKey;

public final class ShareTransferStore {
    private native void jniShareTransferStoreFree();

    final long pointer;

    public ShareTransferStore(long ptr) {
        this.pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareTransferStoreFree();
    }
}
