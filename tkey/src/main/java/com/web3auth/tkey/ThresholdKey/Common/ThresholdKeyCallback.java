package com.web3auth.tkey.ThresholdKey.Common;

public interface ThresholdKeyCallback<T> {
    void onComplete(Result<T> result);
}
