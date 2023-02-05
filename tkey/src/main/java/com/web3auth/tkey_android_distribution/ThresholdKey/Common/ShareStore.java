package com.web3auth.tkey_android_distribution.ThresholdKey.Common;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class ShareStore {
    private static native long jniSharestoreFromJson(String json, RuntimeError error);

    private static native String jniSharestoreToJson(RuntimeError error);

    private static native String jniShareStoreGetShare(RuntimeError error);

    private static native String jniShareStoreGetShareIndex(RuntimeError error);

    private static native String jniShareStoreGetPolynomialId(RuntimeError error);

    private static native void jniShareStoreFree();

    private final long pointer;

    public ShareStore(long ptr) {
        pointer = ptr;
    }

    public ShareStore(String json) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniSharestoreFromJson(json, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    public String toJsonString() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharestoreToJson(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String share() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareStoreGetShare(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String shareIndex() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareStoreGetShareIndex(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String polynomialId() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareStoreGetPolynomialId(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareStoreFree();
    }
}
