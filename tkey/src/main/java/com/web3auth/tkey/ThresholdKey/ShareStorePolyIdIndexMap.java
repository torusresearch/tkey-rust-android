package com.web3auth.tkey.ThresholdKey;

import androidx.core.util.Pair;

import com.web3auth.tkey.RuntimeError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class ShareStorePolyIdIndexMap {
    private native String jniShareStorePolyIdIndexMapGetKeys(RuntimeError error);

    private native long jniShareStorePolyIdIndexMapMapGetValueByKey(String key, RuntimeError error);

    private native void jniShareStorePolyIdIndexMapMapFree();

    final long pointer;

    public ShareStorePolyIdIndexMap(long ptr) {
        pointer = ptr;
    }

    public ArrayList<Pair<String, ShareStoreMap>> getShareStoreMaps() throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String keys = jniShareStorePolyIdIndexMapGetKeys(error);
        if (error.code != 0) {
            throw error;
        }

        ArrayList<Pair<String, ShareStoreMap>> array = new ArrayList<>();
        JSONArray json = new JSONArray(keys);
        for (int i = 0; i < json.length(); i++) {
            String key = json.getString(i);
            long ptr = jniShareStorePolyIdIndexMapMapGetValueByKey(key, error);
            if (error.code != 0) {
                throw error;
            }
            ShareStoreMap value = new ShareStoreMap(ptr);
            Pair<String, ShareStoreMap> result = new Pair<>(key, value);
            array.add(result);
        }
        return array;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareStorePolyIdIndexMapMapFree();
    }
}
