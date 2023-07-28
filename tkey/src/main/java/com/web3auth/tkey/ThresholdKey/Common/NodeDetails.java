package com.web3auth.tkey.ThresholdKey.Common;

import com.web3auth.tkey.RuntimeError;

public final class NodeDetails {
    private long pointer;
    private native long jniNodeDetails(String serverEndpoints, String serverPublicKeys, int serverThreshold, RuntimeError error);
    private native void jniNodeDetailsFree();

    public NodeDetails(String serverEndpoints, String serverPublicKeys, int serverThreshold) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniNodeDetails(serverEndpoints, serverPublicKeys, serverThreshold, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniNodeDetailsFree();
    }
}
