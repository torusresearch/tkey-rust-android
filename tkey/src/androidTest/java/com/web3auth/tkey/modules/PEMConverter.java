package com.web3auth.tkey.modules;

import androidx.annotation.Nullable;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public final class PEMConverter {

    public static byte[] pemToBytes() {

        String pemString = "-----BEGIN PRIVATE KEY-----\n" +
                "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCCD7oLrcKae+jVZPGx52Cb/lKhdKxpXjl9eGNa1MlY57A==\n" +
                "-----END PRIVATE KEY-----";

        // Remove header and footer lines
        String pemContent = pemString
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");  // Remove whitespace

        // Decode base64-encoded content
        return Base64.getDecoder().decode(pemContent);
    }

    public static java.security.PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
        java.security.PrivateKey privateKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not reconstruct the private key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            System.out.println("Could not reconstruct the private key");
        }

        return privateKey;
    }

    public static void main(@Nullable String[] args) {
    }
}
