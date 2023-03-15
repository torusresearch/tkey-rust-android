package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

public final class LocalMetadataTransitions {
    private native void jniLocalMetadataTransitionsFree();

    private native long jniLocalMetadataTranstionsFromJson(String json, RuntimeError error);

    private native String jniLocalMetadataTranstionsToJson(RuntimeError error);

    final long pointer;

    public LocalMetadataTransitions(long ptr) {
        pointer = ptr;
    }

    public LocalMetadataTransitions(String json) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniLocalMetadataTranstionsFromJson(json, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    public String export() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniLocalMetadataTranstionsToJson(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniLocalMetadataTransitionsFree();
    }
}
