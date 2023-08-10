package com.web3auth.tkey.ThresholdKey;

import androidx.annotation.Nullable;

import com.web3auth.tkey.RuntimeError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.torusresearch.fetchnodedetails.FetchNodeDetails;
import org.torusresearch.fetchnodedetails.types.TorusNodePub;
import org.torusresearch.fetchnodedetails.types.NodeDetails;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class ServiceProvider {
    private native void jniServiceProviderFree();
    private native long jniServiceProvider(boolean enableLogging, String postboxKey, String curveN, boolean useTss, @Nullable String verifierName, @Nullable String verifierId, @Nullable com.web3auth.tkey.ThresholdKey.Common.NodeDetails sss, @Nullable com.web3auth.tkey.ThresholdKey.Common.NodeDetails tss, @Nullable com.web3auth.tkey.ThresholdKey.Common.NodeDetails rss, RuntimeError error);

    final static String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    static String verifier;
    static String verifierId;
    static NodeDetails nodeDetails;
    static Boolean useTss;
    static Boolean assignedTssPubKey;

    final long pointer;
    static FetchNodeDetails fetchNodeDetails;


    /**
     * Instantiates a ServiceProvider object.
     * @param enableLogging Determines whether logging is enabled or not.
     * @param postboxKey The private key to be used for the ServiceProvider.
     * @param useTss Whether to use TSS or not.
     * @param verifierName Verifier Name, required for TSS.
     * @param verifierId Verifier ID, required for TSS.
     * @throws RuntimeError Indicates invalid parameters were used.
     * @see NodeDetails
     */
    public ServiceProvider(boolean enableLogging, String postboxKey, boolean useTss, @Nullable String verifierName, @Nullable String verifierId, @Nullable NodeDetails nodeDetails) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        com.web3auth.tkey.ThresholdKey.Common.NodeDetails sss = null;
        com.web3auth.tkey.ThresholdKey.Common.NodeDetails rss = null;
        com.web3auth.tkey.ThresholdKey.Common.NodeDetails tss = null;

        if(nodeDetails != null) {
            String[] sssEndpoints = nodeDetails.getTorusNodeSSSEndpoints();
            String[] rssEndpoints = nodeDetails.getTorusNodeRSSEndpoints();
            String[] tssEndpoints = nodeDetails.getTorusNodeTSSEndpoints();
            String[] nodeEndpoints = nodeDetails.getTorusNodeEndpoints();

            TorusNodePub[] pub = nodeDetails.getTorusNodePub();

//            sss = NodeDetails("", nodeEndpoints, sssEndpoints, rssEndpoints, tssEndpoints, )
//              sss = new FetchNodeDetails()
              System.out.println("sssEndpoints.toString()");
              System.out.println(Arrays.toString(sssEndpoints));
              System.out.println(new JSONArray(sssEndpoints).toString());
              System.out.println(pub[0].toString());
              System.out.println(pub[0].getX());
              JSONArray jsonArray = new JSONArray();
              for (TorusNodePub data : pub) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("x", data.getX());
                jsonObject.put("y", data.getY());
                jsonArray.put(jsonObject);
              }
              System.out.println(jsonArray.toString());
              sss = new com.web3auth.tkey.ThresholdKey.Common.NodeDetails(new JSONArray(sssEndpoints).toString(), jsonArray.toString(), 3);
              rss = new com.web3auth.tkey.ThresholdKey.Common.NodeDetails(new JSONArray(rssEndpoints).toString(), jsonArray.toString(), 3);
              tss = new com.web3auth.tkey.ThresholdKey.Common.NodeDetails(new JSONArray(tssEndpoints).toString(), jsonArray.toString(), 3);
        }
        long ptr = jniServiceProvider(enableLogging, postboxKey, curveN, useTss, verifierName, verifierId, sss, rss, tss, error);
        if (error.code != 0) {
            throw error;
        }
        this.nodeDetails = nodeDetails;
        this.verifier = verifierName;
        this.verifierId = verifierId;
        this.useTss = useTss;
        pointer = ptr;
    }

     @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniServiceProviderFree();
    }
}
