package com.web3auth.tkey.modules;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.KeyDetails;
import com.web3auth.tkey.ThresholdKey.Modules.TSSModule;
import com.web3auth.tkey.ThresholdKey.RssComm;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.torusresearch.fetchnodedetails.FetchNodeDetails;
import org.torusresearch.fetchnodedetails.types.NodeDetails;
import org.torusresearch.fetchnodedetails.types.TorusNetwork;
import org.torusresearch.torusutils.TorusUtils;
import org.torusresearch.torusutils.types.RetrieveSharesResponse;
import org.torusresearch.torusutils.types.SessionToken;
import org.torusresearch.torusutils.types.TorusCtorOptions;
import org.torusresearch.torusutils.types.VerifierParams;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.auth0.jwt.algorithms.Algorithm;

import com.auth0.jwt.JWT;
import org.web3j.crypto.Hash;
class TssMod {
    private ThresholdKey thresholdKey;
    private String tag;

    public TssMod(ThresholdKey thresholdKey, String tag) {
        this.thresholdKey = thresholdKey;
        this.tag = tag;
    }

    public ThresholdKey getThresholdKey() {
        return thresholdKey;
    }

    public String getTag() {
        return tag;
    }
}
class PEMConverter {

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
        byte[] pemBytes = Base64.getDecoder().decode(pemContent);

        return pemBytes;
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
class JwtUtils {
    public static String generateIdToken(String email) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Algorithm algorithmRs;
        String verifierPrivateKeyForSigning = "-----BEGIN PRIVATE KEY-----\nMEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCCD7oLrcKae+jVZPGx52Cb/lKhdKxpXjl9eGNa1MlY57A==\n-----END PRIVATE KEY-----";

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
@RunWith(AndroidJUnit4.class)
public class tkeyTSSModuleTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static ThresholdKey thresholdKey;
    public String curveN = "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141";

    @BeforeClass
    public static void setupTest() {
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void test() {
        try {
            String TORUS_TEST_EMAIL = "saasa2123@tr.us";
            String TORUS_TEST_VERIFIER = "torus-test-health";
//            String TORUS_IMPORT_EMAIL = "importeduser2@tor.us";
//            String TORUS_EXTENDED_VERIFIER_EMAIL = "testextenderverifierid@example.com";

            FetchNodeDetails nodeManager = new FetchNodeDetails(TorusNetwork.SAPPHIRE_DEVNET);

            CompletableFuture<NodeDetails> nodeDetailResult = nodeManager.getNodeDetails(TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL);
            NodeDetails nodeDetail = nodeDetailResult.get();

            System.out.println(nodeDetail.getTorusNodeSSSEndpoints());
            System.out.println("nodeDetail.getTorusNodeSSSEndpoints()");
            TorusCtorOptions torusOptions = new TorusCtorOptions("Custom");
            torusOptions.setNetwork(TorusNetwork.SAPPHIRE_DEVNET.toString());
            torusOptions.setClientId("BG4pe3aBso5SjVbpotFQGnXVHgxhgOxnqnNBKyjfEJ3izFvIVWUaMIzoCrAfYag8O6t6a6AOvdLcS4JR2sQMjR4");
//            torusOptions.setEnableOneKey(true);
//            torusOptions.setAllowHost("https://signer.tor.us/api/allow");
            TorusUtils torusUtils = new TorusUtils(torusOptions);

            String idToken = new JwtUtils().generateIdToken(TORUS_TEST_EMAIL);
            String hashedIdToken = Hash.sha3String(idToken).substring(2);

            System.out.println(idToken);
            System.out.println("idToken");
            // todo: validate
            RetrieveSharesResponse retrievedShare = torusUtils.retrieveShares(nodeDetail.getTorusNodeEndpoints(), nodeDetail.getTorusIndexes(), TORUS_TEST_VERIFIER, new HashMap<String, Object>() {{
                put("verifier_id", TORUS_TEST_EMAIL);
            }} , idToken).get();

            System.out.println(retrievedShare.getFinalKeyData().getPrivKey() + " priv key " + retrievedShare.getFinalKeyData().getEvmAddress() + " nonce " + retrievedShare.getMetadata().getNonce());
            System.out.println("retrievedShare");
//            JSONArray signatureArray = new JSONArray();
            ArrayList<String> signatureString = new ArrayList<>();
            List<SessionToken> signature = retrievedShare.getSessionData().getSessionTokenData();
            Set<String> integerSet = new HashSet<>();
            System.out.println(signature.size());

            for (SessionToken item : signature) {
                if (item != null) {
                    System.out.println("signature");
//                    System.out.println(item.getSignature().toString());
//                    signatureArray.put(item.getSignature());
                    signatureString.add(item.getSignature());
                    integerSet.add(item.getSignature());
                }
            }
//            for (String item : integerSet) {
//                if (item != null) {
//                    signatureString.add(item);
//                }
//            }
            System.out.println("signature string");
            System.out.println(signatureString.toString());


            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(true, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(true, postboxKey.hex,true, TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL,nodeDetail);
            RssComm rss_comm = new RssComm();

            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, rss_comm);
            CountDownLatch lock = new CountDownLatch(2);

            thresholdKey.initialize(postboxKey.hex, null, false, false, false, null, 0, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock.countDown();
            });
            thresholdKey.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock.countDown();
            });
            lock.await();
            thresholdKey.getKeyDetails();
            thresholdKey.getLastFetchedCloudMetadata();
            thresholdKey.getLocalMetadataTransitions();

            CountDownLatch lock1 = new CountDownLatch(1);
            final GenerateShareStoreResult[] share = new GenerateShareStoreResult[1];

            thresholdKey.generateNewShare(false, null,result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                System.out.println("Generate New Share");
                System.out.println(((Result.Success<GenerateShareStoreResult>) result).data);
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock1.countDown();
            });
            lock1.await();
//            System.out.println(share.);
            String firstShare = thresholdKey.outputShare(share[0].getIndex());

            String tssTag = "testing";

            
            PrivateKey factorKey = PrivateKey.generate();
            String factorPub = factorKey.toPublic();

            System.out.println("Pre TSS Module");
            System.out.println(factorPub.toString());
            CountDownLatch lock2 = new CountDownLatch(1);

//            TSSModule.updateTssPubKey(thresholdKey, tssTag, nodeDetail, torusUtils, false, result -> {
//                if (result instanceof Result.Error) {
//                    throw new RuntimeException("failed to pre updateTssPubKey");
//                }
//            });
            TSSModule.createTaggedTSSTagShare(thresholdKey, tssTag, null, factorPub, 2, nodeDetail, torusUtils, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not create tagged tss shares for tkey");
                }
                System.out.println("Generate New Share");
                lock2.countDown();
            });
            lock2.await();

            Pair<String, String> tssShareResponse = TSSModule.getTSSShare(thresholdKey, tssTag, factorKey.hex, 0);
            String tssIndex = tssShareResponse.first;
            String tssShare = tssShareResponse.second;
            System.out.println("tssShare");
            System.out.println(tssShare);
            System.out.println(tssIndex);

            CountDownLatch lock3 = new CountDownLatch(1);
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock3.countDown();
            });
            lock3.await();


            PrivateKey newFactorKey = PrivateKey.generate();
            String newFactorPub = newFactorKey.toPublic();
            // 2/2 -> 2/3 tss
            CountDownLatch lock4 = new CountDownLatch(1);

            TSSModule.generateTSSShare(thresholdKey, tssTag, tssShare, Integer.parseInt(tssIndex), signatureString, newFactorPub, 3, nodeDetail, torusUtils, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generateTSSShare mpc tkey");
                }
                lock4.countDown();
            });
            lock4.await();

            Pair<String, String> tssShareResponse3 = TSSModule.getTSSShare(thresholdKey, tssTag, newFactorKey.hex, 0);
            String tssIndex3 = tssShareResponse3.first;
            String tssShare3 = tssShareResponse3.second;
            System.out.println("tssShare");
            System.out.println(tssShare3);
            System.out.println(tssIndex3);
            Pair<String, String> tssShareResponseUpdated = TSSModule.getTSSShare(thresholdKey, tssTag, factorKey.hex, 0);
            String tssShareUpdated = tssShareResponse3.second;


            // after refresh (generate new share), existing tss_share is not valid anymore, new tss share1 is return
            assertEquals(tssShare, tssShareUpdated);
            assertEquals(tssShare3, tssShareUpdated);
            assertEquals(tssIndex3, tssIndex);

            // Initialize on Instance 2
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, null);

            CountDownLatch lock5 = new CountDownLatch(3);
            thresholdKey2.initialize(postboxKey.hex, null, false, false, false, null, 0, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock5.countDown();
            });
            thresholdKey2.inputShare(firstShare, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not input share for tkey");
                }
                lock5.countDown();
            });
            thresholdKey2.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock5.countDown();
            });
            lock5.await();

            Pair<String, String> tssShareResponse2 = TSSModule.getTSSShare(thresholdKey2, tssTag, factorKey.hex, 0);
            String tssIndex2 = tssShareResponse2.first;
            String tssShare2 = tssShareResponse2.second;
            assertEquals(tssShare, tssShare2);
            assertEquals(tssShareUpdated, tssShare2);
            assertEquals(tssIndex, tssIndex2);
            Pair<String, String> tssShareResponse2_3 = TSSModule.getTSSShare(thresholdKey2, tssTag, newFactorKey.hex, 0);
            String tssIndex2_3 = tssShareResponse2_3.first;
            String tssShare2_3 = tssShareResponse2_3.second;
            assertEquals(tssIndex3, tssIndex2_3);
            assertEquals(tssShare3, tssShare2_3);

            // 2/3 -> 2/2 tss
            CountDownLatch lock6 = new CountDownLatch(1);
            TSSModule.deleteTSSShare(thresholdKey2, tssTag, tssShare3, Integer.parseInt(tssIndex3), signatureString, newFactorPub, nodeDetail, torusUtils, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not deleteTSSShare mpc tkey");
                }
                lock6.countDown();
            });
            lock6.await();
            Pair<String, String> tssShareResponseUpdated2 = TSSModule.getTSSShare(thresholdKey2, tssTag, factorKey.hex, 0);
            String tssIndexUpdated2 = tssShareResponseUpdated2.first;
            String tssShareUpdated2 = tssShareResponseUpdated2.second;
            assertEquals(tssShare, tssShareUpdated2);
            assertEquals(tssShareUpdated, tssShareUpdated2);

            // 2/2 -> 2/3 tss using addFactorPub
            TSSModule.AddFactorPub(thresholdKey, tssTag, factorKey.hex, signatureString, newFactorPub, 3, null, nodeDetail, torusUtils);

            // 2/3 -> 2/2 tss using deleteFactorPub
            TSSModule.DeleteFactorPub(thresholdKey, tssTag, factorKey.hex, signatureString, newFactorPub, nodeDetail, torusUtils, null);
        } catch (Exception | RuntimeError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMultipleTSSTags() throws RuntimeError, ExecutionException, InterruptedException, JSONException, NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            String TORUS_TEST_EMAIL = "saasa2123@tr.us";
            String TORUS_TEST_VERIFIER = "torus-test-health";
//            String TORUS_IMPORT_EMAIL = "importeduser2@tor.us";
//            String TORUS_EXTENDED_VERIFIER_EMAIL = "testextenderverifierid@example.com";

            FetchNodeDetails nodeManager = new FetchNodeDetails(TorusNetwork.SAPPHIRE_DEVNET);

            CompletableFuture<NodeDetails> nodeDetailResult = nodeManager.getNodeDetails(TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL);
            NodeDetails nodeDetail = nodeDetailResult.get();

            System.out.println(nodeDetail.getTorusNodeSSSEndpoints());
            System.out.println("nodeDetail.getTorusNodeSSSEndpoints()");
            TorusCtorOptions torusOptions = new TorusCtorOptions("Custom");
            torusOptions.setNetwork(TorusNetwork.SAPPHIRE_DEVNET.toString());
            torusOptions.setClientId("BG4pe3aBso5SjVbpotFQGnXVHgxhgOxnqnNBKyjfEJ3izFvIVWUaMIzoCrAfYag8O6t6a6AOvdLcS4JR2sQMjR4");
            // torusOptions.setEnableOneKey(true);
            TorusUtils torusUtils = new TorusUtils(torusOptions);

            String idToken = new JwtUtils().generateIdToken(TORUS_TEST_EMAIL);
            String hashedIdToken = Hash.sha3String(idToken).substring(2);

            System.out.println(idToken);
            System.out.println("idToken");
            // todo: validate
            RetrieveSharesResponse retrievedShare = torusUtils.retrieveShares(nodeDetail.getTorusNodeEndpoints(), nodeDetail.getTorusIndexes(), TORUS_TEST_VERIFIER, new HashMap<String, Object>() {{
                put("verifier_id", TORUS_TEST_EMAIL);
            }} , idToken).get();

            System.out.println(retrievedShare.getFinalKeyData().getPrivKey() + " priv key " + retrievedShare.getFinalKeyData().getEvmAddress() + " nonce " + retrievedShare.getMetadata().getNonce());
            System.out.println("retrievedShare");
            ArrayList<String> signatureString = new ArrayList<>();
            List<SessionToken> signature = retrievedShare.getSessionData().sessionTokenData;

            for (SessionToken item : signature) {
                if (item != null) {
                    System.out.println("signature");
                    System.out.println(item.getSignature());
                    signatureString.add(item.getSignature());
                }
            }

            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(true, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(true, postboxKey.hex,true, TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL,nodeDetail);
            RssComm rss_comm = new RssComm();

            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, rss_comm);
            CountDownLatch lock = new CountDownLatch(2);

            thresholdKey.initialize(postboxKey.hex, null, false, false, false, null, 0, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock.countDown();
            });
            thresholdKey.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock.countDown();
            });
            lock.await();
            thresholdKey.getKeyDetails();
            thresholdKey.getLastFetchedCloudMetadata();
            thresholdKey.getLocalMetadataTransitions();

            CountDownLatch lock1 = new CountDownLatch(1);
            final GenerateShareStoreResult[] share = new GenerateShareStoreResult[1];

            thresholdKey.generateNewShare(false, null,result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                System.out.println("Generate New Share");
                System.out.println(((Result.Success<GenerateShareStoreResult>) result).data);
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock1.countDown();
            });
            lock1.await();
            String firstShare = thresholdKey.outputShare(share[0].getIndex());
            String[] testTags = {"tag1", "tag2", "tag3", "tag4", "tag5"};
            List<TssMod> tssMods = new ArrayList<>();
            List<PrivateKey> factorKeys = new ArrayList<>();
            List<String> factorPubs = new ArrayList<>();
            List<String> tssIndexes = new ArrayList<>();
            List<String> tssShares = new ArrayList<>();

            for (String tag: testTags) {
                tssMods.add(new TssMod(thresholdKey, tag));

                PrivateKey factorKey = PrivateKey.generate();
                String factorPub = factorKey.toPublic();
                factorKeys.add(factorKey);
                factorPubs.add(factorPub);

                CountDownLatch lock2 = new CountDownLatch(2);
                TSSModule.setTSSTag(thresholdKey, tag, result -> {
                    if (result instanceof Result.Error) {
                        fail("Could not setTSSTag tkey");
                    }
                    lock2.countDown();
                });
                TSSModule.createTaggedTSSTagShare(thresholdKey, tag, null, factorPub, 2, nodeDetail, torusUtils,result -> {
                    if (result instanceof Result.Error) {
                        fail("Could not createTaggedTSSTagShare tkey");
                    }
                    lock2.countDown();
                });
                lock2.await();
                Pair<String, String> tssShareResponse = TSSModule.getTSSShare(thresholdKey, tag, factorKey.hex, 0);
                tssIndexes.add(tssShareResponse.first);
                tssShares.add(tssShareResponse.second);

            }
            CountDownLatch lock3 = new CountDownLatch(1);
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock3.countDown();
            });
            lock3.await();

            // add factor key
            List<PrivateKey> newFactorKeys = new ArrayList<>();
            List<String> newFactorPubs = new ArrayList<>();

            for (int i = 0; i < tssMods.size(); i++) {
                PrivateKey newFactorKey = PrivateKey.generate();
                String newFactorPub = newFactorKey.toPublic();
                newFactorKeys.add(newFactorKey);
                newFactorPubs.add(newFactorPub);

                TssMod tssMod = tssMods.get(i);
                TSSModule.AddFactorPub(thresholdKey, tssMod.getTag(), factorKeys.get(i).hex, signatureString, newFactorPub, 3, null, nodeDetail , torusUtils);

                Pair<String, String> tssShareResponse = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), factorKeys.get(i).hex, 0);
                assertEquals(tssIndexes.get(i), tssShareResponse.first);
                assertEquals(tssShares.get(i), tssShareResponse.second);

                Pair<String, String> tssShareResponse2 = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), newFactorKey.hex, 0);
                assertEquals(tssIndexes.get(i), tssShareResponse2.first);
                assertEquals(tssShares.get(i), tssShareResponse2.second);
            }
            CountDownLatch lock4 = new CountDownLatch(1);
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock4.countDown();
            });
            lock4.await();

            // copy factor key
            List<PrivateKey> newFactorKeys2 = new ArrayList<>();
            List<String> newFactorPubs2 = new ArrayList<>();

            for (int i = 0; i < tssMods.size(); i++) {
                PrivateKey newFactorKey2 = PrivateKey.generate();
                String newFactorPub2 = newFactorKey2.toPublic();
                newFactorKeys2.add(newFactorKey2);
                newFactorPubs2.add(newFactorPub2);

                TssMod tssMod = tssMods.get(i);
                CountDownLatch lock5 = new CountDownLatch(1);
                TSSModule.copyFactorPub(thresholdKey, tssMod.getTag(), newFactorKeys.get(i).hex, newFactorPub2, 3, result -> {
                    if (result instanceof Result.Error) {
                        fail("Could not sync local metadata transitions tkey");
                    }
                    lock5.countDown();
                });
                lock5.await();

                Pair<String, String> tssShareResponse = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), factorKeys.get(i).hex, 0);
                assertEquals(tssIndexes.get(i), tssShareResponse.first);
                assertEquals(tssShares.get(i), tssShareResponse.second);

                Pair<String, String> tssShareResponse1 = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), newFactorKeys.get(i).hex, 0);
                assertEquals(tssIndexes.get(i), tssShareResponse1.first);
                assertEquals(tssShares.get(i), tssShareResponse1.second);

                Pair<String, String> tssShareResponse2 = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), newFactorKeys2.get(i).hex, 0);
                assertEquals(tssShareResponse1.first, tssShareResponse2.first);
                assertEquals(tssShareResponse1.second, tssShareResponse2.second);
            }
            CountDownLatch lock6 = new CountDownLatch(1);
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock6.countDown();
            });
            lock6.await();

            // Initialize on Instance 2
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, null);
            CountDownLatch lock7 = new CountDownLatch(3);
            thresholdKey2.initialize(postboxKey.hex, null, false, false, false, null, 0, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock7.countDown();
            });
            thresholdKey2.inputShare(firstShare, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not input share for tkey");
                }
                lock7.countDown();
            });
            thresholdKey2.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock7.countDown();
            });
            lock7.await();

            List<TssMod> tssMods2 = new ArrayList<>();

            for (int i=0; i< testTags.length; i++) {
                tssMods2.add(new TssMod(thresholdKey2, testTags[i]));
                TSSModule.getTSSShare(thresholdKey2, testTags[i], factorKeys.get(i).hex, 0);
            }
            CountDownLatch lock8 = new CountDownLatch(1);
            thresholdKey2.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock8.countDown();
            });
            lock8.await();

            for (int i = 0; i < tssMods2.size(); i++) {
                PrivateKey newFactorKey2 = PrivateKey.generate();
                String newFactorPub2 = newFactorKey2.toPublic();
                newFactorKeys2.add(newFactorKey2);
                newFactorPubs2.add(newFactorPub2);

                TssMod tssMod = tssMods2.get(i);
                TSSModule.DeleteFactorPub(thresholdKey2, tssMod.getTag(), newFactorKeys.get(i).hex, signatureString, newFactorPubs.get(i), nodeDetail , torusUtils, null);
            }
            CountDownLatch lock9 = new CountDownLatch(1);
            thresholdKey2.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock9.countDown();
            });
            lock9.await();
        } catch (Exception | RuntimeError e) {
            throw new RuntimeException(e);
        }
    }
}
