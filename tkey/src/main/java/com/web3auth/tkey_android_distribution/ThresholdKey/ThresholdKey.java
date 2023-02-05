package com.web3auth.tkey_android_distribution.ThresholdKey;

import androidx.annotation.Nullable;

import com.web3auth.tkey_android_distribution.RuntimeError;
import com.web3auth.tkey_android_distribution.ThresholdKey.Common.ShareStore;

public final class ThresholdKey {
    private long pointer;
    public static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    private static native long jniThresholdKey(@Nullable Metadata metadata, @Nullable ShareStorePolyIdIndexMap shares, StorageLayer storageLayer, @Nullable ServiceProvider serviceProvider, @Nullable LocalMetadataTransitions localTransitions, @Nullable Metadata lastFetchedCloudMetadata, boolean enableLogging, boolean manualSync, RuntimeError error);

    private static native long jniThresholdKeyGetMetadata(RuntimeError error);

    private static native long jniThresholdKeyInitialize(@Nullable String share, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions, String curveN, RuntimeError error);

    private static native long jniThresholdKeyReconstruct(String curveN, RuntimeError error);

    private static native long jniThresholdKeyGenerateNewShare(String curveN, RuntimeError error);

    private static native void jniThresholdKeyDeleteShare(String shareIndex, String curveN, RuntimeError error);

    private static native long jniThresholdKeyGetKeyDetails(RuntimeError error);

    private static native String jniThresholdKeyOutputShare(String shareIndex, @Nullable String shareType, String curveN, RuntimeError error);

    private static native long jniThresholdKeyOutputShareStore(String shareIndex, @Nullable String polyId, String curveN, RuntimeError error);

    private static native long jniThresholdKeyShareToShareStore(String share, String curveN, RuntimeError error);

    private static native void jniThresholdKeyInputShare(String share, String shareType, String curveN, RuntimeError error);

    private static native void jniThresholdKeyInputShareStore(long share, RuntimeError error);

    private static native String jniThresholdKeyGetShareIndexes(RuntimeError error);

    private static native long jniThresholdKeyGetLastFetchedCloudMetadata(RuntimeError error);

    private static native long jniThresholdKeyGetLocalMetadataTransitions(RuntimeError error);

    private static native String jniThresholdKeyGetTKeyStore(String moduleName, RuntimeError error);

    private static native String jniThresholdKeyGetTKeyStoreItem(String moduleName, String id, RuntimeError error);

    private static native void jniThresholdKeySyncLocalMetadataTransitions(String curveN, RuntimeError error);

    private static native String jniThresholdKeyFree();

    public long getPointer() {
        return pointer;
    }

    public ThresholdKey(@Nullable Metadata metadata, @Nullable ShareStorePolyIdIndexMap shares, StorageLayer storage, @Nullable ServiceProvider provider, @Nullable LocalMetadataTransitions transitions, @Nullable Metadata lastFetchedCloudMetadata, boolean enableLogging, boolean manualSync) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKey(metadata, shares, storage, provider, transitions, lastFetchedCloudMetadata, enableLogging, manualSync, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    public Metadata getMetadata() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGetMetadata(error);
        if (error.code != 0) {
            throw error;
        }
        return new Metadata(ptr);
    }

    public KeyDetails initialize(@Nullable String importShare, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyInitialize(importShare, input, neverInitializedNewKey, includeLocalMetadataTransitions, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyDetails(ptr);
    }

    public KeyReconstructionDetails reconstruct() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyReconstruct(ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyReconstructionDetails(ptr);
    }

    public GenerateShareStoreResult generateNewShare() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGenerateNewShare(ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new GenerateShareStoreResult(ptr);
    }

    public void deleteShare(String shareIndex) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyDeleteShare(shareIndex, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public KeyDetails getKeyDetails() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGetKeyDetails(error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyDetails(ptr);
    }

    public String outputShare(String shareIndex, @Nullable String shareType) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyOutputShare(shareIndex, shareType, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public ShareStore shareToShareStore(String share) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyShareToShareStore(share, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    public void inputShare(String share, @Nullable String shareType) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyInputShare(share, shareType, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public ShareStore outputShareStore(String shareIndex, @Nullable String polyId) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyOutputShareStore(shareIndex, polyId, ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    public void inputShareStore(ShareStore store) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyInputShareStore(store.getPointer(), error);
        if (error.code != 0) {
            throw error;
        }
    }

    public String getShareIndexes() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetShareIndexes(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public Metadata getLastFetchedCloudMetadata() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyGetLastFetchedCloudMetadata(error);
        if (error.code != 0) {
            throw error;
        }
        return new Metadata(result);
    }

    public LocalMetadataTransitions getLocalMetadataTransitions() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyGetLocalMetadataTransitions(error);
        if (error.code != 0) {
            throw error;
        }
        return new LocalMetadataTransitions(result);
    }

    public String getTKeyStore(String moduleName) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetTKeyStore(moduleName, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public String getTKeyStoreItem(String moduleName, String id) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetTKeyStoreItem(moduleName, id, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public void syncLocalMetadataTransitions() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeySyncLocalMetadataTransitions(ThresholdKey.curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniThresholdKeyFree();
    }
}
