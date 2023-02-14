package com.web3auth.tkey_android_distribution.ThresholdKey;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class Metadata {
    private native void jniMetadataFree();

    private native long jniMetadataFromJson(String json, RuntimeError error);

    private native String jniMetadataToJson(RuntimeError error);

    private final long pointer;

    public Metadata(long ptr) {
        pointer = ptr;
    }

    public Metadata(String json) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniMetadataFromJson(json, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    public String export() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniMetadataToJson(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniMetadataFree();
    }
}
