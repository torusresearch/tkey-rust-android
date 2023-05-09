package com.web3auth.tkey.ThresholdKey.Modules;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

public final class ShareSerializationModule {
    private ShareSerializationModule() {
        //Utility class
    }

    private static native String jniShareSerializationModuleSerializeShare(ThresholdKey thresholdKey, String share, @Nullable String format, RuntimeError error);

    private static native String jniShareSerializationModuleDeserializeShare(ThresholdKey thresholdKey, String share, @Nullable String format, RuntimeError error);

    public static String serializeShare(ThresholdKey thresholdKey, String share, @Nullable String format = null) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareSerializationModuleSerializeShare(thresholdKey, share, format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String deserializeShare(ThresholdKey thresholdKey, String share, @Nullable String format = null) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareSerializationModuleDeserializeShare(thresholdKey, share, format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }
}
