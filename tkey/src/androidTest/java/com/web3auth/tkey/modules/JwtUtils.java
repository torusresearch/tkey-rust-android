package com.web3auth.tkey.modules;

import androidx.annotation.Nullable;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

public final class JwtUtils {
    public static String generateIdToken(String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Algorithm algorithmRs;

        byte[] privateKeyByte = PEMConverter.pemToBytes();
        ECPrivateKey privateKey = (ECPrivateKey) PEMConverter.getPrivateKey(privateKeyByte, "EC");
        ECPublicKey publicKey = (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(privateKey.getParams().getGenerator(), privateKey.getParams()));
        algorithmRs = Algorithm.ECDSA256(publicKey, privateKey);

        return JWT.create()
                .withSubject("email|" + email.split("@")[0])
                .withAudience("torus-key-test")
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .withIssuedAt(new Date())
                .withIssuer("torus-key-test")
                .withClaim("email", email)
                .withClaim("nickname", email.split("@")[0])
                .withClaim("name", email)
                .withClaim("picture", "")
                .withClaim("email_verified", true)
                .sign(algorithmRs);
    }

    public static void main(@Nullable String[] args) {
    }
}
