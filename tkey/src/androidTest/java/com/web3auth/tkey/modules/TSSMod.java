package com.web3auth.tkey.modules;


import com.web3auth.tkey.ThresholdKey.ThresholdKey;

public final class TSSMod {
    private final ThresholdKey thresholdKey;
    private final String tag;

    public TSSMod(ThresholdKey thresholdKey, String tag) {
        this.thresholdKey = thresholdKey;
        this.tag = tag;
    }

    public ThresholdKey getThresholdKey() {
        return thresholdKey;
    }

    public String getTag() {
        return tag;
    }
}
