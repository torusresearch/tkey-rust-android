package com.web3auth.tkey.ThresholdKey;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.NodeDetails;

public final class ServiceProvider {
    private native void jniServiceProviderFree();
    private native long jniServiceProvider(boolean enableLogging, String postboxKey, String curveN, boolean useTss, @Nullable String verifierName, @Nullable String verifierId, @Nullable NodeDetails sss, @Nullable NodeDetails tss, @Nullable NodeDetails rss, RuntimeError error);

    final static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    final long pointer;

    /**
     * Instantiates a ServiceProvider object.
     * @param enableLogging Determines whether logging is enabled or not.
     * @param postboxKey The private key to be used for the ServiceProvider.
     * @param useTss Whether to use TSS or not.
     * @param verifierName Verifier Name, required for TSS.
     * @param verifierId Verifier ID, required for TSS.
     * @param sss Node information for the SSS service, required for TSS.
     * @param tss Node information for the TSS service, required for TSS.
     * @param rss Node information for the RSS Service, required for TSS.
     * @throws RuntimeError Indicates invalid parameters were used.
     * @see NodeDetails
     */
    public ServiceProvider(boolean enableLogging, String postboxKey, boolean useTss, @Nullable String verifierName, @Nullable String verifierId, @Nullable NodeDetails sss, @Nullable NodeDetails tss, @Nullable NodeDetails rss) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniServiceProvider(enableLogging, postboxKey, curveN, useTss, verifierName, verifierId, sss, tss, rss, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniServiceProviderFree();
    }
}
