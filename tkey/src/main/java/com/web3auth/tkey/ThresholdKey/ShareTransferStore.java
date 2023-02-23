package com.web3auth.tkey.ThresholdKey;

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
