package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

import org.json.JSONException;

public final class Polynomial {
    public String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    private native void jniPolynomialFree();

    private native long jniGetPublicPolynomial(RuntimeError error);

    private native long jniGenerateShares(String indexes, String curveN, RuntimeError error);

    final long pointer;

    /**
     * Instantiate a Polynomial object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public Polynomial(long ptr) {
        pointer = ptr;
    }

    /**
     * Retrieves the public polynomial.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return PublicPolynomial
     * @see PublicPolynomial
     */
    public PublicPolynomial getPublicPolynomial() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniGetPublicPolynomial(error);
        if (error.code != 0) {
            throw error;
        }
        return new PublicPolynomial(ptr);
    }

    /**
     * Retrieves the shares for this polynomial.
     * @param indexes Share index to be used.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @throws JSONException Indicates the data is malformed.
     * @return ShareMap
     * @see ShareMap
     */
    public ShareMap generateShares(String indexes) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        long ptr = jniGenerateShares(indexes, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareMap(ptr);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniPolynomialFree();
    }
}
