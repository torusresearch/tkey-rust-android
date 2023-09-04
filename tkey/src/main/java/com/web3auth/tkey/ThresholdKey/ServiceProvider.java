package com.web3auth.tkey.ThresholdKey;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.torusresearch.fetchnodedetails.types.TorusNodePub;
import org.torusresearch.fetchnodedetails.types.NodeDetails;
import com.web3auth.tkey.ThresholdKey.Common.TKeyNodeDetails;

public final class ServiceProvider {
    private native void jniServiceProviderFree();
    private native long jniServiceProvider(boolean enableLogging, String postboxKey, String curveN, boolean useTss, @Nullable String verifierName, @Nullable String verifierId, @Nullable TKeyNodeDetails sss, @Nullable TKeyNodeDetails tss, @Nullable TKeyNodeDetails rss, RuntimeError error);

    final static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    final long pointer;

    /**
     * Instantiates a ServiceProvider object.
     * @param enableLogging Determines whether logging is enabled or not.
     * @param postboxKey The private key to be used for the ServiceProvider.
     * @param useTss Whether to use TSS or not.
     * @param verifierName Verifier Name, required for TSS.
     * @param verifierId Verifier ID, required for TSS.
     * @param nodeDetails Node Details for fetching sss, rss, tss infos.
     * @throws RuntimeError Indicates invalid parameters were used.
     * @see NodeDetails
     */
    public ServiceProvider(boolean enableLogging, String postboxKey, boolean useTss, @Nullable String verifierName, @Nullable String verifierId, @Nullable NodeDetails nodeDetails) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        TKeyNodeDetails sss = null;
        TKeyNodeDetails rss = null;
        TKeyNodeDetails tss = null;

        if(nodeDetails != null) {
            String[] sssEndpoints = nodeDetails.getTorusNodeSSSEndpoints();
            String[] rssEndpoints = nodeDetails.getTorusNodeRSSEndpoints();
            String[] tssEndpoints = nodeDetails.getTorusNodeTSSEndpoints();

            TorusNodePub[] pub = nodeDetails.getTorusNodePub();

              JSONArray jsonArray = new JSONArray();
              for (TorusNodePub data : pub) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("x", data.getX());
                jsonObject.put("y", data.getY());
                jsonArray.put(jsonObject);
              }
              sss = new TKeyNodeDetails(new JSONArray(sssEndpoints).toString(), jsonArray.toString(), 3);
              rss = new TKeyNodeDetails(new JSONArray(rssEndpoints).toString(), jsonArray.toString(), 3);
              tss = new TKeyNodeDetails(new JSONArray(tssEndpoints).toString(), jsonArray.toString(), 3);
        }
        long ptr = jniServiceProvider(enableLogging, postboxKey, curveN, useTss, verifierName, verifierId, sss, tss, rss, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

     @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniServiceProviderFree();
    }
}
