package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

import java.util.ArrayList;

public final class KeyReconstructionDetails {
    private native String jniKeyReconstructionDetailsGetPrivateKey(RuntimeError error);

    private native int jniKeyReconstructionDetailsGetSeedPhraseLen(RuntimeError error);

    private native String jniKeyReconstructionDetailsGetSeedPhraseAt(int index, RuntimeError error);

    private native int jniKeyReconstructionDetailsGetAllKeysLen(RuntimeError error);

    private native String jniKeyReconstructionDetailsGetAllKeysAt(int index, RuntimeError error);

    private native void jniKeyReconstructionDetailsFree();

    final long pointer;

    public KeyReconstructionDetails(long ptr) {
        pointer = ptr;
    }

    public String getKey() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        String key = jniKeyReconstructionDetailsGetPrivateKey(error);
        if (error.code != 0) {
            throw error;
        }
        return key;
    }

    public ArrayList<String> getSeedPhrase() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int len = jniKeyReconstructionDetailsGetSeedPhraseLen(error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String key = jniKeyReconstructionDetailsGetSeedPhraseAt(i, error);
            if (error.code != 0) {
                throw error;
            }
            list.add(key);
        }
        return list;
    }

    public ArrayList<String> getAllKeys() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int len = jniKeyReconstructionDetailsGetAllKeysLen(error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String key = jniKeyReconstructionDetailsGetAllKeysAt(i, error);
            if (error.code != 0) {
                throw error;
            }
            list.add(key);
        }
        return list;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniKeyReconstructionDetailsFree();
    }
}
