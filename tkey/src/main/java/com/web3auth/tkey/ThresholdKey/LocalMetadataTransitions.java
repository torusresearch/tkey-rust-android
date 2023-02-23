package com.web3auth.tkey.ThresholdKey;

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
