package com.web3auth.tkey_android_distribution;

public class RuntimeError extends Throwable {
    public int code = -1;

    @Override
    public String toString() {
        return "RuntimeError{" +
                "code=" + code +
                '}';
    }
}
