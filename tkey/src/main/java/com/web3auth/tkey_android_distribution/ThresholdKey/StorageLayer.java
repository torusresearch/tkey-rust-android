package com.web3auth.tkey_android_distribution.ThresholdKey;

import com.web3auth.tkey_android_distribution.RuntimeError;

public final class StorageLayer {
    private static native long jniStorageLayer(boolean enableLogging, String hostUrl, int serverTimeOffset, String networkInterfaceMethodName, String networkInterfaceMethodSignature, RuntimeError error);

    private static native void jniStorageLayerFree();

    private final long pointer;

    private String networkInterface(String url, String data, RuntimeError error) {
        // TODO: Implement network calls
        return "";
    }

    public StorageLayer(long ptr) {
        this.pointer = ptr;
    }

    public StorageLayer(boolean enableLogging, String hostUrl, int serverTimeOffset) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniStorageLayer(enableLogging, hostUrl, serverTimeOffset, "networkInterface", "(Ljava/lang/String;Ljava/lang/String;Lcom.web3auth.tkey_android_distribution.RuntimeError;)Ljava/lang/String;", error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniStorageLayerFree();
    }
}
