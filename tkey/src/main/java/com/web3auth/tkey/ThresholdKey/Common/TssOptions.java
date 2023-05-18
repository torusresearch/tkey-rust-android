package com.web3auth.tkey.ThresholdKey.Common;
import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;

public final class TssOptions {
    private native long jniTssOptions(String inputTssShare, int inputTssIndex, String authSigntatures, @Nullable KeyPoint factorPub, @Nullable String selectedServers, @Nullable int newTssIndex, @Nullable KeyPoint newFactorPub, RuntimeError error);
    private native void jniTssOptionsFree();

    private long pointer;

    public TssOptions(String inputTssShare, int inputTssIndex, String authSignatures, KeyPoint factorPub, @Nullable String selectedServers, @Nullable int newTssIndex, @Nullable KeyPoint newFactorPub) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniTssOptions(inputTssShare, inputTssIndex, authSignatures, factorPub, selectedServers, newTssIndex, newFactorPub, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniTssOptionsFree();
    }
}
