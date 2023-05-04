package com.web3auth.tkey.ThresholdKey.Common;

import com.web3auth.tkey.RuntimeError;

public final class KeyPoint {
    final long pointer;

    private native long jniKeyPointNew(String x, String y, RuntimeError error);

    private native String jniKeyPointEncode(String format, RuntimeError error);

    private native String jniKeyPointGetX(RuntimeError error);

    private native String jniKeyPointGetY(RuntimeError error);

    private native void jniKeyPointFree();

    public KeyPoint(long ptr) {
        this.pointer = ptr;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof KeyPoint)) {
            return false;
        }

        KeyPoint c = (KeyPoint) o;

        try {
            return getX().compareTo(c.getX()) == 0
                    && getY().compareTo(c.getY()) == 0;
        } catch (RuntimeError e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPoint(String x, String y) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniKeyPointNew(x, y, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = result;
    }

    public String getX() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointGetX(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String getY() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointGetY(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String getAsCompressedPublicKey(String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointEncode(format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniKeyPointFree();
    }
}
