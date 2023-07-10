package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

public final class Metadata {
    private native void jniMetadataFree();

    private native long jniMetadataFromJson(String json, RuntimeError error);

    private native String jniMetadataToJson(RuntimeError error);

    final long pointer;

    /**
     * Instantiate a Metadata object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public Metadata(long ptr) {
        pointer = ptr;
    }

    /**
     * Instantiate a Metadata object using its' json representation.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @param json The json string.
     */
    public Metadata(String json) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniMetadataFromJson(json, error);
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
        String result = jniMetadataToJson(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniMetadataFree();
    }
}
