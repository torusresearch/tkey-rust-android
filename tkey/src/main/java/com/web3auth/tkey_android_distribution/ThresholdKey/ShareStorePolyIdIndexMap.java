package com.web3auth.tkey_android_distribution.ThresholdKey;

import androidx.core.util.Pair;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.Common.ShareStore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class ShareStorePolyIdIndexMap {
    private static native String jniShareStorePolyIdIndexMapGetKeys(RuntimeError error);

    private static native long jniShareStorePolyIdIndexMapMapGetValueByKey(String key, RuntimeError error);

    private static native void jniShareStorePolyIdIndexMapMapFree();

    private final long pointer;

    public ShareStorePolyIdIndexMap(long ptr) {
        pointer = ptr;
    }

    public long getPointer() {
        return pointer;
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
