package com.web3auth.tkey.ThresholdKey.Modules;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ThresholdKeyCallback;
import com.web3auth.tkey.ThresholdKey.Metadata;
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
    // static NodeDetails nodeDetails;
    // static TorusUtils torusUtils;
    // static String tssTag;
    // static ThresholdKey thresholdKey;
    // static [String: GetTSSPubKeyResult] assignedTssPubKey;
    public static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

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
    
    private static native void jniGenerateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub,  String selectedServers, String authSignatures, String curveN, RuntimeError error);

    private static native void jniDeleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, String factorPub,  String selectedServers, String authSignatures, String curveN, RuntimeError error);

    public static native void jniThresholdKeyServiceProviderAssignPublicKey(ThresholdKey thresholdKey, String tss_tag, String tss_nonce, String tss_public_key, RuntimeError error);


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

    // public TSSModule(ThresholdKey thresholdKey, String tssTag, TorusUtils torusUtils, NodeDetails nodeDetails) throws RuntimeError, Exception {
    //     this.thresholdKey = thresholdKey;
    //     this.tssTag = tssTag;
    //     this.nodeDetails = nodeDetails;
    //     this.torusUtils = torusUtils;
    //     setTSSTag(tssTag);
    //     updateTssPubKey(false);
    // }

    // todo: guru: move getAllTSSTags and getExtendedVerifier from tss module to tkey
    /**
     * Returns the seed phrases stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid threshold key.
     * @return String
     */
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

    /**
     * Returns the seed phrases stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid threshold key.
     * @return String
     */
    public static ArrayList<String> getAllFactorPub(ThresholdKey thresholdKey) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
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

    /**
     * Returns the seed phrases stored on a ThresholdKey object.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates invalid threshold key.
     * @return String
     */
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
                System.out.println("set tss tsg");
                System.out.println(result.toString());
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

    public static void createTaggedTSSTagShare(ThresholdKey thresholdKey, String tssTag, String deviceTssShare, String factorPub,
                                               int deviceTssIndex, NodeDetails nodeDetails, TorusUtils torusUtils,
                                               ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        setTSSTag(thresholdKey, tssTag, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            System.out.println("set tss create complete");
        });
        updateTssPubKey(thresholdKey, tssTag, nodeDetails, torusUtils, false, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            System.out.println("updating tss pub key");
            thresholdKey.executor.execute(() -> {
                try {

                    Result<Boolean> result2 = createTaggedTSSTagShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex);
                    System.out.println(result2.toString());
                    System.out.println("updated tss pub key");
                    callback.onComplete(result2);
                } catch (Exception e) {
                    Result<Boolean> error = new Result.Error<>(e);
                    callback.onComplete(error);
                }
            });
        });

    }

    private static Result<Boolean> createTaggedTSSTagShare(ThresholdKey thresholdKey, @Nullable String deviceTssShare, String factorPub, int deviceTssIndex) {
        try {
            RuntimeError error = new RuntimeError();
            jniTSSModuleCreateTaggedTSSShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex, curveN, error);
            if (error.code != 0) {
                System.out.println(error.toString());
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }

    // todo: replace threshold with default params
    public static Pair<String, String> getTSSShare(ThresholdKey thresholdKey, String TSSTag, String factorKey, int threshold) throws RuntimeError {
        if(factorKey.length() > 64) {
            throw new RuntimeException("Invalid factor Key");
        }
        setTSSTag(thresholdKey, TSSTag);

        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetTSSShare(thresholdKey, factorKey, threshold, thresholdKey.curveN, error);
        System.out.println("result");
        System.out.println(result);
        String[] splitString = result.split(",", 3);

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
        setTSSTag(thresholdKey, TSSTag);

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

    // todo: convert selectedServers from string to int[] and format approp.
    public static void generateTSSShare(ThresholdKey thresholdKey, String TSSTag, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String newFactorPub, int newTssIndex, NodeDetails nodeDetails, TorusUtils torusUtils, @Nullable int[] selectedServers, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
        setTSSTag(thresholdKey, TSSTag, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
        });
        System.out.println("pre updateTssPubKey generate result");
        updateTssPubKey(thresholdKey, TSSTag, nodeDetails, torusUtils, true, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
            System.out.println("completion updateTssPubKey generate result");
            thresholdKey.executor.execute(() -> {
                try {
                    Result<Boolean> result2 = generateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServers, authSignatures);
                    System.out.println("public generate result");
                    System.out.println(result2.toString());
                    callback.onComplete(result2);
                } catch (Exception e) {
                    Result<Boolean> error = new Result.Error<>(e);
                    callback.onComplete(error);
                }
            });
        });
        System.out.println("post updateTssPubKey generate result");

    }

    private static Result<Boolean> generateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub, int[] selectedServers, ArrayList<String> authSignatures) {
        try {
            RuntimeError error = new RuntimeError();
            JSONArray jsonArray = new JSONArray();
            for (String signature: authSignatures) {
                jsonArray.put(signature);
            }
            String authSignaturesString = jsonArray.toString();
            System.out.println("generateTSSShare code");

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
            System.out.println(inputTssShare);
            System.out.println(newTssIndex);
            System.out.println(newFactorPub);
            System.out.println(selectedServersString);
            System.out.println(authSignaturesString);
            System.out.println(thresholdKey.curveN);
            Metadata metadata = thresholdKey.getMetadata();
            System.out.println(metadata);
//            System.out.println(jsonServer.length());
            jniGenerateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServersString, authSignaturesString, curveN, error);
            if (error.code != 0) {
                System.out.println("error code");
                System.out.println(error.code);
                throw new Exception(error);
            }
            return new Result.Success<>(true);
        } catch (Exception | RuntimeError e) {
            return new Result.Error<>((Exception) e);
        }
    }

    // derived additional tss module functionalities
    private static Result<Boolean> deleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String deleteFactorPub, int[] selectedServers) {
        try {
            RuntimeError error = new RuntimeError();
            JSONArray jsonArray = new JSONArray(authSignatures);
            String authSignaturesString = jsonArray.toString();

            JSONArray jsonServer = new JSONArray();
            for (int value : selectedServers) {
                jsonServer.put(value);
            }
            String selectedServersString = jsonServer.toString();

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
        setTSSTag(thresholdKey, TSSTag);

        updateTssPubKey(thresholdKey, TSSTag, nodeDetails, torusUtils, true, result -> {
            if (result instanceof Result.Error) {
                throw new RuntimeException("failed to set TSS Tag");
            }
        });
        thresholdKey.executor.execute(() -> {
            try {
                Result<Boolean> result = deleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, authSignatures, deleteFactorPub, selectedServers);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    // todo: prefetch boolean should be optional param with default param false
    public static void updateTssPubKey(ThresholdKey thresholdKey, String tssTag, NodeDetails nodeDetails,
                                       TorusUtils torusUtils, Boolean prefetch, ThresholdKeyCallback<Boolean> callback) throws RuntimeError, Exception {
            setTSSTag(thresholdKey, tssTag);
            RuntimeError error = new RuntimeError();
            int nonce = getTSSNonce(thresholdKey, tssTag, prefetch);
            System.out.println("nonce");
            System.out.println(nonce);

            GetTSSPubKeyResult publicAddress = getTssPubAddress(thresholdKey, tssTag, String.valueOf(nonce), nodeDetails, torusUtils);
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
            System.out.println(pubObject.toString());

//            jsonPubKey.put("nodeIndexes", publicAddress.nodeIndexes);
//            jsonPubKey.put("publicKey", pubObject);
            System.out.println(jsonPubKey.toString());
            thresholdKey.executor.execute(() -> {
                try {
                    Result<Boolean> result = updateTssPubKey(thresholdKey, tssTag, String.valueOf(nonce), jsonPubKey.toString());
                    System.out.println("updateTssPubKey");
                    System.out.println(result.toString());
                    callback.onComplete(result);
                } catch (Exception e) {
                    Result<Boolean> error2 = new Result.Error<>((Exception) e);
                    System.out.println("error");
                    System.out.println(error2);
                    callback.onComplete(error2);
                }
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


    private static Result<Void> serviceProviderAssignPublicKey(ThresholdKey thresholdKey, String tssTag, String nonce, String tssPubKey) throws RuntimeError {
        try {
            RuntimeError error = new RuntimeError();
            jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, nonce, tssPubKey, error);
            if (error.code != 0) {
                throw error;
            }
            return new Result.Success<>();
        } catch (Exception e) {
            return new Result.Error<>(e);
        }
    }
    public void serviceProviderAssignPublicKey(ThresholdKey thresholdKey, String tssTag, String nonce, String tssPubKey, ThresholdKeyCallback<Void> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Result<Void> result = serviceProviderAssignPublicKey(thresholdKey, tssTag, nonce, tssPubKey);
                callback.onComplete(result);
            } catch (Exception | RuntimeError e) {
                Result<Void> error = new Result.Error<>((Exception) e);
                callback.onComplete(error);
            }
        });
    }


//    private GetTSSPubKeyResult doInBackground(Void... voids) {
//        String extendedVerifierId = thresholdKey.getEx();
//    }

    public static void AddFactorPub(ThresholdKey thresholdKey, String TSSTag, String factorKey, ArrayList<String> authSignatures, String newFactorPub, int newTssIndex, @Nullable int[] selectedServers, NodeDetails nodeDetails, TorusUtils torusUtils) throws RuntimeError, Exception {
        if (factorKey.length() > 64) {
            throw new RuntimeException("Invalid factor Key");
        }
        setTSSTag(thresholdKey, TSSTag);
        Pair<String, String> tssShareResult = getTSSShare(thresholdKey, TSSTag, factorKey, 0);
        String tssShare = tssShareResult.second;
        int tssIndex = Integer.parseInt(tssShareResult.first);
        TSSModule.generateTSSShare(thresholdKey, TSSTag, tssShare, tssIndex, authSignatures, newFactorPub, newTssIndex, nodeDetails, torusUtils, selectedServers, generateResult -> {
            if (generateResult instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
                // Error handling here
                throw new RuntimeException("Generation Failed");
            }
        });
    }


    public static void DeleteFactorPub(ThresholdKey thresholdKey, String TSSTag, String factorKey, ArrayList<String> authSignatures, String deleteFactorPub, NodeDetails nodeDetails, TorusUtils torusUtils, @Nullable int[] selectedServers) throws RuntimeError, Exception {
        if (factorKey.length() > 64) {
            throw new RuntimeException("Invalid factor Key");
        }
        setTSSTag(thresholdKey, TSSTag);
        Pair<String, String> tssShareResult = getTSSShare(thresholdKey, TSSTag, factorKey, 0);
        String tssShare = tssShareResult.second;
        int tssIndex = Integer.parseInt(tssShareResult.first);
        TSSModule.deleteTSSShare(thresholdKey, TSSTag, tssShare, tssIndex, authSignatures, deleteFactorPub, nodeDetails, torusUtils, selectedServers, generateResult -> {
            if (generateResult instanceof com.web3auth.tkey.ThresholdKey.Common.Result.Error) {
                // Error handling here
                throw new RuntimeException("Generation Failed");
            }
        });
    }


    public static GetTSSPubKeyResult getTssPubAddress(ThresholdKey thresholdKey, String tssTag, String nonce, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception, RuntimeError {
        String extendedVerifierId = getExtendedVerifier(thresholdKey);
        String[] split = extendedVerifierId.split("\u001c");
        System.out.println("extendedVerifierId");
        System.out.println(extendedVerifierId);
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
        System.out.println("pubKey");
        System.out.println(pubKey.x.toString());
        return new GetTSSPubKeyResult(pubKey, nodeIndexes);
    }

}
