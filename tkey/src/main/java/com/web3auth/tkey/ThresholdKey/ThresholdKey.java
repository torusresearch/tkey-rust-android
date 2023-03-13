package com.web3auth.tkey.ThresholdKey;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public final class ThresholdKey {
    final long pointer;
    public String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    private native long jniThresholdKey(@Nullable Metadata metadata, @Nullable ShareStorePolyIdIndexMap shares, StorageLayer storageLayer, @Nullable ServiceProvider serviceProvider, @Nullable LocalMetadataTransitions localTransitions, @Nullable Metadata lastFetchedCloudMetadata, boolean enableLogging, boolean manualSync, RuntimeError error);

    private native long jniThresholdKeyGetMetadata(RuntimeError error);

    private native long jniThresholdKeyInitialize(@Nullable String share, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions, String curveN, RuntimeError error);

    private native long jniThresholdKeyReconstruct(String curveN, RuntimeError error);

    private native long jniThresholdKeyGenerateNewShare(String curveN, RuntimeError error);

    private native void jniThresholdKeyDeleteShare(String shareIndex, String curveN, RuntimeError error);

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

    private native void jniThresholdKeyFree();

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
        long ptr = jniThresholdKeyInitialize(importShare, input, neverInitializedNewKey, includeLocalMetadataTransitions, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyDetails(ptr);
    }

    public KeyReconstructionDetails reconstruct() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyReconstruct(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new KeyReconstructionDetails(ptr);
    }

    public GenerateShareStoreResult generateNewShare() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniThresholdKeyGenerateNewShare(curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new GenerateShareStoreResult(ptr);
    }

    public void deleteShare(String shareIndex) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyDeleteShare(shareIndex, curveN, error);
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
        String result = jniThresholdKeyOutputShare(shareIndex, shareType, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public ShareStore shareToShareStore(String share) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyShareToShareStore(share, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    public void inputShare(String share, @Nullable String shareType) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyInputShare(share, shareType, curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public ShareStore outputShareStore(String shareIndex, @Nullable String polyId) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyOutputShareStore(shareIndex, polyId, curveN, error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStore(result);
    }

    public void inputShareStore(ShareStore store) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyInputShareStore(store, error);
        if (error.code != 0) {
            throw error;
        }
    }

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

    public ArrayList<HashMap<String, Object>> getTKeyStore(String moduleName) throws RuntimeError, JsonProcessingException {
        RuntimeError error = new RuntimeError();
        String jsonString = jniThresholdKeyGetTKeyStore(moduleName, error);
        if (error.code != 0) {
            throw error;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        // Use TypeReference to parse the JSON string into a HashMap
        ArrayList<HashMap<String, Object>> result =
                objectMapper.readValue(jsonString,
                        new TypeReference<ArrayList<HashMap<String,Object>>>() {});
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
        jniThresholdKeySyncLocalMetadataTransitions(curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public ShareStorePolyIdIndexMap getShares() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long result = jniThresholdKeyGetShares(error);
        if (error.code != 0) {
            throw error;
        }
        return new ShareStorePolyIdIndexMap(result);
    }

    public void deleteTKey() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyDelete(curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public void addShareDescription(String key, String description, boolean updateMetadata) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyAddShareDescription(key,description,updateMetadata,curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public void deleteShareDescription(String key, String description, boolean updateMetadata) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyDeleteShareDescription(key,description,updateMetadata,curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public void updateShareDescription(String key, String oldDescription, String newDescription, boolean updateMetadata) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyUpdateShareDescription(key,oldDescription,newDescription,updateMetadata,curveN, error);
        if (error.code != 0) {
            throw error;
        }
    }

    public String getShareDescriptions() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetShareDescriptions(error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniThresholdKeyFree();
    }
}
