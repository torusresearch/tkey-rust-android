package com.web3auth.tkey_android_distribution.ThresholdKey;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class LocalMetadataTransitions {
    private native void jniLocalMetadataTransitionsFree();

    final long pointer;

    public LocalMetadataTransitions(long ptr) {
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniLocalMetadataTransitionsFree();
    }
}
