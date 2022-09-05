package com.web3auth.tkey_android;

public class FFIHash {
    private static native String jniHash(String jInput);

    public static String sha256_hash(String jInput) {
        return jniHash(jInput);
    }
}
