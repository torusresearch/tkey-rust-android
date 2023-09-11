package com.web3auth.tkey.ThresholdKey.Modules;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.TSSPubKeyResult;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

    private static void setTSSTag(ThresholdKey thresholdKey, String tssTag) throws Exception {
        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
    }

    /**
     * sets the tag for given threshold key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag The tss tag to be set.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void setTSSTag(ThresholdKey thresholdKey, String tssTag, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                setTSSTag(thresholdKey, tssTag);
                callback.onComplete(new Result.Success<>());
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    /**
     * returns the the tss tag.
     * @param thresholdKey The threshold key to act on.
     * @throws Exception Indicates invalid threshold key.
     * @return String
     */
    public static String getTSSTag(ThresholdKey thresholdKey) throws Exception {
        RuntimeError error = new RuntimeError();
        String result = jniTSSModuleGetTSSTag(thresholdKey, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return result;
    }

    /**
     * returns list of all the tss tags for given threshold key.
     * @param thresholdKey The threshold key to act on.
     * @throws RuntimeError Indicates an invalid ThresholdKey.
     * @throws JSONException Indicates library returning invalid json(shouldn't happen ever though)
     * @return ArrayList<String>
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

    private static ArrayList<String> getAllFactorPub(ThresholdKey thresholdKey, String tssTag) throws Exception {
        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        String result = jniTSSTagFactorPub(thresholdKey, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        ArrayList<String> factorPubs = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            factorPubs.add(jsonArray.getString(i));
        }
        return factorPubs;
    }

    /**
     * returns the tss factor public key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag The tssTag to use.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void getAllFactorPub(ThresholdKey thresholdKey, String tssTag, ThresholdKeyCallback<ArrayList<String>> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                ArrayList<String> result = getAllFactorPub(thresholdKey, tssTag);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<ArrayList<String>> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static String getTSSPubKey(ThresholdKey thresholdKey, String tssTag) throws Exception {
        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        String result = jniTSSModuleGetTSSPublicKey(thresholdKey, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        return result;
    }

    /**
     * returns the tss public key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag The tssTag to use.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void getTSSPubKey(ThresholdKey thresholdKey, String tssTag, ThresholdKeyCallback<String> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                String result = getTSSPubKey(thresholdKey, tssTag);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<String> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    /**
     * returns the current tss nonce.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param preFetch Prefetch the nonce of the next public key.
     * @return int
     */
    public static int getTSSNonce(ThresholdKey thresholdKey, String tssTag, Boolean preFetch) throws Exception {
        RuntimeError error = new RuntimeError();
        int nonce = jniGetTSSNonce(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        if (preFetch) {
            nonce = nonce + 1;
        }
        return nonce;
    }

    private static Pair<String, String> getTSSShare(ThresholdKey thresholdKey, String tssTag, String factorKey, int threshold) throws Exception {
        if(factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }

        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        String result = jniTSSModuleGetTSSShare(thresholdKey, factorKey, threshold, thresholdKey.curveN, error);
        String[] splitString = result.split(",", 2);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return new Pair<>(splitString[0], splitString[1]);
    }

    /**
     * returns the tss index as the first pair and tss share as the second pair.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param factorKey A string representing the factor key.
     * @param threshold An integer representing the threshold
     * @param callback The method which the result will be sent to
     */
    public static void getTSSShare(ThresholdKey thresholdKey, String tssTag, String factorKey, int threshold, ThresholdKeyCallback<Pair<String, String>> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Pair<String, String> result = getTSSShare(thresholdKey, tssTag, factorKey, threshold);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<Pair<String, String>> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Boolean createTaggedTSSTagShare(ThresholdKey thresholdKey, @Nullable String deviceTssShare, String factorPub,
                                                           int deviceTssIndex, String tssTag, Boolean prefetch, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception {
        RuntimeError error = new RuntimeError();

        // set tss tag
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // update tss pub key
        Pair<Integer, String> updateParams = getUpdateParams(thresholdKey, tssTag, prefetch, nodeDetails, torusUtils);
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, String.valueOf(updateParams.first), updateParams.second, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // create tagged tss share
        jniTSSModuleCreateTaggedTSSShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }

    /**
     * This function creates a tagged tss share.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param deviceTssShare A string representing the device tss share.
     * @param factorPub A string representing the factor public key.
     * @param deviceTssIndex An integer representing the device index.
     * @param nodeDetails A NodeDetailsModel object representing the node details..
     * @param torusUtils A TorusUtils object to be used.
     * @param callback The method which the result will be sent to
     */
    public static void createTaggedTSSTagShare(ThresholdKey thresholdKey, String tssTag, String deviceTssShare, String factorPub,
                                            int deviceTssIndex, NodeDetails nodeDetails, TorusUtils torusUtils,
                                            ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Boolean createTagResult = createTaggedTSSTagShare(thresholdKey, deviceTssShare, factorPub, deviceTssIndex, tssTag, false, nodeDetails, torusUtils);
                callback.onComplete(new Result.Success<>(createTagResult));
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Boolean updateTssPubKey(ThresholdKey thresholdKey, String tssTag, String nonce, String pubKey) throws Exception {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, nonce, pubKey, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }

    /**
     * This function update a tss pub key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param nodeDetails An NodeDetails object representing the node details.
     * @param torusUtils A TorusUtils object to be used.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void updateTssPubKey(ThresholdKey thresholdKey, String tssTag, NodeDetails nodeDetails,
                                       TorusUtils torusUtils, Boolean prefetch, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                int nonce = getTSSNonce(thresholdKey, tssTag, prefetch);

                TSSPubKeyResult publicAddress = getDkgPubKey(thresholdKey, tssTag, String.valueOf(nonce), nodeDetails, torusUtils);
                JSONObject pubObject = new JSONObject();
                pubObject.put("x", publicAddress.publicKey.x);
                pubObject.put("y", publicAddress.publicKey.y);
                JSONArray nodeIndexArray = new JSONArray();
                for (Integer nodeIndex: publicAddress.nodeIndexes) {
                    nodeIndexArray.put(nodeIndex);
                }
                JSONObject jsonPubKey = new JSONObject();

                jsonPubKey.put("nodeIndexes", nodeIndexArray);
                jsonPubKey.put("publicKey", pubObject);

                RuntimeError error = new RuntimeError();
                jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
                if (error.code != 0) {
                    throw new Exception(error);
                }

                Boolean updateResult = updateTssPubKey(thresholdKey, tssTag, String.valueOf(nonce), jsonPubKey.toString());
                callback.onComplete(new Result.Success<>(updateResult));
            } catch (Exception e) {
                Result<Boolean> error2 = new Result.Error<>(e);
                callback.onComplete(error2);
            }
        });
    }

    private static Boolean copyFactorPub(ThresholdKey thresholdKey, String tssTag, String factorKey, String newFactorPub, int tssIndex) throws Exception {
        RuntimeError error = new RuntimeError();
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        jniCopyFactorPub(thresholdKey, newFactorPub, tssIndex, factorKey, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }

    /**
     * This function copies a factor public key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param factorKey A string representing the factor key.
     * @param newFactorPub A string representing the new factor public key.
     * @param tssIndex An integer representing the tss index.
     * @param callback The method which the result will be sent to
     */
    public static void copyFactorPub(ThresholdKey thresholdKey, String tssTag, String factorKey, String newFactorPub, int tssIndex, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                if (factorKey.length() > 66) {
                    throw new RuntimeException("Invalid factor Key");
                }
                Boolean result = copyFactorPub(thresholdKey, tssTag, factorKey, newFactorPub, tssIndex);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Boolean generateTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, int newTssIndex, String newFactorPub
    , int[] selectedServers, ArrayList<String> authSignatures, String tssTag, Boolean prefetch, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception {
        RuntimeError error = new RuntimeError();

        // set tss tag
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // update tss pub key
        Pair<Integer, String> updateParams = getUpdateParams(thresholdKey, tssTag, prefetch, nodeDetails, torusUtils);
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, String.valueOf(updateParams.first), updateParams.second, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // generate tss share
        Pair<String, String> sigParams = getServerParams(authSignatures, selectedServers);
        jniGenerateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, sigParams.second, sigParams.first, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }
    
    /**
     * This function generates a tss share.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param inputTssShare A string representing the Input TSS Share.
     * @param inputTssIndex A integer representing the Input tss index.
     * @param authSignatures An array of strings representing the auth signatures.
     * @param newFactorPub A string representing the new factor public key.
     * @param newTssIndex An integer representing the new tss index.
     * @param nodeDetails An NodeDetails object representing the node details.
     * @param torusUtils A TorusUtils object to be used.
     * @param selectedServers An integer representing the new tss index.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void generateTSSShare(ThresholdKey thresholdKey, String tssTag, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String newFactorPub, int newTssIndex, NodeDetails nodeDetails, TorusUtils torusUtils, @Nullable int[] selectedServers, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Boolean generateShareResult = generateTSSShare(thresholdKey, inputTssShare, inputTssIndex, newTssIndex, newFactorPub, selectedServers, authSignatures, tssTag, true, nodeDetails, torusUtils);
                callback.onComplete(new Result.Success<>(generateShareResult));
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Boolean deleteTSSShare(ThresholdKey thresholdKey, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String deleteFactorPub, int[] selectedServers
                                                    ,String tssTag, Boolean prefetch, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception {
        RuntimeError error = new RuntimeError();

        // set tss tag
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // update tss pub key
        Pair<Integer, String> updateParams = getUpdateParams(thresholdKey, tssTag, prefetch, nodeDetails, torusUtils);
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, String.valueOf(updateParams.first), updateParams.second, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // generate tss share
        Pair<String, String> sigParams = getServerParams(authSignatures, selectedServers);
        jniDeleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, deleteFactorPub, sigParams.second, sigParams.first, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }

    /**
     * This function deletes a tss share.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param inputTssShare A string representing the Input TSS Share.
     * @param inputTssIndex A integer representing the Input tss index.
     * @param authSignatures An array of strings representing the auth signatures.
     * @param deleteFactorPub A string representing the factor public key to be deleted.
     * @param nodeDetails An NodeDetails object representing the node details.
     * @param torusUtils A TorusUtils object to be used.
     * @param selectedServers An integer representing the new tss index.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void deleteTSSShare(ThresholdKey thresholdKey, String tssTag, String inputTssShare, int inputTssIndex, ArrayList<String> authSignatures, String deleteFactorPub,
                                      NodeDetails nodeDetails, TorusUtils torusUtils, int[] selectedServers, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Boolean result = deleteTSSShare(thresholdKey, inputTssShare, inputTssIndex, authSignatures, deleteFactorPub, selectedServers, tssTag, true, nodeDetails, torusUtils);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    private static Boolean AddFactorPub(ThresholdKey thresholdKey, int newTssIndex, String newFactorPub
            , int[] selectedServers, ArrayList<String> authSignatures, String tssTag, Boolean prefetch, NodeDetails nodeDetails,
        TorusUtils torusUtils, String factorKey, int threshold) throws Exception {
        RuntimeError error = new RuntimeError();

        // set tss tag
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // getTSSShare
        if (factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }

        String result = jniTSSModuleGetTSSShare(thresholdKey, factorKey, threshold, thresholdKey.curveN, error);
        String[] splitString = result.split(",", 2);
        if (error.code != 0) {
            throw new Exception(error);
        }
        String tssShare = splitString[1];
        int tssIndex = Integer.parseInt(splitString[0]);


        // update tss pub key
        Pair<Integer, String> updateParams = getUpdateParams(thresholdKey, tssTag, prefetch, nodeDetails, torusUtils);
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, String.valueOf(updateParams.first), updateParams.second, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // generate tss share
        Pair<String, String> sigParams = getServerParams(authSignatures, selectedServers);
        jniGenerateTSSShare(thresholdKey, tssShare, tssIndex, newTssIndex, newFactorPub, sigParams.second, sigParams.first, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }

    /**
     * This function adds a factor public key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param factorKey A string representing the factor key.
     * @param authSignatures An array of strings representing the auth signatures.
     * @param newFactorPub A string representing the new factor public key.
     * @param newTssIndex An integer representing the new tss index.
     * @param selectedServers An integer representing the new tss index.
     * @param nodeDetails An NodeDetails object representing the node details.
     * @param torusUtils A TorusUtils object to be used.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void AddFactorPub(ThresholdKey thresholdKey, String tssTag, String factorKey,
                                    ArrayList<String> authSignatures, String newFactorPub, int newTssIndex,
                                    @Nullable int[] selectedServers, NodeDetails nodeDetails, TorusUtils torusUtils,
                                    ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                Boolean result = AddFactorPub(thresholdKey, newTssIndex, newFactorPub, selectedServers, authSignatures, tssTag, true, nodeDetails, torusUtils, factorKey, 0);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }


    private static Boolean DeleteFactorPub(ThresholdKey thresholdKey, String tssTag, String factorKey,
                                                   ArrayList<String> authSignatures, String deleteFactorPub,
                                                   NodeDetails nodeDetails, TorusUtils torusUtils,
                                                   @Nullable int[] selectedServers, int threshold, Boolean prefetch) throws Exception {
        RuntimeError error = new RuntimeError();

        // set tss tag
        jniTSSModuleSetTSSTag(thresholdKey, tssTag, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // getTSSShare
        if (factorKey.length() > 66) {
            throw new RuntimeException("Invalid factor Key");
        }

        String result = jniTSSModuleGetTSSShare(thresholdKey, factorKey, threshold, thresholdKey.curveN, error);
        String[] splitString = result.split(",", 2);
        if (error.code != 0) {
            throw new Exception(error);
        }
        String tssShare = splitString[1];
        int tssIndex = Integer.parseInt(splitString[0]);

        // update tss pub key
        Pair<Integer, String> updateParams = getUpdateParams(thresholdKey, tssTag, prefetch, nodeDetails, torusUtils);
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, String.valueOf(updateParams.first), updateParams.second, error);
        if (error.code != 0) {
            throw new Exception(error);
        }

        // generate tss share
        Pair<String, String> sigParams = getServerParams(authSignatures, selectedServers);
        jniDeleteTSSShare(thresholdKey, tssShare, tssIndex, deleteFactorPub, sigParams.second, sigParams.first, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return true;
    }

    /**
     * This function deletes a factor public key.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param factorKey A string representing the factor key.
     * @param authSignatures An array of strings representing the auth signatures.
     * @param deleteFactorPub A string representing the delete factor public key.
     * @param nodeDetails An NodeDetails object representing the node details.
     * @param torusUtils A TorusUtils object to be used.
     * @param selectedServers An integer representing the new tss index.
     * @param callback The method which the result will be sent to
     * @see ThresholdKeyCallback
     */
    public static void DeleteFactorPub(ThresholdKey thresholdKey, String tssTag, String factorKey, ArrayList<String> authSignatures, String deleteFactorPub, NodeDetails nodeDetails, TorusUtils torusUtils, @Nullable int[] selectedServers, ThresholdKeyCallback<Boolean> callback) {
        thresholdKey.executor.execute(() -> {
            try {
                if (factorKey.length() > 66) {
                    throw new RuntimeException("Invalid factor Key");
                }
                Boolean result = DeleteFactorPub(thresholdKey, tssTag, factorKey, authSignatures, deleteFactorPub, nodeDetails, torusUtils, selectedServers, 0, true);
                callback.onComplete(new Result.Success<>(result));
            } catch (Exception e) {
                Result<Boolean> error = new Result.Error<>(e);
                callback.onComplete(error);
            }
        });
    }

    /**
     * This function backup device share with factor key.
     * @param thresholdKey The threshold key to act on.
     * @param shareIndex   The threshold key to act on.
     * @param factorKey    A string representing the factor key.
     * @throws Exception   Indicates an invalid ThresholdKey or invalid parameters.
     */
    public static void backupShareWithFactorKey(ThresholdKey thresholdKey, String shareIndex, String factorKey) throws Exception {
        RuntimeError error = new RuntimeError();
        jniBackupShareWithFactorKey(thresholdKey, shareIndex, factorKey, thresholdKey.curveN, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
    }

    // todo: replace see with torus utils and node details model once docs are added for those libraries
    /**
     * returns the the Dkg PublicKey.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag .
     * @param nonce A string representing the nonce.
     * @param nodeDetails A NodeDetailsModel object representing the node details..
     * @param torusUtils A TorusUtils object to be used.
     * @throws Exception Indicates invalid threshold key or params being passed.
     * @return TSSPubKeyResult
     * @see TSSPubKeyResult
     */
    public static TSSPubKeyResult getDkgPubKey(ThresholdKey thresholdKey, String tssTag, String nonce, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception {
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
        List<Integer> nodeIndexList = nodeIndexes.stream()
                .map(BigInteger::intValue)
                .collect(Collectors.toList());

        TSSPubKeyResult.Point pubKey = new TSSPubKeyResult.Point(x, y);
        return new TSSPubKeyResult(pubKey, nodeIndexList);
    }
    
    /**
     * returns the the Device Share Index.
     * @param thresholdKey The threshold key to act on.
     * @param factorKey A string representing the TSS tag .
     * @throws Exception Indicates invalid threshold key or factor key being passed.
     * @return String
     */
    public static String findDeviceShareIndex(ThresholdKey thresholdKey, String factorKey) throws Exception {
        AtomicReference<String> resultData = new AtomicReference<>("");
        AtomicReference<String> shareIndex = new AtomicReference<>("");

        thresholdKey.storage_layer_get_metadata(factorKey, result -> {
            resultData.set(((Result.Success<String>) result).data);
            JSONObject resultJson = null;
            try {
                resultJson = new JSONObject(resultData.get());

                JSONObject deviceShareJson = resultJson.getJSONObject("deviceShare");
                JSONObject shareJson = deviceShareJson.getJSONObject("share");
                shareIndex.set(shareJson.get("shareIndex").toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return shareIndex.get();
    }

    /**
     * returns the extended verifier id (includes verifier and verifier-id with delimiters).
     * @param thresholdKey The threshold key to act on.
     * @throws Exception Indicates an invalid ThresholdKey.
     * @return String
     */
    public static String getExtendedVerifier(ThresholdKey thresholdKey) throws Exception {
        RuntimeError error = new RuntimeError();
        String result = jniGetExtendedVerifier(thresholdKey, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
        return result;
    }

    /**
     * This function assign a tss pub key to service provider.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param pubKey A JSON representation of public key.
     * @throws Exception Indicates an invalid thresholdKey or invalid parameters
     */
    public static void serviceProviderAssignPublicKey(ThresholdKey thresholdKey, String tssTag, String nonce, String pubKey) throws Exception {
        RuntimeError error = new RuntimeError();
        jniThresholdKeyServiceProviderAssignPublicKey(thresholdKey, tssTag, nonce, pubKey, error);
        if (error.code != 0) {
            throw new Exception(error);
        }
    }

    /**
     * This function assign a tss pub key to service provider.
     * @param thresholdKey The threshold key to act on.
     * @param tssTag A string representing the TSS tag.
     * @param jsonPubKey A JSON representation of public key.
     * @throws Exception Indicates an invalid thresholdKey or invalid parameters
     */
    public static void serviceProviderAssignPublicKey(ThresholdKey thresholdKey, String tssTag, Integer nonce, String jsonPubKey) throws Exception {
            serviceProviderAssignPublicKey(thresholdKey, tssTag, String.valueOf(nonce), jsonPubKey);
    }

    private static Pair<Integer, String> getUpdateParams(ThresholdKey thresholdKey, String tssTag, Boolean prefetch, NodeDetails nodeDetails, TorusUtils torusUtils) throws Exception {
        int nonce = getTSSNonce(thresholdKey, tssTag, prefetch);
        TSSPubKeyResult publicAddress = getDkgPubKey(thresholdKey, tssTag, String.valueOf(nonce), nodeDetails, torusUtils);
        JSONObject pubObject = new JSONObject();
        pubObject.put("x", publicAddress.publicKey.x);
        pubObject.put("y", publicAddress.publicKey.y);
        JSONArray nodeIndexArray = new JSONArray();
        for (Integer nodeIndex: publicAddress.nodeIndexes) {
            nodeIndexArray.put(nodeIndex);
        }
        JSONObject jsonPubKey = new JSONObject();
        jsonPubKey.put("nodeIndexes", nodeIndexArray);
        jsonPubKey.put("publicKey", pubObject);
        return new Pair<>(nonce, jsonPubKey.toString());
    }

    private static Pair<String, String> getServerParams(ArrayList<String> authSignatures, @Nullable int[] selectedServers) {
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
        return new Pair<>(authSignaturesString, selectedServersString);
    }
}
