package com.web3auth.tkey.ThresholdKey;

import androidx.core.util.Pair;

import com.web3auth.tkey.RuntimeError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class ShareMap {

    private native void jniShareMapFree();
    private native String jniShareMapGetKeys(long ptr, RuntimeError error);
    private native String jniShareMapGetShareByKey(long ptr, String key, RuntimeError error);

    public ArrayList<Pair<String, String>> share_map;

    public ShareMap(long ptr) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String keys = jniShareMapGetKeys(ptr, error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<Pair<String, String>> array = new ArrayList<>();
        JSONArray json = new JSONArray(keys);
        for (int i = 0; i < json.length(); i++) {
            String key = json.getString(i);
            String value = jniShareMapGetShareByKey(ptr, key, error);
            if (error.code != 0) {
                throw error;
            }
            Pair<String, String> result = new Pair<>(key, value);
            array.add(result);
        }
        share_map = array;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniShareMapFree();
    }
}
