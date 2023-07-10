package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.KeyPoint;

public final class KeyPointArray {
    private native long jniKeyPointArrayNew();

    private native void jniKeyPointArrayFree();

    private native void jniKeyPointArrayRemoveAt(int index, RuntimeError error);

    private native void jniKeyPointArrayInsert(KeyPoint point, RuntimeError error);

    private native void jniKeyPointArrayUpdateAt(KeyPoint point, int index, RuntimeError error);

    private native long jniKeyPointArrayGetAt(int index, RuntimeError error);

    private native int jniKeyPointArrayLen(RuntimeError error);

    private native long jniKeyPointArrayLagrange(String curveN, RuntimeError error);

    final long pointer;

    /**
     * Instantiate a new KeyDetails object.
     */
    public KeyPointArray() {
        pointer = jniKeyPointArrayNew();
    }

    /**
     * Instantiate a KeyDetails object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public KeyPointArray(long ptr) {
        pointer = ptr;
    }

    /**
     * Removes a KeyPoint from the collection at the specified index.
     * @param index The index of the item to be removed.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @see KeyPoint
     */
    public void removeAt(int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniKeyPointArrayRemoveAt(index, error);
        if (error.code != 0) {
            throw error;
        }
    }

    /**
     * Inserts a KeyPoint at the end of the collection.
     * @param point The Keypoint to be inserted
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @see KeyPoint
     */
    public void insert(KeyPoint point) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniKeyPointArrayInsert(point, error);
        if (error.code != 0) {
            throw error;
        }
    }

    /**
     * Replaces a KeyPoint in the collection at the specified index.
     * @param point The replacement KeyPoint.
     * @param index Index to update the item at.
     * @throws RuntimeError Indicates underlying pointer or index is invalid.
     * @see KeyPoint
     */
    public void updateAt(KeyPoint point, int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniKeyPointArrayUpdateAt(point, index, error);
        if (error.code != 0) {
            throw error;
        }
    }

    /**
     * Returns a KeyPoint in the collection at the specified index.
     * @param index Index to retrieve the item from.
     * @throws RuntimeError Indicates underlying pointer or index is invalid.
     * @return KeyPoint
     * @see KeyPoint
     */
    public KeyPoint getAt(int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniKeyPointArrayGetAt(index, error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyPoint(ptr);
    }

    /**
     * Returns the length of the collection.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return KeyPoint
     * @see KeyPoint
     */
    public int length() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniKeyPointArrayLen(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Performs lagrange interpolation on items contained in the collection.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return Polynomial
     * @see Polynomial
     */
    public Polynomial lagrange() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";
        long ptr = jniKeyPointArrayLagrange(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new Polynomial(ptr);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniKeyPointArrayFree();
    }
}
