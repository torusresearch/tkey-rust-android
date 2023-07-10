package com.web3auth.tkey.ThresholdKey.Modules;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

public final class SecurityQuestionModule {
    private SecurityQuestionModule() {
        //Utility class
    }

    private static native long jniSecurityQuestionModuleGenerateShareStoreResult(ThresholdKey thresholdKey, String questions, String answer, String curveN, RuntimeError error);

    private static native boolean jniSecurityQuestionModuleInputShare(ThresholdKey thresholdKey, String answer, String curveN, RuntimeError error);

    private static native boolean jniSecurityQuestionModuleChangeQuestionAndAnswer(ThresholdKey thresholdKey, String questions, String answer, String curveN, RuntimeError error);

    private static native boolean jniSecurityQuestionModuleStoreAnswer(ThresholdKey thresholdKey, String answer, String curveN, RuntimeError error);

    private static native String jniSecurityQuestionModuleGetAnswer(ThresholdKey thresholdKey, RuntimeError error);

    private static native String jniSecurityQuestionModuleGetQuestions(ThresholdKey thresholdKey, RuntimeError error);

    /**
     * Generates a new security share on an existing ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param questions The security question
     * @param answer The answer for the security question.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     * @see GenerateShareStoreResult
     */
    public static void generateNewShare(ThresholdKey thresholdKey, String questions, String answer, ThresholdKeyCallback<GenerateShareStoreResult> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<GenerateShareStoreResult> result = generateNewShare(thresholdKey, questions, answer);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<GenerateShareStoreResult> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<GenerateShareStoreResult> generateNewShare(ThresholdKey thresholdKey, String questions, String answer) {
        try {
            RuntimeError error = new RuntimeError();
            long result = jniSecurityQuestionModuleGenerateShareStoreResult(thresholdKey, questions, answer, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(new GenerateShareStoreResult(result));
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }


    /**
     * Inputs a stored security share into an existing ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param answer The answer for the security question of the stored share.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void inputShare(ThresholdKey thresholdKey, String answer, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = inputShare(thresholdKey, answer);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> inputShare(ThresholdKey thresholdKey, String answer) {
        try {
            RuntimeError error = new RuntimeError();
            boolean result = jniSecurityQuestionModuleInputShare(thresholdKey, answer, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(result);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Changes the question and answer for an existing security share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @param questions The security question.
     * @param answer The answer for the security question.
     * @param callback The method which the result will be sent to
     * @see Result
     * @see ThresholdKeyCallback
     */
    public static void changeSecurityQuestionAndAnswer(ThresholdKey thresholdKey, String questions, String answer, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = changeSecurityQuestionAndAnswer(thresholdKey, questions, answer);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> changeSecurityQuestionAndAnswer(ThresholdKey thresholdKey, String questions, String answer) {
        try {
            RuntimeError error = new RuntimeError();
            boolean result = jniSecurityQuestionModuleChangeQuestionAndAnswer(thresholdKey, questions, answer, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(result);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Saves the answer for an existing security share on a ThresholdKey object to the tkey store.
     * @param thresholdKey The threshold key to act on.
     * @param answer The answer for the security question.
     * @param callback The method which the result will be sent to
     * @see Result
     * @see ThresholdKeyCallback
     */
    public static void storeAnswer(ThresholdKey thresholdKey, String answer, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = storeAnswer(thresholdKey, answer);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> storeAnswer(ThresholdKey thresholdKey, String answer) {
        try {
            RuntimeError error = new RuntimeError();
            boolean result = jniSecurityQuestionModuleStoreAnswer(thresholdKey, answer, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(result);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Retrieves the answer for an existing security share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid pointer.
     * @return String
     */
    public static String getAnswer(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSecurityQuestionModuleGetAnswer(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Retrieves the question for an existing security share on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid pointer.
     * @return String
     */
    public static String getQuestions(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSecurityQuestionModuleGetQuestions(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }
}
