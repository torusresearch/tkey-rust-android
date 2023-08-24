package com.web3auth.tkey.ThresholdKey.Common;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;

public final class PrivateKey {

    private static native String jniGeneratePrivateKey(String curve, RuntimeError error);
    
    private static native String jniPrivateToPublic(String secret, RuntimeError error);

    public String hex;
    final static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    /**
     * Instantiates a PrivateKey object from its' serialized format.
     * @param key hexadecimal representation as String
     */
    public PrivateKey(String key) {
        hex = key;
    }

    /**
     * Instantiates a PrivateKey object by random generation.
     * @throws RuntimeError Only possible if curveN is passed externally.
     * @return PrivateKey
     */
    public static PrivateKey generate() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniGeneratePrivateKey(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new PrivateKey(result);
    }

    public String toPublic(@Nullable KeyPoint.PublicKeyEncoding encoding) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniPrivateToPublic(this.hex, error);
        if (error.code != 0) {
            throw error;
        }
        String publicKey = new KeyPoint(result).getPublicKey(encoding);
        return publicKey;
    }
}
