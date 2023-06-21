package com.web3auth.tkey.ThresholdKey.Modules;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

public final class SeedPhraseModule {
    private SeedPhraseModule() {
        //Utility class
    }

    private static native void jniSeedPhraseModuleSetSeedPhrase(ThresholdKey thresholdKey, String format, String phrase, int wallets, String curveN, RuntimeError error);

    private static native void jniSeedPhraseModuleChangePhrase(ThresholdKey thresholdKey, String oldPhrase, String newPhrase, String curveN, RuntimeError error);

    private static native String jniSeedPhraseModuleGetSeedPhrases(ThresholdKey thresholdKey, RuntimeError error);

    private static native void jniSeedPhraseModuleDeletePhrase(ThresholdKey thresholdKey, @Nullable String phrase, RuntimeError error);

    /**
     * Sets a seed phrase on the metadata of a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param format "HD Key Tree" is the only supported format.
     * @param phrase The seed phrase. Optional, will be generated if not provided.
     * @param wallets Number of children derived from this seed phrase.
     * @param callback The method which the result will be sent to
     * @see Result
     * @see ThresholdKeyCallback
     */
    public static void setSeedPhrase(ThresholdKey thresholdKey, String format, @Nullable String phrase, int wallets, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = setSeedPhrase(thresholdKey, format, phrase, wallets);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> setSeedPhrase(ThresholdKey thresholdKey, String format, @Nullable String phrase, int wallets) {
        try {
            RuntimeError error = new RuntimeError();
            jniSeedPhraseModuleSetSeedPhrase(thresholdKey, format, phrase, wallets, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Replaces an old seed phrase with a new seed phrase on a ThresholdKey object. Same format of the seed phrase must be used.
     * @param thresholdKey The threshold key to act on.
     * @param oldPhrase The original seed phrase.
     * @param newPhrase The replacement seed phrase.
     * @param callback The method which the result will be sent to
     * @see Result
     * @see ThresholdKeyCallback
     */
    public static void changePhrase(ThresholdKey thresholdKey, String oldPhrase, String newPhrase, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = changePhrase(thresholdKey, oldPhrase, newPhrase);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> changePhrase(ThresholdKey thresholdKey, String oldPhrase, String newPhrase) {
        try {
            RuntimeError error = new RuntimeError();
            jniSeedPhraseModuleChangePhrase(thresholdKey, oldPhrase, newPhrase, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Returns the seed phrases stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid threshold key.
     * @return String
     */
    public static String getPhrases(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSeedPhraseModuleGetSeedPhrases(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Deletes a seed phrase stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param phrase The original seed phrase.
     * @param callback The method which the result will be sent to
     * @see Result
     * @see ThresholdKeyCallback
     */
    public static void deletePhrase(ThresholdKey thresholdKey, String phrase, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = deletePhrase(thresholdKey, phrase);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> deletePhrase(ThresholdKey thresholdKey, String phrase) {
        try {
            RuntimeError error = new RuntimeError();
            jniSeedPhraseModuleDeletePhrase(thresholdKey, phrase, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }
}
