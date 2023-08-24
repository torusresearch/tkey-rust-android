package com.web3auth.tkey.ThresholdKey.Common;

import com.web3auth.tkey.RuntimeError;

public final class KeyPoint {
    final long pointer;

    private native long jniKeyPointNew(String x, String y, RuntimeError error);

    private native long jniKeyPointNewAddr(String address, RuntimeError error);

    private native String jniKeyPointEncode(String format, RuntimeError error);

    private native String jniKeyPointGetX(RuntimeError error);

    private native String jniKeyPointGetY(RuntimeError error);

    private native void jniKeyPointFree();

    public enum PublicKeyEncoding {
        EllipticCompress,
        FullAddress;

        public String getValue() {
            switch (this) {
                case EllipticCompress:
                    return "elliptic-compressed";
                case FullAddress:
                    return "";
                default:
                    return "";
            }
        }
    }
    /**
     * Instantiate a KeyPoint object using the underlying pointer.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public KeyPoint(long ptr) {
        this.pointer = ptr;
    }

    /**
    * Compares two KeyPoint objects
    * @param o Keypoint to compare against.
    * @throws RuntimeException Indicates invalid parameters was used.
    * @return boolean `true` if they are equal, `false` otherwise.
    */
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

    /**
     * Instantiate a KeyPoint object using X and Y co-ordinates in hexadecimal format.
     * @param x X value of co-ordinate pair.
     * @param y Y value of co-ordinate pair.
     * @throws RuntimeError Indicates invalid parameters was used.
     */
    public KeyPoint(String x, String y) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniKeyPointNew(x, y, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = result;
    }
    
    public KeyPoint(String address) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniKeyPointNewAddr(address, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = result;
    }

    /**
     * Retrieves the X value of the co-ordinate pair.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String getX() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointGetX(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Retrieves the Y value of the co-ordinate pair.
     * @throws RuntimeError Indicates underlying pointer is invalid.
     * @return String
     */
    public String getY() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniKeyPointGetY(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Gets the serialized form, should it be a valid PublicKey.
     * @param format `KeyPoint.PublicKeyEncoding.EllipticCompress` for the compressed form, otherwise the uncompressed form will be returned.
     * @throws RuntimeError Indicates either the underlying pointer is invalid or the co-ordinate pair is not a valid PublicKey.
     * @return String
     */
    public String getPublicKey(PublicKeyEncoding encoding) throws RuntimeError {
        String format = encoding.getValue();

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
