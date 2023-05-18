package com.web3auth.tkey.ThresholdKey;

import com.web3auth.tkey.RuntimeError;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class RssComm {
    private native long jniRssComm(String networkInterfaceMethodName, String networkInterfaceMethodSignature, RuntimeError error);
    private native void jniRssCommFree();
    final long pointer;
    @SuppressWarnings("unused") // linter cannot detect that this is called from the JNI
    final long callback_method_id = 0;
    private volatile String networkResponse;
    private volatile int networkCode;

    public RssComm(long pointer) {
        this.pointer = pointer;
    }

    @SuppressWarnings("unused") // linter cannot detect that this is called from the JNI
    private String networkInterface(String url, String data, RuntimeError error) {
        Thread networkThread = new Thread() {
            @Override
            public void run() {
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
                    con.setRequestProperty("Content-Type", "application/json");
                    form_data_string = data;
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
                        networkCode = 0;
                        networkResponse = response.toString();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    networkCode = -1;
                    networkResponse = "";
                }
            }
        };
        networkThread.start();
        try {
            networkThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            networkCode = -1;
            networkResponse = "";
        }
        error.code = networkCode;
        return networkResponse;
    }

    public RssComm() throws RuntimeError {
        RuntimeError error = new RuntimeError();
        long ptr = jniRssComm("networkInterface", "(Ljava/lang/String;Ljava/lang/String;Lcom/web3auth/tkey/RuntimeError;)Ljava/lang/String;", error);
        if (error.code != 0) {
            throw error;
        }
        pointer = ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        jniRssCommFree();
    }
}
