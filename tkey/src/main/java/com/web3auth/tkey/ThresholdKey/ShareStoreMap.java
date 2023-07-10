package com.web3auth.tkey.ThresholdKey;

import androidx.core.util.Pair;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class ShareStoreMap {
    private native String jniShareStoreMapGetKeys(RuntimeError error);

    private native long jniShareStoreMapGetValueByKey(String key, RuntimeError error);

    private native void jniShareStoreMapFree();

    final long pointer;

    /**
     * Instantiates a ShareStoreMap object.
     * @param ptr The pointer to the underlying foreign function interface object.
     */
    public ShareStoreMap(long ptr) {
        pointer = ptr;
    }

    /**
     * Returns the share stores.
     * @throws RuntimeError Indicates invalid pointer.
     * @throws JSONException Data in object is malformed.
     * @return ArrayList
     * @see ShareStore
     */
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
