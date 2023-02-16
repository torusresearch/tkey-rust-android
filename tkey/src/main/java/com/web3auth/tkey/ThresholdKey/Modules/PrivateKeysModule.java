package com.web3auth.tkey.ThresholdKey.Modules;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public final class PrivateKeysModule {
    private PrivateKeysModule() {
    }

    private static native boolean jniPrivateKeysModuleSetPrivateKey(ThresholdKey thresholdKey, @Nullable String key, String format, String curveN, RuntimeError error);

    private static native String jniPrivateKeysModuleGetPrivateKey(ThresholdKey thresholdKey, RuntimeError error);

    private static native String jniPrivateKeysModuleGetPrivateKeyAccounts(ThresholdKey thresholdKey, RuntimeError error);

    public static Boolean setPrivateKey(ThresholdKey thresholdKey, @Nullable String key, String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        boolean result = jniPrivateKeysModuleSetPrivateKey(thresholdKey, key, format, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String getPrivateKeys(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniPrivateKeysModuleGetPrivateKey(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static ArrayList<String> getPrivateKeyAccounts(ThresholdKey thresholdKey) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String result = jniPrivateKeysModuleGetPrivateKeyAccounts(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<String> array = new ArrayList<>();
        JSONArray json = new JSONArray(result);
        for (int i = 0; i < json.length(); i++) {
            String value = json.getString(i);
            array.add(value);
        }
        return array;
    }
}
