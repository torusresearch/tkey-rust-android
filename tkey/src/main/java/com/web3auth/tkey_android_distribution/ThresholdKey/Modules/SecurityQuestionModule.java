package com.web3auth.tkey_android_distribution.ThresholdKey.Modules;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey_android_distribution.ThresholdKey.ThresholdKey;

public final class SecurityQuestionModule {
    private SecurityQuestionModule() {
    }

    private static native long jniSecurityQuestionModuleGenerateShareStoreResult(ThresholdKey thresholdKey, String questions, String answer, String curveN, RuntimeError error);

    private static native boolean jniSecurityQuestionModuleInputShare(ThresholdKey thresholdKey, String answer, String curveN, RuntimeError error);

    private static native boolean jniSecurityQuestionModuleChangeQuestionAndAnswer(ThresholdKey thresholdKey, String questions, String answer, String curveN, RuntimeError error);

    private static native boolean jniSecurityQuestionModuleStoreAnswer(ThresholdKey thresholdKey, String answer, String curveN, RuntimeError error);

    private static native String jniSecurityQuestionModuleGetAnswer(ThresholdKey thresholdKey, RuntimeError error);

    private static native String jniSecurityQuestionModuleGetQuestions(ThresholdKey thresholdKey, RuntimeError error);

    public static GenerateShareStoreResult generateNewShare(ThresholdKey thresholdKey, String questions, String answer) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniSecurityQuestionModuleGenerateShareStoreResult(thresholdKey, questions, answer, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new GenerateShareStoreResult(result);
    }

    public static Boolean inputShare(ThresholdKey thresholdKey, String answer) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        boolean result = jniSecurityQuestionModuleInputShare(thresholdKey, answer, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static Boolean changeSecurityQuestionAndAnswer(ThresholdKey thresholdKey, String questions, String answer) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        boolean result = jniSecurityQuestionModuleChangeQuestionAndAnswer(thresholdKey, questions, answer, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static Boolean storeAnswer(ThresholdKey thresholdKey, String answer) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        boolean result = jniSecurityQuestionModuleStoreAnswer(thresholdKey, answer, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String getAnswer(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSecurityQuestionModuleGetAnswer(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String getQuestions(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSecurityQuestionModuleGetQuestions(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }
}
