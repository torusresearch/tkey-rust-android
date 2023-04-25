package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

import org.json.JSONException;

public final class Polynomial {
    public String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    private native void jniPolynomialFree();

    private native long jniGetPublicPolynomial(RuntimeError error);

    private native long jniGenerateShares(String indexes, String curveN, RuntimeError error);

    final long pointer;

    public Polynomial(long ptr) {
        pointer = ptr;
    }

    public PublicPolynomial getPublicPolynomial() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniGetPublicPolynomial(error);
        if (error.code != 0) {
            throw error;
        }
        return new PublicPolynomial(ptr);
    }

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
