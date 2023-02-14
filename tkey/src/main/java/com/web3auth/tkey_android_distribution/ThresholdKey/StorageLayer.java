package com.web3auth.tkey_android_distribution.ThresholdKey;

import com.web3auth.tkey_android_distribution.RuntimeError;

import java.lang.reflect.Method;

public final class StorageLayer {
    private native long jniStorageLayer(boolean enableLogging, String hostUrl, int serverTimeOffset, String networkInterfaceMethodName, String networkInterfaceMethodSignature, RuntimeError error);

    private native void jniStorageLayerFree();

    private final long pointer;

    public String networkInterface(String url, String data, RuntimeError error) {
        // TODO: Implement network calls
        error.code = -1;
        return "";
    }

    public StorageLayer(long ptr) {
        pointer = ptr;
    }

    public StorageLayer(boolean enableLogging, String hostUrl, int serverTimeOffset) throws RuntimeError, NoSuchMethodException {
        RuntimeError error = new RuntimeError();
        Method methodInfo = this.getClass().getMethod("networkInterface", String.class, String.class, RuntimeError.class);
        long ptr = jniStorageLayer(enableLogging, hostUrl, serverTimeOffset, methodInfo.getName(), "(Ljava/lang/String;Ljava/lang/String;Lcom.web3auth.tkey_android_distribution.RuntimeError;)Ljava/lang/String;", error);
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
