package com.web3auth.tkey_android_distribution.ThresholdKey.Modules;

import androidx.annotation.Nullable;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.ThresholdKey;

public final class ShareSerializationModule {
    private ShareSerializationModule() {
    }

    private static native String jniShareSerializationModuleSerializeShare(long thresholdKey, String share, @Nullable String format, RuntimeError error);

    private static native String jniShareSerializationModuleDeserializeShare(long thresholdKey, String share, @Nullable String format, RuntimeError error);

    public static String serializeShare(ThresholdKey thresholdKey, String share, @Nullable String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareSerializationModuleSerializeShare(thresholdKey.getPointer(), share, format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String deserializeShare(ThresholdKey thresholdKey, String share, @Nullable String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareSerializationModuleDeserializeShare(thresholdKey.getPointer(), share, format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }
}
