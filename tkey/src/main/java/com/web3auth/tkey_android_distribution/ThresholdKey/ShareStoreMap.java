package com.web3auth.tkey_android_distribution.ThresholdKey;

import androidx.core.util.Pair;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.Common.ShareStore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class ShareStoreMap {
    private static native String jniShareStoreMapGetKeys(RuntimeError error);

    private static native long jniShareStoreMapGetValueByKey(String key, RuntimeError error);

    private static native void jniShareStoreMapFree();

    private final long pointer;

    public ShareStoreMap(long ptr) {
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
    }

    public ArrayList<Pair<String, ShareStore>> getShareStores() throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String keys = jniShareStoreMapGetKeys(error);
        if (error.code != 0) {
            throw error;
        }

        ArrayList<Pair<String, ShareStore>> array = new ArrayList<>();
        JSONArray json = new JSONArray(keys);
        for (int i = 0; i < json.length(); i++) {
            String key = json.getString(i);
            long ptr = jniShareStoreMapGetValueByKey(key, error);
            if (error.code != 0) {
                throw error;
            }
            ShareStore value = new ShareStore(ptr);
            Pair<String, ShareStore> result = new Pair<>(key, value);
            array.add(result);
        }
        return array;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareStoreMapFree();
    }
}
