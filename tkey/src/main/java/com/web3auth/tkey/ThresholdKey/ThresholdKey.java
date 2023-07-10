package com.web3auth.tkey.ThresholdKey;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.KeyPoint;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;
import com.web3auth.tkey.ThresholdKey.Common.TssOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class ThresholdKey {
    public final Executor executor;
    final long pointer;
    public String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    private native long jniThresholdKey(@Nullable Metadata metadata, @Nullable ShareStorePolyIdIndexMap shares, StorageLayer storageLayer, @Nullable ServiceProvider serviceProvider, @Nullable LocalMetadataTransitions localTransitions, @Nullable Metadata lastFetchedCloudMetadata, boolean enableLogging, boolean manualSync, @Nullable RssComm rss, RuntimeError error);

    private native long jniThresholdKeyGetMetadata(RuntimeError error);

    private native long jniThresholdKeyInitialize(@Nullable String share, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions, String curveN, boolean useTss, @Nullable String device_tss_share, int device_tss_index, @Nullable KeyPoint factor_pub, RuntimeError error);

    private native long jniThresholdKeyReconstruct(String curveN, RuntimeError error);

    private native long jniThresholdKeyGenerateNewShare(String curveN, boolean useTss, @Nullable TssOptions options, RuntimeError error);

    private native void jniThresholdKeyDeleteShare(String shareIndex, String curveN, boolean useTss, @Nullable TssOptions options, RuntimeError error);

    private native long jniThresholdKeyGetKeyDetails(RuntimeError error);

    private native String jniThresholdKeyOutputShare(String shareIndex, @Nullable String shareType, String curveN, RuntimeError error);

    private native long jniThresholdKeyOutputShareStore(String shareIndex, @Nullable String polyId, String curveN, RuntimeError error);

    private native long jniThresholdKeyShareToShareStore(String share, String curveN, RuntimeError error);

    private native void jniThresholdKeyInputShare(String share, String shareType, String curveN, RuntimeError error);

    private native void jniThresholdKeyInputShareStore(ShareStore share, RuntimeError error);

    private native String jniThresholdKeyGetShareIndexes(RuntimeError error);

    private native long jniThresholdKeyGetShares(RuntimeError error);

    private native long jniThresholdKeyGetLastFetchedCloudMetadata(RuntimeError error);

    private native long jniThresholdKeyGetLocalMetadataTransitions(RuntimeError error);

    private native String jniThresholdKeyGetTKeyStore(String moduleName, RuntimeError error);

    private native String jniThresholdKeyGetTKeyStoreItem(String moduleName, String id, RuntimeError error);

    private native void jniThresholdKeySyncLocalMetadataTransitions(String curveN, RuntimeError error);

    private native void jniThresholdKeyDelete(String curveN, RuntimeError error);

    private native void jniThresholdKeyAddShareDescription(String key, String Description, boolean updateMetadata, String curveN, RuntimeError error);

    private native void jniThresholdKeyDeleteShareDescription(String key, String Description, boolean updateMetadata, String curveN, RuntimeError error);

    private native void jniThresholdKeyUpdateShareDescription(String key, String oldDescription, String newDescription, boolean updateMetadata, String curveN, RuntimeError error);

    private native String jniThresholdKeyGetShareDescriptions(RuntimeError error);

    private native String jniThresholdKeyStorageLayerGetMetadata(@Nullable String privateKey, RuntimeError error);

    private native void jniThresholdKeyStorageLayerSetMetadata(@Nullable String privateKey, String json, String curveN, RuntimeError error);

    private native void jniThresholdKeyStorageLayerSetMetadataStream(String privateKeys, String json, String curveN, RuntimeError error);

    private native long jniThresholdKeyGetAllShareStoresForLatestPolynomial(String curveN, RuntimeError error);

    private native long jniThresholdKeyReconstructLatestPolynomial(String curveN, RuntimeError error);

    public native void jniThresholdKeyServiceProviderAssignPublicKey(String tss_tag, String tss_nonce, String tss_public_key, RuntimeError error);

    private native void jniThresholdKeyFree();

    /**
     * Instantiates a ThresholdKey object.
     * @param metadata Existing metadata to be used, optional.
     * @param shares Existing shares to be used, optional.
     * @param storage StorageLayer to be used.
     * @param provider Service provider to be used, optional only in the most basic usage of tKey.
     * @param transitions Existing local transitions to be used.
     * @param lastFetchedCloudMetadata Existing cloud metadata to be used.
     * @param enableLogging Determines whether logging is available or not (pending).
     * @param manualSync Determines if changes to the metadata are automatically synced.
     * @param rss Communication object for the RSS service, required for TSS.
     * @throws RuntimeError Indicates invalid parameters were used.
     * @see Metadata
     * @see ShareStorePolyIdIndexMap
     * @see StorageLayer
     * @see ServiceProvider
     * @see LocalMetadataTransitions
     * @see RssComm
     *
     */
    public ThresholdKey(@Nullable Metadata metadata, @Nullable ShareStorePolyIdIndexMap shares, StorageLayer storage, @Nullable ServiceProvider provider, @Nullable LocalMetadataTransitions transitions, @Nullable Metadata lastFetchedCloudMetadata, boolean enableLogging, boolean manualSync, @Nullable RssComm rss) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        this.executor = Executors.newSingleThreadExecutor();

        long ptr = jniThresholdKey(metadata, shares, storage, provider, transitions, lastFetchedCloudMetadata, enableLogging, manualSync, rss, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    /**
     * Returns the metadata.
     * @throws RuntimeError Indicates invalid parameters were used.
     * @return Metadata
     * @see Metadata
     *
     */
    public Metadata getMetadata() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGetMetadata(error);
        if (error.code != 0) {
            throw error;
        }
        return new Metadata(ptr);
    }

    /**
     * Initializes a ThresholdKey object.
     * @param importShare Share to be imported, optional.
     * @param input Sharestore used, optional.
     * @param neverInitializedNewKey Do not initialize a new tKey is an existing one is found.
     * @param includeLocalMetadataTransitions Prioritize existing metadata transitions over cloud fetched transitions.
     * @param useTss If TSS is to be used or not.
     * @param device_tss_share Device share for TSS, optional.
     * @param device_tss_index Device index for TSS.
     * @param factor_pub Factor key for TSS, optional.
     * @param callback The method which the result will be sent to
     * @see ShareStore
     * @see ThresholdKeyCallback
     * @see KeyDetails
     */
    public void initialize(@Nullable String importShare, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions, boolean useTss, @Nullable String device_tss_share, int device_tss_index, @Nullable KeyPoint factor_pub, final ThresholdKeyCallback<KeyDetails> callback) {
        executor.execute(
                () -> {
                    try {
                        Result<KeyDetails> result = initialize(importShare, input, neverInitializedNewKey, includeLocalMetadataTransitions, useTss, device_tss_share, device_tss_index, factor_pub);
                        callback.onComplete(result);
                    } catch (Exception e) {
                        Result<KeyDetails> error = new Result.Error<>(e);
                        callback.onComplete(error);
                    }
                }
        );
    }

    /**
     * Initializes a ThresholdKey object.
     * @param importShare Share to be imported, optional.
     * @param input Sharestore used, optional.
     * @param useTss If TSS should be used or not.
     * @param device_tss_share Device share for TSS, optional.
     * @param device_tss_index Device index for TSS.
     * @param factor_pub Factor key for TSS, optional.
     * @param callback The method which the result will be sent to
     * @see ShareStore
     * @see ThresholdKeyCallback
     * @see KeyDetails
     */
    public void initialize(@Nullable String importShare, @Nullable ShareStore input, boolean useTss, @Nullable String device_tss_share, int device_tss_index, @Nullable KeyPoint factor_pub, final ThresholdKeyCallback<KeyDetails> callback) {
        executor.execute(
                () -> {
                    try {
                        Result<KeyDetails> result = initialize(importShare, input, false, false, useTss, device_tss_share, device_tss_index, factor_pub);
                        callback.onComplete(result);
                    } catch (Exception e) {
                        Result<KeyDetails> error = new Result.Error<>(e);
                        callback.onComplete(error);
                    }
                }
        );
    }

    private Result<KeyDetails> initialize(@Nullable String importShare, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions, boolean useTss, @Nullable String device_tss_share, int device_tss_index, @Nullable KeyPoint factor_pub) {
        try {
            RuntimeError error = new RuntimeError();
            long ptr = jniThresholdKeyInitialize(importShare, input, neverInitializedNewKey, includeLocalMetadataTransitions, curveN, useTss, device_tss_share, device_tss_index, factor_pub, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(new KeyDetails(ptr));
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Function to retrieve the metadata directly from the network, only used in very specific instances.
     * @param privateKey The reconstructed private key, optional.
     * @param callback The method which the result will be sent to
     */
    public void storage_layer_get_metadata(@Nullable String privateKey, final ThresholdKeyCallback<String> callback) {
        executor.execute(
                () -> {
                    try {
                        Result<String> result = storage_layer_get_metadata(privateKey);
                        callback.onComplete(result);
                    } catch (Exception e) {
                        Result<String> error = new Result.Error<>(e);
                        callback.onComplete(error);
                    }
                }
        );
    }

    private Result<Void> storage_layer_set_metadata(@Nullable String privateKey, String json) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyStorageLayerSetMetadata(privateKey, json, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Function to set the metadata directly to the network, only used in very specific instances.
     * @param privateKey The private key, optional.
     * @param json Relevant json to be set.
     * @param callback The method which the result will be sent to
     */
    public void storage_layer_set_metadata(@Nullable String privateKey, String json, final ThresholdKeyCallback<Void> callback) {
        executor.execute(
                () -> {
                    try {
                        Result<Void> result = storage_layer_set_metadata(privateKey, json);
                        callback.onComplete(result);
                    } catch (Exception e) {
                        Result<Void> error = new Result.Error<>(e);
                        callback.onComplete(error);
                    }
                }
        );
    }

    private Result<Void> storage_layer_set_metadata_stream(String privateKeys, String json) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyStorageLayerSetMetadataStream(privateKeys, json, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Function to set the metadata directly to the network, only used in very specific instances.
     * @param privateKeys The relevant private keys.
     * @param json Relevant json to be set.
     * @param callback The method which the result will be sent to
     */
    public void storage_layer_set_metadata_stream(String privateKeys, String json, final ThresholdKeyCallback<Void> callback) {
        executor.execute(
                () -> {
                    try {
                        Result<Void> result = storage_layer_set_metadata_stream(privateKeys, json);
                        callback.onComplete(result);
                    } catch (Exception e) {
                        Result<Void> error = new Result.Error<>(e);
                        callback.onComplete(error);
                    }
                }
        );
    }

    private Result<String> storage_layer_get_metadata(@Nullable String privateKey) {
        try {
            RuntimeError error = new RuntimeError();
            String result = jniThresholdKeyStorageLayerGetMetadata(privateKey, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(result);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Reconstructs the private key, this assumes that the number of shares inserted into the `ThrehsoldKey` are equal or greater than the threshold.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     * @see KeyReconstructionDetails
     */
    public void reconstruct(final ThresholdKeyCallback<KeyReconstructionDetails> callback) {
        executor.execute(() -> {
            try {
                Result<KeyReconstructionDetails> result = reconstruct();
                callback.onComplete(result);
            } catch (Exception e) {
                Result<KeyReconstructionDetails> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<KeyReconstructionDetails> reconstruct() {
        try {
            RuntimeError error = new RuntimeError();
            long ptr = jniThresholdKeyReconstruct(curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(new KeyReconstructionDetails(ptr));
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Generates a new share.
     * @param callback The method which the result will be sent to
     * @param useTss Whether TSS is used or not.
     * @param options Options for TSS
     * @see ThresholdKeyCallback
     * @see GenerateShareStoreResult
     * @see TssOptions
     */
    public void generateNewShare(boolean useTss, @Nullable TssOptions options, ThresholdKeyCallback<GenerateShareStoreResult> callback) {
        executor.execute(() -> {
            try {
                Result<GenerateShareStoreResult> result = generateNewShare(useTss, options);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<GenerateShareStoreResult> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<GenerateShareStoreResult> generateNewShare(boolean useTss, @Nullable TssOptions options) {
        try {
            RuntimeError error = new RuntimeError();
            long ptr = jniThresholdKeyGenerateNewShare(curveN, useTss, options, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(new GenerateShareStoreResult(ptr));
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Deletes a share at the specified index. Caution is advised to not try delete a share that would prevent the total number of shares being below the threshold.
     * @param shareIndex Index of share to be deleted.
     * @param useTss Whether TSS is used or not.
     * @param options Options for TSS
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     * @see TssOptions
     */
    public void deleteShare(String shareIndex, boolean useTss, @Nullable TssOptions options, ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = deleteShare(shareIndex, useTss, options);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> deleteShare(String shareIndex, boolean useTss, @Nullable TssOptions options) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyDeleteShare(shareIndex, curveN, useTss, options, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Returns the key details, mainly used after reconstruction.
     * @throws RuntimeError Indicates invalid pointer.
     * @return KeyDetails
     * @see KeyDetails
     */
    public KeyDetails getKeyDetails() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGetKeyDetails(error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyDetails(ptr);
    }

    /**
     * Retrieves a specific share.
     * @param shareIndex The index of the share to output.
     * @throws RuntimeError Indicates invalid pointer or invalid index.
     * @return String
     */
    public String outputShare(String shareIndex) throws RuntimeError {
        return outputShare(shareIndex, null);
    }

    /**
     * Retrieves a specific share.
     * @param shareIndex The index of the share to output.
     * @param shareType The format of the output, can be `"mnemonic"`, optional.
     * @throws RuntimeError Indicates invalid pointer or invalid index.
     * @return String
     */
    public String outputShare(String shareIndex, @Nullable String shareType) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyOutputShare(shareIndex, shareType, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    /**
     * Converts a share to a ShareStore.
     * @param share Hexadecimal representation of a share.
     * @throws RuntimeError Indicates invalid parameters.
     * @return ShareStore
     * @see ShareStore
     */
    public ShareStore shareToShareStore(String share) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyShareToShareStore(share, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    /**
     * Converts a share to a ShareStore.
     * @param share Hexadecimal representation of a share.
     * @param shareType The format of the share, can be `"mnemonic"`, optional.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     * @see ShareStore
     */
    public void inputShare(String share, @Nullable String shareType, ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = inputShare(share, shareType);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> inputShare(String share, @Nullable String shareType) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyInputShare(share, shareType, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Retrieves a specific share store.
     * @param shareIndex The index of the share to output.
     * @param polyId The polynomial id to be used for the output, optional.
     * @throws RuntimeError Indicates invalid pointer or invalid index.
     * @return ShareStore
     * @see ShareStore
     */
    public ShareStore outputShareStore(String shareIndex, @Nullable String polyId) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyOutputShareStore(shareIndex, polyId, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    /**
     * Inserts a ShareStore, useful for insertion before reconstruction to ensure the number of shares meet the minimum threshold.
     * @param store The ShareStore to be inserted.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     * @see ShareStore
     */
    public void inputShareStore(ShareStore store, ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = inputShareStore(store);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> inputShareStore(ShareStore store) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyInputShareStore(store, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Retrieves all share indexes.
     * @throws RuntimeError Indicates invalid pointer.
     * @throws JSONException Data in object is malformed.
     * @return ArrayList
     */
    public ArrayList<String> getShareIndexes() throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetShareIndexes(error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<String> indexes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            indexes.add(jsonArray.getString(i));
        }
        return indexes;
    }

    /**
     * Returns last metadata fetched from the cloud.
     * @throws RuntimeError Indicates invalid pointer.
     * @return Metadata
     * @see Metadata
     */
    public Metadata getLastFetchedCloudMetadata() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyGetLastFetchedCloudMetadata(error);
        if (error.code != 0) {
            throw error;
        }
        return new Metadata(result);
    }

    /**
     * Returns current metadata transitions not yet synchronised.
     * @throws RuntimeError Indicates invalid pointer.
     * @return Metadata
     * @see Metadata
     */
    public LocalMetadataTransitions getLocalMetadataTransitions() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyGetLocalMetadataTransitions(error);
        if (error.code != 0) {
            throw error;
        }
        return new LocalMetadataTransitions(result);
    }

    /**
     * Returns the tKey store for a module.
     * @param moduleName Specific name of the module.
     * @throws RuntimeError Indicates invalid pointer.
     * @throws JSONException Data in object is malformed.
     * @return ArrayList
     */
    public ArrayList<JSONObject> getTKeyStore(String moduleName) throws JSONException, RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetTKeyStore(moduleName, error);
        if (error.code != 0) {
            throw error;
        }
        JSONArray store = new JSONArray(result);
        ArrayList<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < store.length(); i++) {
            JSONObject value = store.getJSONObject(i);
            list.add(value);
        }
        return list;
    }

    /**
     * Returns the specific tKey store item json for a module.
     * @param moduleName Specific name of the module.
     * @param id Identifier of the item.
     * @throws RuntimeError Indicates invalid pointer.
     * @throws JSONException Data in object is malformed.
     * @return JSONObject
     */
    public JSONObject getTKeyStoreItem(String moduleName, String id) throws JSONException, RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetTKeyStoreItem(moduleName, id, error);
        if (error.code != 0) {
            throw error;
        }
        return new JSONObject(result);
    }

    /**
     * Syncronises metadata transitions, only used if manual sync is enabled.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public void syncLocalMetadataTransitions(ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = syncLocalMetadataTransitions();
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> syncLocalMetadataTransitions() {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeySyncLocalMetadataTransitions(curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Returns all shares according to their mapping.
     * @throws RuntimeError Indicates invalid pointer.
     * @return ShareStorePolyIdIndexMap
     * @see ShareStorePolyIdIndexMap
     */
    public ShareStorePolyIdIndexMap getShares() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyGetShares(error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStorePolyIdIndexMap(result);
    }

    /**
     * Permanently deletes a tKey, this process is irrecoverable.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public void CRITICALDeleteTKey(ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = deleteTKey();
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> deleteTKey() {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyDelete(curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Adds a share description.
     * @param key The key, usually the share index.
     * @param description  Description for the key.
     * @param updateMetadata Whether the metadata is synced immediately or not.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public void addShareDescription(String key, String description, boolean updateMetadata, ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = addShareDescription(key, description, updateMetadata);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> addShareDescription(String key, String description, boolean updateMetadata) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyAddShareDescription(key, description, updateMetadata, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Deletes a share description.
     * @param key The key, usually the share index.
     * @param description  Description for the key.
     * @param updateMetadata Whether the metadata is synced immediately or not.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public void deleteShareDescription(String key, String description, boolean updateMetadata, ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = deleteShareDescription(key, description, updateMetadata);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> deleteShareDescription(String key, String description, boolean updateMetadata) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyDeleteShareDescription(key, description, updateMetadata, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Updates a share description.
     * @param key The key, usually the share index.
     * @param oldDescription Old description used for the key.
     * @param newDescription New description for the key.
     * @param updateMetadata Whether the metadata is synced immediately or not.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public void updateShareDescription(String key, String oldDescription, String newDescription, boolean updateMetadata, ThresholdKeyCallback<Void> callback) {
        executor.execute(() -> {
            try {
                Result<Void> result = updateShareDescription(key, oldDescription, newDescription, updateMetadata);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Void> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Void> updateShareDescription(String key, String oldDescription, String newDescription, boolean updateMetadata) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyUpdateShareDescription(key, oldDescription, newDescription, updateMetadata, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    /**
     * Returns all shares descriptions.
     * @throws RuntimeError Indicates invalid pointer.
     * @throws JSONException Data in object is malformed.
     * @return HashMap
     */
    public HashMap<String, ArrayList<String>> getShareDescriptions() throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetShareDescriptions(error);
        if (error.code != 0) {
            throw error;
        }
        HashMap<String, ArrayList<String>> description = new HashMap<>();
        JSONObject object = new JSONObject(result);
        Iterator<String> iter = object.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            JSONArray values = object.getJSONArray(key);
            ArrayList<String> value = new ArrayList<>();
            for (int i = 0; i < values.length(); i++) {
                value.add(values.getString(i));
            }
            description.put(key, value);
        }
        return description;
    }

    /**
     * Returns share stores for the latest polynomial.
     * @throws RuntimeError Indicates invalid pointer.
     * @return ShareMap
     * @see ShareMap
     */
    public ShareStoreArray getAllAllShareStoresForLatestPolynomial() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGetAllShareStoresForLatestPolynomial(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStoreArray(ptr);
    }

    /**
     * Returns the latest polynomial.
     * @throws RuntimeError Indicates invalid pointer.
     * @return Polynomial
     * @see Polynomial
     */
    public Polynomial reconstructLatestPolynomial() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyReconstructLatestPolynomial(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new Polynomial(ptr);
    }

    public void serviceProviderAssignPublicKey(String tssTag, String nonce, String tssPubKey) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyServiceProviderAssignPublicKey(tssTag,nonce,tssPubKey, error);
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
