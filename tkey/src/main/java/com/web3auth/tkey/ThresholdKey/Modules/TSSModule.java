package com.web3auth.tkey.ThresholdKey.Modules;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONObject;
import org.torusresearch.fetchnodedetails.types.NodeDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.torusresearch.torusutils.TorusUtils;
import org.torusresearch.torusutils.types.TorusPublicKey;
import org.torusresearch.torusutils.types.VerifierArgs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class GetTSSPubKeyResult {
    public static class Point {
        public String x;
        public String y;

        public String toFullAddr() {
            return "04" + x + y;
        }

        public Point(String x, String y) {
            this.x = x;
            this.y = y;
        }
    }

    public Point publicKey;
    public List<BigInteger> nodeIndexes;

    public GetTSSPubKeyResult(Point publicKey, List<BigInteger> nodeIndexes) {
        this.publicKey = publicKey;
        this.nodeIndexes = nodeIndexes;
    }
}

public final class TSSModule {
    private TSSModule() {
        //Utility class
    }

    private static native String jniTSSModuleGetTSSPublicKey(ThresholdKey thresholdKey, RuntimeError error);
    
    private static native String jniTSSModuleGetAllTSSTags(ThresholdKey thresholdKey, RuntimeError error);
    
    private static native String jniTSSTagFactorPub(ThresholdKey thresholdKey, RuntimeError error);
    
    private static native String jniGetExtendedVerifier(ThresholdKey thresholdKey, RuntimeError error);

    private static native void jniTSSModuleSetTSSTag(ThresholdKey thresholdKey, String tssTag, RuntimeError error);
    
    private static native String jniTSSModuleGetTSSTag(ThresholdKey thresholdKey, RuntimeError error);
    
    private static native void jniTSSModuleCreateTaggedTSSShare(ThresholdKey thresholdKey, String deviceTssShare, String factorPub, int deviceTssIndex, String curveN, RuntimeError error);
    
    private static native String jniTSSModuleGetTSSShare(ThresholdKey thresholdKey, String factorKey, int threshold, String curveN, RuntimeError error);
    
    private static native int jniGetTSSNonce(ThresholdKey thresholdKey, String tssTag, RuntimeError error);

    private static native void jniCopyFactorPub(ThresholdKey thresholdKey, String newFactorPub, int newTssIndex, String factorPub, String curveN, RuntimeError error);
    
    private static native void jniBackupShareWithFactorKey(ThresholdKey thresholdKey, String shareIndex, String factorKey, String curveN, RuntimeError error);
    
    private static native void jniGenerateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub,  String selectedServers, String authSignatures, String curveN, RuntimeError error);

    private static native void jniDeleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, String factorPub,  String selectedServers, String authSignatures, String curveN, RuntimeError error);

    public static native void jniThresholdKeyServiceProviderAssignPublicKey(ThresholdKey thresholdKey, String tss_tag, String tss_nonce, String tss_public_key, RuntimeError error);

    public static String getTSSPubKey(ThresholdKey thresholdKey, String tssTag) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw error;
        }
        String result = jniTSSModuleGetTSSPublicKey(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

    public static ArrayList<String> getAllTSSTags(ThresholdKey thresholdKey) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetAllTSSTags(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<String> tssTags = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            tssTags.add(jsonArray.getString(i));
        }
        return tssTags;
    }

    public static ArrayList<String> getAllFactorPub(ThresholdKey thresholdKey, String tssTag) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw error;
        }
        String result = jniTSSTagFactorPub(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        ArrayList<String> factorPubs = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            factorPubs.add(jsonArray.getString(i));
        }
        return factorPubs;
    }

    public static String getExtendedVerifier(ThresholdKey thresholdKey) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        String result = jniGetExtendedVerifier(thresholdKey, error);
        if (error.code != 0) {
            throw error;
        }
        return result;
    }

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

    public static GetTSSPubKeyResult getDkgPubKey(ThresholdKey thresholdKey, String tssTag, String nonce, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception, RuntimeError {
        String extendedVerifierId = getExtendedVerifier(thresholdKey);
        String[] split = extendedVerifierId.split("\u001c");
        String extendedVerifierIdFormatted = split[1] + "\u0015" + tssTag + "\u0016" + nonce;

        VerifierArgs verifierArgs = new VerifierArgs(
            split[0],
            split[1],
            extendedVerifierIdFormatted
        );
        TorusPublicKey result = torusUtils.getPublicAddress(nodeDetails.getTorusNodeEndpoints(), nodeDetails.getTorusNodePub(), verifierArgs).get();

        String x = result.getFinalKeyData().getX();
        String y = result.getFinalKeyData().getY();
        List<BigInteger> nodeIndexes = result.getNodesData().getNodeIndexes();
        GetTSSPubKeyResult.Point pubKey = new GetTSSPubKeyResult.Point(x, y);
        return new GetTSSPubKeyResult(pubKey, nodeIndexes);
    }

    public static Pair<String, String> getTSSShare(ThresholdKey thresholdKey, String TSSTag, String factorKey, int threshold) throws RuntimeError {
        if(factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }

        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, TSSTag, error);
        if (error.code != 0) {
            throw error;
        }
        String result = jniTSSModuleGetTSSShare(thresholdKey, factorKey, threshold, thresholdKey.curveN, error);
        String[] splitString = result.split(",", 2);

        if (error.code != 0) {
            throw error;
        }
        return new Pair<>(splitString[0], splitString[1]);
    }

    public static int getTSSNonce(ThresholdKey thresholdKey, String tssTag, Boolean preFetch) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        int nonce = jniGetTSSNonce(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw error;
        }
        if (preFetch) {
            nonce = nonce + 1;
        }
        return nonce;
    }

    public static void copyFactorPub(ThresholdKey thresholdKey, String TSSTag, String factorKey, String newFactorPub, int TSSIndex, ThresholdKeyCallback<Boolean> callback) {
        if (factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }
        setTSSTag(thresholdKey, TSSTag, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to copyFactorPub");
            }
        });

        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = copyFactorPub(thresholdKey, factorKey, newFactorPub, TSSIndex);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Result<Boolean> copyFactorPub(ThresholdKey thresholdKey, String factorKey, String newFactorPub, int tssIndex) {
        try {
            RuntimeError error = new RuntimeError();
            jniCopyFactorPub(thresholdKey, newFactorPub, tssIndex, factorKey, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static Result<Boolean> backupShareWithFactorKey(ThresholdKey thresholdKey, String shareIndex, String factorKey) {
        try {
            RuntimeError error = new RuntimeError();
            jniBackupShareWithFactorKey(thresholdKey, shareIndex, factorKey, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static void createTaggedTSSTagShare(ThresholdKey thresholdKey, String tssTag, String deviceTssShare, String factorPub,
                                               int deviceTssIndex, NodeDetails nodeDetails, TorusUtils torusUtils,
                                               ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        setTSSTag(thresholdKey, tssTag, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            try {
                updateTssPubKey(thresholdKey, tssTag, nodeDetails, torusUtils, false, udpateResult -> {
                    if (udpateResult instanceof Result.Error) {
                        throw new RuntimeException("failed to updateTssPubKey");
                    }
                    thresholdKey.executor.execute(() -> {
                        try {
                            Result<Boolean> createTagResult = createTaggedTSSTagShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex);
                            callback.onComplete(createTagResult);
                        } catch (Exception e) {
                            Result<Boolean> error = new Result.Error<>(e);
                            callback.onComplete(error);
                        }
                    });
                });
            } catch (RuntimeError | Exception e) {
                throw new RuntimeException(e);
            }
        });


    }

    private static Result<Boolean> createTaggedTSSTagShare(ThresholdKey thresholdKey, @Nullable String deviceTssShare, String factorPub, int deviceTssIndex) {
        try {
            RuntimeError error = new RuntimeError();
            jniTSSModuleCreateTaggedTSSShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static void generateTSSShare(ThresholdKey thresholdKey, String TSSTag, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String newFactorPub, int newTssIndex, NodeDetails nodeDetails, TorusUtils torusUtils, @Nullable int[] selectedServers, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        setTSSTag(thresholdKey, TSSTag, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            try {
                updateTssPubKey(thresholdKey, TSSTag, nodeDetails, torusUtils, true, updateResult -> {
                    if (updateResult instanceof Result.Error) {
                        throw new RuntimeException("failed to updateTssPubKey");
                    }
                    thresholdKey.executor.execute(() -> {
                        try {
                            Result<Boolean> generateShareResult = generateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServers, authSignatures);
                            callback.onComplete(generateShareResult);
                        } catch (Exception e) {
                            Result<Boolean> error = new Result.Error<>(e);
                            callback.onComplete(error);
                        }
                    });
                });
            }catch (RuntimeError | Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private static Result<Boolean> generateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub, int[] selectedServers, ArrayList<String> authSignatures) {
        try {
            RuntimeError error = new RuntimeError();
            JSONArray jsonArray = new JSONArray();
            for (String signature: authSignatures) {
                jsonArray.put(signature);
            }
            String authSignaturesString = jsonArray.toString();

            JSONArray jsonServer = new JSONArray();
            if(selectedServers != null) {
                for (int value : selectedServers) {
                    jsonServer.put(value);
                }
            }

            String selectedServersString = null;
            if(jsonServer.length() > 0) {
                selectedServersString = jsonServer.toString();
            }
            jniGenerateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServersString, authSignaturesString, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>((Exception) e);
        }
    }

    // derived additional tss module functionalities
    private static Result<Boolean> deleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String deleteFactorPub, int[] selectedServers) {
        try {
            RuntimeError error = new RuntimeError();
            JSONArray jsonArray = new JSONArray();
            for (String signature: authSignatures) {
                jsonArray.put(signature);
            }
            String authSignaturesString = jsonArray.toString();

            JSONArray jsonServer = new JSONArray();
            if(selectedServers != null) {
                for (int value : selectedServers) {
                    jsonServer.put(value);
                }
            }

            String selectedServersString = null;
            if(jsonServer.length() > 0) {
                selectedServersString = jsonServer.toString();
            }
            jniDeleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, deleteFactorPub, selectedServersString, authSignaturesString, thresholdKey.curveN, error);
            if (error.code != 0) {
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    public static void deleteTSSShare(ThresholdKey thresholdKey, String TSSTag, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String deleteFactorPub,
                                      NodeDetails nodeDetails, TorusUtils torusUtils, int[] selectedServers, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        setTSSTag(thresholdKey, TSSTag, setTagResult -> {
            if (setTagResult instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            try {
                updateTssPubKey(thresholdKey, TSSTag, nodeDetails, torusUtils, true, result1 -> {
                    if (result1 instanceof Result.Error) {
                        throw new RuntimeException("failed to updateTssPubKey");
                    }

                    thresholdKey.executor.execute(() -> {
                        try {
                            Result<Boolean> result = deleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, authSignatures, deleteFactorPub, selectedServers);
                            callback.onComplete(result);
                        } catch (Exception e) {
                            Result<Boolean> error = new Result.Error<>(e);
                            callback.onComplete(error);
                        }
                    });
                });
            } catch (RuntimeError | Exception e) {
                throw new RuntimeException(e);
            }
        });



    }

    public static void updateTssPubKey(ThresholdKey thresholdKey, String tssTag, NodeDetails nodeDetails,
                                       TorusUtils torusUtils, Boolean prefetch, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {

        RuntimeError error = new RuntimeError();
        int nonce = getTSSNonce(thresholdKey, tssTag, prefetch);

        GetTSSPubKeyResult publicAddress = getDkgPubKey(thresholdKey, tssTag, String.valueOf(nonce), nodeDetails, torusUtils);
        JSONObject pubObject = new JSONObject();
        pubObject.put("x", publicAddress.publicKey.x);
        pubObject.put("y", publicAddress.publicKey.y);
        JSONArray nodeIndexArray = new JSONArray();
        for (BigInteger nodeIndex: publicAddress.nodeIndexes) {
            nodeIndexArray.put(nodeIndex);
        }
        JSONObject jsonPubKey = new JSONObject();

        jsonPubKey.put("nodeIndexes", nodeIndexArray);
        jsonPubKey.put("publicKey", pubObject);

        setTSSTag(thresholdKey, tssTag, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            thresholdKey.executor.execute(() -> {
                try {
                    Result<Boolean> updateResult = updateTssPubKey(thresholdKey, tssTag, String.valueOf(nonce), jsonPubKey.toString());
                    callback.onComplete(updateResult);
                } catch (Exception e) {
                    Result<Boolean> error2 = new Result.Error<>((Exception) e);
                    callback.onComplete(error2);
                }
            });
        });
    }

    private static Result<Boolean> updateTssPubKey(ThresholdKey thresholdKey, String tssTag, String nonce, String pubKey) {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, nonce, pubKey, error);
            if (error.code != 0) {
                throw error;
            }
            return new Result.Success<>();
        } catch (Exception | RuntimeError e) {
            return new Result.Error<>((Exception) e);
        }
    }

    public static void AddFactorPub(ThresholdKey thresholdKey, String TSSTag, String factorKey, ArrayList<String> authSignatures, String newFactorPub, int newTssIndex, @Nullable int[] selectedServers, NodeDetails nodeDetails, TorusUtils torusUtils, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        if (factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }
        Pair<String, String> tssShareResult = getTSSShare(thresholdKey, TSSTag, factorKey, 0);
        String tssShare = tssShareResult.second;
        int tssIndex = Integer.parseInt(tssShareResult.first);
        TSSModule.generateTSSShare(thresholdKey, TSSTag, tssShare, tssIndex, authSignatures, newFactorPub, newTssIndex, nodeDetails, torusUtils, selectedServers, generateResult -> {
            if (generateResult instanceof Result.Error) {
                callback.onComplete(generateResult);
            }
            callback.onComplete(new Result.Success<>(true));
        });
    }


    public static void DeleteFactorPub(ThresholdKey thresholdKey, String TSSTag, String factorKey, ArrayList<String> authSignatures, String deleteFactorPub, NodeDetails nodeDetails, TorusUtils torusUtils, @Nullable int[] selectedServers, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        if (factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }
        Pair<String, String> tssShareResult = getTSSShare(thresholdKey, TSSTag, factorKey, 0);
        String tssShare = tssShareResult.second;
        int tssIndex = Integer.parseInt(tssShareResult.first);
        TSSModule.deleteTSSShare(thresholdKey, TSSTag, tssShare, tssIndex, authSignatures, deleteFactorPub, nodeDetails, torusUtils, selectedServers, deleteResult -> {
            if (deleteResult instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
                callback.onComplete(deleteResult);
            }
            callback.onComplete(new Result.Success<>(true));
        });
    }

}
