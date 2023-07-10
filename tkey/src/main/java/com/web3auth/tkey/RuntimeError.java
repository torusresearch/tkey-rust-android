package com.web3auth.tkey;

import androidx.annotation.NonNull;

public class RuntimeError extends Throwable {
    public int code = -1;

    @NonNull
    @Override
    public String toString() {
        return "RuntimeError{" +
                "code=" + code +
                '}';
    }
}
