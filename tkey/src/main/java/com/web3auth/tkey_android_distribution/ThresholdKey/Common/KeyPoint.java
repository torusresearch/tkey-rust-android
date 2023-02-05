package com.web3auth.tkey_android_distribution.ThresholdKey.Common;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class KeyPoint {
    private final long pointer;

    private static native String jniKeyPointEncode(String format, RuntimeError error);

    private static native String jniKeyPointGetX(RuntimeError error);

    private static native String jniKeyPointGetY(RuntimeError error);

    private static native void jniKeyPointFree();

    public KeyPoint(long ptr) {
        this.pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    public String getX() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointGetX(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String getY() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointGetY(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String getAsCompressedPublicKey(String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointEncode(format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniKeyPointFree();
    }
}
