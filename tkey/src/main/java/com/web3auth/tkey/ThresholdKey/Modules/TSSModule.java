package com.web3auth.tkey.ThresholdKey.Modules;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

public final class TSSModule {
    private TSSModule() {
        //Utility class
    }

    private static native String jniTSSModuleGetTSSPublicKey(ThresholdKey thresholdKey, RuntimeError error);
    
    private static native String jniTSSModuleGetAllTSSTags(ThresholdKey thresholdKey, RuntimeError error);

    private static native void jniTSSModuleSetTSSTag(ThresholdKey thresholdKey, String tssTag, RuntimeError error);
    
    private static native String jniTSSModuleGetTSSTag(ThresholdKey thresholdKey, RuntimeError error);
    
    private static native void jniTSSModuleCreateTaggedTSSTagShare(ThresholdKey thresholdKey, String deviceTssShare, String factorPub, int deviceTssIndex, String curveN, RuntimeError error);
    
    private static native String jniTSSModuleGetTSSShare(ThresholdKey thresholdKey, String factorKey, int threshold, String curveN, RuntimeError error);
    
    private static native int jniGetTSSNonce(ThresholdKey thresholdKey, String tssTag, RuntimeError error);

    private static native void jniCopyFactorPub(ThresholdKey thresholdKey, String newFactorPub, int newTssIndex, String factorPub, String curveN, RuntimeError error);
    
    private static native void jniGenerateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub,  String selectedServers, String authSignatures, String curveN, RuntimeError error);

    private static native void jniDeleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, String factorPub,  String selectedServers, String authSignatures, String curveN, RuntimeError error);

    /**
     * Returns the seed phrases stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid threshold key.
     * @return String
     */
    public static String getTSSPubKey(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetTSSPublicKey(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Returns the seed phrases stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid threshold key.
     * @return String
     */
    public static String getAllTSSTags(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetAllTSSTags(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
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
    public static void setTSSTag(ThresholdKey thresholdKey, String tssTag, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = setTSSTag(thresholdKey, tssTag);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> setTSSTag(ThresholdKey thresholdKey, String tssTag) {
        try {
            RuntimeError error = new RuntimeError();
            jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static String getTSSTag(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetTSSTag(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void createTaggedTSSTagShare(ThresholdKey thresholdKey, String deviceTssShare, String factorPub, int deviceTssIndex, String curveN, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = createTaggedTSSTagShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex, curveN);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> createTaggedTSSTagShare(ThresholdKey thresholdKey, String deviceTssShare, String factorPub, int deviceTssIndex, String curveN) {
        try {
            RuntimeError error = new RuntimeError();
            jniTSSModuleCreateTaggedTSSTagShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static String getTSSShare(ThresholdKey thresholdKey, String factorKey, int threshold, String curveN) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetTSSShare(thresholdKey, factorKey, threshold, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static int getTSSNonce(ThresholdKey thresholdKey, String tssTag) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int result = jniGetTSSNonce(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void copyFactorPub(ThresholdKey thresholdKey, String newFactorPub, int newTssIndex, String factorPub, String curveN, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = copyFactorPub(thresholdKey, newFactorPub, newTssIndex, factorPub, curveN);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> copyFactorPub(ThresholdKey thresholdKey, String newFactorPub, int newTssIndex, String factorPub, String curveN) {
        try {
            RuntimeError error = new RuntimeError();
            jniCopyFactorPub(thresholdKey, newFactorPub, newTssIndex, factorPub, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static void generateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub,  String selectedServers, String authSignatures, String curveN, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = generateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServers, authSignatures, curveN );
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> generateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub,  String selectedServers, String authSignatures, String curveN) {
        try {
            RuntimeError error = new RuntimeError();
            jniGenerateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServers, authSignatures, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static void deleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, String factorPub,  String selectedServers, String authSignatures, String curveN, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = deleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, factorPub, selectedServers, authSignatures, curveN);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> deleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, String factorPub, String selectedServers, String authSignatures, String curveN) {
        try {
            RuntimeError error = new RuntimeError();
            jniDeleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, factorPub, selectedServers, authSignatures, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

}
