package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.KeyPoint;

public final class PublicPolynomial {
    public String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    private native void jniPublicPolynomialFree();

    private native int jniGetThreshold(RuntimeError error);

    private native long jniPolyCommitmentEval(String index, String curveN, RuntimeError error);

    final long pointer;

    /**
     * Instantiate a PublicPolynomial object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public PublicPolynomial(long ptr) {
        pointer = ptr;
    }

    /**
     * Retrieves the threshold.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return int
     */
    public int getThreshold() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniGetThreshold(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Retrieves the keypoint for the respective share index.
     * @param index share index in hexadecimal format
     * @throws RuntimeError Indicates underlying pointer or index is invalid.
     * @return KeyPoint
     * @see KeyPoint
     */
    public KeyPoint polyCommitmentEval(String index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniPolyCommitmentEval(index, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyPoint(ptr);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniPublicPolynomialFree();
    }
}
