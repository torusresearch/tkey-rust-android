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

    public KeyPointArray() {
        pointer = jniKeyPointArrayNew();
    }

    public KeyPointArray(long ptr) {
        pointer = ptr;
    }

    public void removeAt(int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniKeyPointArrayRemoveAt(index,error);
        if (error.code != 0) {
            throw error;
        }
    }

    public void insert(KeyPoint point) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniKeyPointArrayInsert(point,error);
        if (error.code != 0) {
            throw error;
        }
    }

    public void updateAt(KeyPoint point, int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniKeyPointArrayUpdateAt(point,index,error);
        if (error.code != 0) {
            throw error;
        }
    }

    public KeyPoint getAt(int index) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniKeyPointArrayGetAt(index,error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyPoint(ptr);
    }

    public int length() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniKeyPointArrayLen(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public Polynomial lagrange() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";
        long ptr = jniKeyPointArrayLagrange(curveN,error);
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
