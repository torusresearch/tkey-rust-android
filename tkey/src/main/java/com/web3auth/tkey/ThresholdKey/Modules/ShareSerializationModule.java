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


    /**
     * Serialize a share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param share Share to be serialized
     * @throws RuntimeError Indicates invalid parameters was used or invalid threshold key.
     * @return String
     */
    public static String serializeShare(ThresholdKey thresholdKey, String share) throws RuntimeError {
        return serializeShare(thresholdKey, share, "mnemonic");
    }


    /**
     * Serialize a share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param share Share to be serialized
     * @param format Either null or "mnemonic"
     * @throws RuntimeError Indicates invalid parameters was used or invalid threshold key.
     * @return String
     */
    public static String serializeShare(ThresholdKey thresholdKey, String share, @Nullable String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareSerializationModuleSerializeShare(thresholdKey, share, format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }


    /**
     * Deserialize a share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param share Share to be serialized
     * @throws RuntimeError Indicates invalid parameters was used or invalid threshold key.
     * @return String
     */
    public static String deserializeShare(ThresholdKey thresholdKey, String share) throws RuntimeError {
        return deserializeShare(thresholdKey, share, "mnemonic");
    }

    /**
     * Deserialize a share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param share Share to be serialized
     * @param format Either null or "mnemonic"
     * @throws RuntimeError Indicates invalid parameters was used or invalid threshold key.
     * @return String
     */
    public static String deserializeShare(ThresholdKey thresholdKey, String share, @Nullable String format) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniShareSerializationModuleDeserializeShare(thresholdKey, share, format, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }
}
