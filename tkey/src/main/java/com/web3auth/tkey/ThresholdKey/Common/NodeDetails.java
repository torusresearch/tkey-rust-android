package com.web3auth.tkey.ThresholdKey.Common;
import org.json.JSONArray;
import org.json.JSONException;

import com.web3auth.tkey.RuntimeError;
class JSONConverter {

    public byte[] convertToData(String[] endpoints) throws JSONException {
        JSONArray jsonArray = new JSONArray(endpoints);
        String jsonString = jsonArray.toString();
        return jsonString.getBytes();
    }

    public static void main(String[] args) {
    }
}
public final class NodeDetails {
    private long pointer;
    private native long jniNodeDetails(String serverEndpoints, String serverPublicKeys, int serverThreshold, RuntimeError error);
    private native void jniNodeDetailsFree();

    public NodeDetails(String serverEndpoints, String serverPublicKeys, int serverThreshold) throws RuntimeError {
//        JSONConverter jsonConverter = new JSONConverter();

        RuntimeError error = new RuntimeError();
        long ptr = jniNodeDetails(serverEndpoints, serverPublicKeys, serverThreshold, error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniNodeDetailsFree();
    }
}
