package com.web3auth.tkey.ThresholdKey.Common;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import com.web3auth.tkey.RuntimeError;

public final class ServerOptions {
    private native long jniServerOptions(String serverEndpointsString, String PubKeys, int ServerThreshold, String authSignatures, @Nullable String selectedServers, RuntimeError error);
    private native void jniServerOptionsFree();

    private long pointer;

    public ServerOptions(String[] ServerEndpoints, String PubKeys, int ServerThreshold, ArrayList<String> authSignatures, @Nullable List<Integer> selectedServers) throws RuntimeError, JSONException {
        RuntimeError error = new RuntimeError();
        // Convert to JSON using JSONArray
        JSONArray jsonArray = new JSONArray(authSignatures);
        String authSignaturesString = jsonArray.toString();

        JSONArray serverEndpointsArray = new JSONArray(ServerEndpoints);
        String serverEndpointsString = serverEndpointsArray.toString();

        System.out.println(serverEndpointsString);
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

        // todo: update PubKeys
        long ptr = jniServerOptions(serverEndpointsString, PubKeys, ServerThreshold, authSignaturesString, selectedServersString, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniServerOptionsFree();
    }
}
