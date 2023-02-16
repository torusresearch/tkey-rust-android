package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class StorageLayer {
    private native long jniStorageLayer(boolean enableLogging, String hostUrl, int serverTimeOffset, String networkInterfaceMethodName, String networkInterfaceMethodSignature, RuntimeError error);

    private native void jniStorageLayerFree();

    final long pointer;

    private String networkInterface(String url, String data, RuntimeError error) {
        try {
            URL uri = new URL(url);
            HttpURLConnection con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Access-Control-Allow-Origin", "*");
            con.setRequestProperty("Access-Control-Allow-Methods", "GET, POST");
            con.setRequestProperty("Access-Control-Allow-Headers", "Content-Type");
            String last = url.substring(url.lastIndexOf('/') + 1);
            con.setDoOutput(true);
            String form_data_string;
            if (last.equalsIgnoreCase("bulk_set_stream")) {
                ArrayList<String> form_data = new ArrayList<>();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String value = jsonObject.toString();
                    String encodedValue = URLEncoder.encode(value, "utf-8");
                    String finalValue = i + "=" + encodedValue;
                    form_data.add(finalValue);
                }
                form_data_string = String.join("&", form_data);
            } else {
                con.setRequestProperty("Content-Type", "application/json");
                form_data_string = data;
            }
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = form_data_string.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                error.code = 0;
                return response.toString();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            error.code = -1;
            return "";
        }
    }

    public StorageLayer(boolean enableLogging, String hostUrl, int serverTimeOffset) throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniStorageLayer(enableLogging, hostUrl, serverTimeOffset, "networkInterface", "(Ljava/lang/String;Ljava/lang/String;Lcom/web3auth/tkey/RuntimeError;)Ljava/lang/String;", error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniStorageLayerFree();
    }
}
