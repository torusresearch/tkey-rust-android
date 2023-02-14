package com.web3auth.tkey_android_distribution.ThresholdKey.Modules;

import androidx.annotation.Nullable;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.ThresholdKey;

public final class SeedPhraseModule {
    private SeedPhraseModule() {
    }

    private static native void jniSeedPhraseModuleSetSeedPhrase(ThresholdKey  thresholdKey, String format, String phrase, int wallets, String curveN, RuntimeError error);

    private static native void jniSeedPhraseModuleChangePhrase(ThresholdKey  thresholdKey, String oldPhrase, String newPhrase, String curveN, RuntimeError error);

    private static native String jniSeedPhraseModuleGetSeedPhrases(ThresholdKey  thresholdKey, RuntimeError error);

    private static native void jniSeedPhraseModuleDeletePhrase(ThresholdKey thresholdKey, @Nullable String phrase, RuntimeError error);

    public static void setSeedPhrase(ThresholdKey thresholdKey, String format, @Nullable String phrase, int wallets) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSeedPhraseModuleSetSeedPhrase(thresholdKey, format, phrase, wallets, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static void changePhrase(ThresholdKey thresholdKey, String oldPhrase, String newPhrase) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSeedPhraseModuleChangePhrase(thresholdKey, oldPhrase, newPhrase, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static String getPhrases(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSeedPhraseModuleGetSeedPhrases(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void deletePhrase(ThresholdKey thresholdKey, String phrase) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSeedPhraseModuleDeletePhrase(thresholdKey, phrase, error);
        if (error.code != 0) {
            throw error;
        }
    }
}
