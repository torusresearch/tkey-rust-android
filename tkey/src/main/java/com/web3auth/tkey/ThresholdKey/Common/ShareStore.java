package com.web3auth.tkey.ThresholdKey.Common;

import com.web3auth.tkey.RuntimeError;

public final class ShareStore {
    private native long jniSharestoreFromJson(String json, RuntimeError error);

    private native String jniSharestoreToJson(RuntimeError error);

    private native String jniShareStoreGetShare(RuntimeError error);

    private native String jniShareStoreGetShareIndex(RuntimeError error);

    private native String jniShareStoreGetPolynomialId(RuntimeError error);

    private native void jniShareStoreFree();

    final long pointer;

    /**
     * Instantiate a ShareStore object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public ShareStore(long ptr) {
        pointer = ptr;
    }

    /**
     * Instantiate a ShareStore object using its' corresponding json.
     * @param json Json representation as String.
     * @throws RuntimeError Indicates json is invalid.
     */
    public ShareStore(String json) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniSharestoreFromJson(json, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    /**
     * Serialize a ShareStore object  object to its' corresponding json.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String toJsonString() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharestoreToJson(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Returns the share contained in the ShareStore object.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String share() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareStoreGetShare(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Returns the share index contained in the ShareStore object.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String shareIndex() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareStoreGetShareIndex(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Returns the polynomial ID contained in the ShareStore object.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String polynomialId() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareStoreGetPolynomialId(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareStoreFree();
    }
}
