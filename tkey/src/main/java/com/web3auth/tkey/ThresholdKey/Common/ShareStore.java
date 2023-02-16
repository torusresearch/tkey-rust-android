package com.web3auth.tkey.ThresholdKey.Common;

import com.web3auth.tkey.RuntimeError;

public final class ShareStore {
    private native long jniSharestoreFromJson(String json, RuntimeError error);

    private native String jniSharestoreToJson(RuntimeError error);

    private native String jniShareStoreGetShare(RuntimeError error);

    private native String jniShareStoreGetShareIndex(RuntimeError error);

    private native String jniShareStoreGetPolynomialId(RuntimeError error);

    private native void jniShareStoreFree();

    final long pointer;

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
