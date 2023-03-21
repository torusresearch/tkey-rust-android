package com.web3auth.tkey;

public final class Version {
    private Version() {
        //Utility class
    }

    private static native String jniVersion(RuntimeError error);

    public static String current() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String version = jniVersion(error);
        if (error.code != 0) {
            throw error;
        }
        return version;
    }
}
