package com.web3auth.tkey_android_distribution.ThresholdKey;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class LocalMetadataTransitions {
    private native void jniLocalMetadataTransitionsFree();

    private final long pointer;

    public LocalMetadataTransitions(long ptr) {
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniLocalMetadataTransitionsFree();
    }
}
