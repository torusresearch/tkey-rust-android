package com.web3auth.tkey_android_distribution.ThresholdKey.Modules;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey_android_distribution.ThresholdKey.ShareTransferStore;
import com.web3auth.tkey_android_distribution.ThresholdKey.ThresholdKey;

public final class SharetransferModule {
    private SharetransferModule() {
    }

    private static native String jniSharetransferModuleRequestNewShare(long thresholdKey, String agent, String indexes, String curveN, RuntimeError error);

    private static native void jniSharetransferModuleAddCustomInfoToRequest(long thresholdKey, String encPubKeyX, String customInfo, String curveN, RuntimeError error);

    private static native String jniSharetransferModuleLookForRequest(long thresholdKey, RuntimeError error);

    private static native void jniSharetransferModuleApproveRequest(long thresholdKey, String encPubKeyX, long shareStore, String curveN, RuntimeError error);

    private static native void jniSharetransferModuleApproveRequestWithShareIndex(long thresholdKey, String encPubKeyX, String indexes, String curveN, RuntimeError error);

    private static native long jniSharetransferModuleGetStore(long thresholdKey, RuntimeError error);

    private static native boolean jniSharetransferModuleSetStore(long thresholdKey, long shareStore, String curveN, RuntimeError error);

    private static native boolean jniSharetransferModuleDeleteStore(long thresholdKey, String encPubKeyX, String curveN, RuntimeError error);

    private static native String jniSharetransferModuleGetCurrentEncryptionKey(long thresholdKey, RuntimeError error);

    private static native long jniSharetransferModuleRequestStatusCheck(long thresholdKey, String encPubKeyX, boolean deleteOnCompletion, String curveN, RuntimeError error);

    private static native void jniSharetransferModuleCleanupRequest(long thresholdKey, RuntimeError error);

    public static String requestNewShare(ThresholdKey thresholdKey, String userAgent, String availableShareIndexes) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharetransferModuleRequestNewShare(thresholdKey.getPointer(), userAgent, availableShareIndexes, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void addCustomInfoToRequest(ThresholdKey thresholdKey, String encPubKeyX, String customInfo) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleAddCustomInfoToRequest(thresholdKey.getPointer(), encPubKeyX, customInfo, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static String lookForRequest(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharetransferModuleLookForRequest(thresholdKey.getPointer(), error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static void approveRequest(ThresholdKey thresholdKey, String encPubKeyX, ShareStore store) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleApproveRequest(thresholdKey.getPointer(), encPubKeyX, store.getPointer(), ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static void approveRequestWithShareIndex(ThresholdKey thresholdKey, String encPubKeyX, String shareIndex) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleApproveRequestWithShareIndex(thresholdKey.getPointer(), encPubKeyX, shareIndex, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public static ShareTransferStore getStore(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniSharetransferModuleGetStore(thresholdKey.getPointer(), error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareTransferStore(result);
    }

    public static Boolean setStore(ThresholdKey thresholdKey, ShareTransferStore store) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        Boolean result = jniSharetransferModuleSetStore(thresholdKey.getPointer(), store.getPointer(), ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static Boolean deleteStore(ThresholdKey thresholdKey, String encPubKeyX) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        Boolean result = jniSharetransferModuleDeleteStore(thresholdKey.getPointer(), encPubKeyX, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static String getCurrentEncryptionKey(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniSharetransferModuleGetCurrentEncryptionKey(thresholdKey.getPointer(), error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static ShareStore requestStatusCheck(ThresholdKey thresholdKey, String encPubKeyX, Boolean deleteRequestOnCompletion) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniSharetransferModuleRequestStatusCheck(thresholdKey.getPointer(), encPubKeyX, deleteRequestOnCompletion, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    public static void cleanupRequest(ThresholdKey thresholdKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniSharetransferModuleCleanupRequest(thresholdKey.getPointer(), error);
        if (error.code != 0) {
            throw error;
        }
    }
}
