package com.web3auth.tkey_android_distribution.ThresholdKey.Common;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class PrivateKey {

    private static native String jniGeneratePrivateKey(String curve, RuntimeError error);

    public String hex;
    final static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    public PrivateKey(String key) {
        hex = key;
    }

    public static PrivateKey generate() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniGeneratePrivateKey(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new PrivateKey(result);
    }


}
