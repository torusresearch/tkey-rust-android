package com.web3auth.tkey.ThresholdKey;

import androidx.core.util.Pair;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;

import java.util.ArrayList;

public final class ShareMap {

    private native void jniShareMapFree();
    private native String jniShareMapGetKeys(RuntimeError error);
    private native String jniShareMapGetShareByKey(String key, RuntimeError error);

    public ArrayList<Pair<String, ShareStore>> share_map;

    public ShareMap(long ptr) {

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareMapFree();
    }
}
