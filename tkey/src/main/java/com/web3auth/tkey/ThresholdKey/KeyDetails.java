package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.KeyPoint;

public final class KeyDetails {
    private native void jniKeyDetailsFree();

    private native long jniKeyDetailsGetPublicKeyPoint(RuntimeError error);

    private native int jniKeyDetailsGetThreshold(RuntimeError error);

    private native int jniKeyDetailsGetRequiredShares(RuntimeError error);

    private native int jniKeyDetailsGetTotalShares(RuntimeError error);

    private native String jniKeyDetailsGetShareDescriptions(RuntimeError error);

    final long pointer;

    public KeyDetails(long ptr) {
        pointer = ptr;
    }

    public KeyPoint getPublicKeyPoint() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniKeyDetailsGetPublicKeyPoint(error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyPoint(result);
    }

    public int getThreshold() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniKeyDetailsGetThreshold(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public int getRequiredShares() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniKeyDetailsGetRequiredShares(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public int getTotalShares() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniKeyDetailsGetTotalShares(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String getShareDescriptions() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyDetailsGetShareDescriptions(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniKeyDetailsFree();
    }
}
