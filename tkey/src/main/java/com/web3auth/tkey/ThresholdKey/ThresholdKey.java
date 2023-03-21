package com.web3auth.tkey.ThresholdKey;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;

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
        Executor executor = Executors.newSingleThreadExecutor();
        this.executor = executor;

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

    public void initialize(@Nullable String importShare, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions, final ThresholdKeyCallback<KeyDetails> callback) {
        executor.execute(
                () -> {
                    try {
                        Result<KeyDetails> result = initialize(importShare, input, neverInitializedNewKey, includeLocalMetadataTransitions);
                        callback.onComplete(result);
                    } catch (Exception e) {
                        Result<KeyDetails> error = new Result.Error<>(e);
                        callback.onComplete(error);
                    }
                }
        );
    }

    private Result<KeyDetails> initialize(@Nullable String importShare, @Nullable ShareStore input, boolean neverInitializedNewKey, boolean includeLocalMetadataTransitions) {
        try {
            RuntimeError error = new RuntimeError();
            long ptr = jniThresholdKeyInitialize(importShare, input, neverInitializedNewKey, includeLocalMetadataTransitions, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(new KeyDetails(ptr));
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

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

    public void generateNewShare(ThresholdKeyCallback<GenerateShareStoreResult> callback) {
        executor.execute(() -> {
            try {
                Result<GenerateShareStoreResult> result = generateNewShare();
                callback.onComplete(result);
            } catch (Exception e) {
                Result<GenerateShareStoreResult> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<GenerateShareStoreResult> generateNewShare() {
        try {
            RuntimeError error = new RuntimeError();
            long ptr = jniThresholdKeyGenerateNewShare(curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(new GenerateShareStoreResult(ptr));
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public void deleteShare(String shareIndex, ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = deleteShare(shareIndex);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> deleteShare(String shareIndex) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyDeleteShare(shareIndex, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
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

    public void inputShare(String share, @Nullable String shareType, ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = inputShare(share, shareType);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> inputShare(String share, @Nullable String shareType) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyInputShare(share, shareType, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
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

    public void inputShareStore(ShareStore store, ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = inputShareStore(store);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> inputShareStore(ShareStore store) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyInputShareStore(store, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
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

    public JSONObject getTKeyStoreItem(String moduleName, String id) throws JSONException, RuntimeError {
        RuntimeError error = new RuntimeError();
        String result = jniThresholdKeyGetTKeyStoreItem(moduleName, id, error);
        if (error.code != 0) {
            throw error;
        }
        return new JSONObject(result);
    }

    public void syncLocalMetadataTransitions(ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = syncLocalMetadataTransitions();
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> syncLocalMetadataTransitions() {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeySyncLocalMetadataTransitions(curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
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

    public void deleteTKey(ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = deleteTKey();
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> deleteTKey() {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyDelete(curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public void addShareDescription(String key, String description, boolean updateMetadata, ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = addShareDescription(key, description, updateMetadata);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> addShareDescription(String key, String description, boolean updateMetadata) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyAddShareDescription(key, description, updateMetadata, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public void deleteShareDescription(String key, String description, boolean updateMetadata, ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = deleteShareDescription(key, description, updateMetadata);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> deleteShareDescription(String key, String description, boolean updateMetadata) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyDeleteShareDescription(key, description, updateMetadata, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public void updateShareDescription(String key, String oldDescription, String newDescription, boolean updateMetadata, ThresholdKeyCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Result<Boolean> result = updateShareDescription(key, oldDescription, newDescription, updateMetadata);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private Result<Boolean> updateShareDescription(String key, String oldDescription, String newDescription, boolean updateMetadata) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyUpdateShareDescription(key, oldDescription, newDescription, updateMetadata, curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniThresholdKeyFree();
    }
}
