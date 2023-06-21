package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

public final class LocalMetadataTransitions {
    private native void jniLocalMetadataTransitionsFree();

    private native long jniLocalMetadataTranstionsFromJson(String json, RuntimeError error);

    private native String jniLocalMetadataTranstionsToJson(RuntimeError error);

    final long pointer;

    /**
     * Instantiate a LocalMetadataTransitions object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public LocalMetadataTransitions(long ptr) {
        pointer = ptr;
    }

    /**
     * Instantiate a LocalMetadataTransitions object using its' json representation.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @param json The json string.
     */
    public LocalMetadataTransitions(String json) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniLocalMetadataTranstionsFromJson(json, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    /**
     * Serialize to json
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String export() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniLocalMetadataTranstionsToJson(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniLocalMetadataTransitionsFree();
    }
}
