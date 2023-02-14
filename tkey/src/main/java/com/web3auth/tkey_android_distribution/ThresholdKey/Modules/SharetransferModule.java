package com.web3auth.tkey_android_distribution.ThresholdKey.Modules;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey_android_distribution.ThresholdKey.ShareTransferStore;
import com.web3auth.tkey_android_distribution.ThresholdKey.ThresholdKey;

public final class SharetransferModule {
    private SharetransferModule() {
    }

    private static native String jniSharetransferModuleRequestNewShare(ThresholdKey thresholdKey, String agent, String indexes, String curveN, RuntimeError error);

    private static native void jniSharetransferModuleAddCustomInfoToRequest(ThresholdKey thresholdKey, String encPubKeyX, String customInfo, String curveN, RuntimeError error);

    private static native String jniSharetransferModuleLookForRequest(ThresholdKey thresholdKey, RuntimeError error);

    private static native void jniSharetransferModuleApproveRequest(ThresholdKey thresholdKey, String encPubKeyX, ShareStore shareStore, String curveN, RuntimeError error);

    private static native void jniSharetransferModuleApproveRequestWithShareIndex(ThresholdKey thresholdKey, String encPubKeyX, String indexes, String curveN, RuntimeError error);

    private static native long jniSharetransferModuleGetStore(ThresholdKey thresholdKey, RuntimeError error);

    private static native boolean jniSharetransferModuleSetStore(ThresholdKey thresholdKey, ShareTransferStore shareStore, String curveN, RuntimeError error);

    private static native boolean jniSharetransferModuleDeleteStore(ThresholdKey thresholdKey, String encPubKeyX, String curveN, RuntimeError error);

    private static native String jniSharetransferModuleGetCurrentEncryptionKey(ThresholdKey thresholdKey, RuntimeError error);

    private static native long jniSharetransferModuleRequestStatusCheck(ThresholdKey thresholdKey, String encPubKeyX, boolean deleteOnCompletion, String curveN, RuntimeError error);

    private static native void jniSharetransferModuleCleanupRequest(ThresholdKey thresholdKey, RuntimeError error);

    public static String requestNewShare(ThresholdKey thresholdKey, String userAgent, String availableShareIndexes) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharetransferModuleRequestNewShare(thresholdKey, userAgent, availableShareIndexes, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void addCustomInfoToRequest(ThresholdKey thresholdKey, String encPubKeyX, String customInfo) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleAddCustomInfoToRequest(thresholdKey, encPubKeyX, customInfo, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static String lookForRequest(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharetransferModuleLookForRequest(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void approveRequest(ThresholdKey thresholdKey, String encPubKeyX, ShareStore store) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleApproveRequest(thresholdKey, encPubKeyX, store, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static void approveRequestWithShareIndex(ThresholdKey thresholdKey, String encPubKeyX, String shareIndex) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleApproveRequestWithShareIndex(thresholdKey, encPubKeyX, shareIndex, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static ShareTransferStore getStore(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniSharetransferModuleGetStore(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareTransferStore(result);
    }

    public static Boolean setStore(ThresholdKey thresholdKey, ShareTransferStore store) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        Boolean result = jniSharetransferModuleSetStore(thresholdKey, store, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static Boolean deleteStore(ThresholdKey thresholdKey, String encPubKeyX) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        Boolean result = jniSharetransferModuleDeleteStore(thresholdKey, encPubKeyX, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String getCurrentEncryptionKey(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharetransferModuleGetCurrentEncryptionKey(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static ShareStore requestStatusCheck(ThresholdKey thresholdKey, String encPubKeyX, Boolean deleteRequestOnCompletion) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniSharetransferModuleRequestStatusCheck(thresholdKey, encPubKeyX, deleteRequestOnCompletion, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    public static void cleanupRequest(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleCleanupRequest(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
    }
}
