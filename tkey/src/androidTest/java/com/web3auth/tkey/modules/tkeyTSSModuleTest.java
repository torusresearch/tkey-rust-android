package com.web3auth.tkey.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import android.util.Pair;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.Modules.TSSModule;
import com.web3auth.tkey.ThresholdKey.RssComm;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONObject;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;


final class TSSMod {
    private final ThresholdKey thresholdKey;
    private final String tag;

    public TSSMod(ThresholdKey thresholdKey, String tag) {
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

@RunWith(AndroidJUnit4.class)
public class tkeyTSSModuleTest {
    static {
        System.loadLibrary("tkey-native");
    }

    @BeforeClass
    public static void setupTest() {
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void testTSSModule() {
        try {
            String TORUS_TEST_EMAIL = "saasa2123@tr.us";
            String TORUS_TEST_VERIFIER = "torus-test-health";

            FetchNodeDetails nodeManager = new FetchNodeDetails(TorusNetwork.SAPPHIRE_DEVNET);

            CompletableFuture<NodeDetails> nodeDetailResult = nodeManager.getNodeDetails(TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL);
            NodeDetails nodeDetail = nodeDetailResult.get();

            TorusCtorOptions torusOptions = new TorusCtorOptions("Custom");
            torusOptions.setNetwork(TorusNetwork.SAPPHIRE_DEVNET.toString());
            torusOptions.setClientId("BG4pe3aBso5SjVbpotFQGnXVHgxhgOxnqnNBKyjfEJ3izFvIVWUaMIzoCrAfYag8O6t6a6AOvdLcS4JR2sQMjR4");
            TorusUtils torusUtils = new TorusUtils(torusOptions);

            String idToken = JwtUtils.generateIdToken(TORUS_TEST_EMAIL);

            RetrieveSharesResponse retrievedShare = torusUtils.retrieveShares(nodeDetail.getTorusNodeEndpoints(), nodeDetail.getTorusIndexes(), TORUS_TEST_VERIFIER, new HashMap<String, Object>() {{
                put("verifier_id", TORUS_TEST_EMAIL);
            }} , idToken).get();

            ArrayList<String> signatureString = new ArrayList<>();
            List<SessionToken> signature = retrievedShare.getSessionData().getSessionTokenData();

            for (SessionToken item : signature) {
                if (item != null) {
                    JSONObject temp = new JSONObject();
                    temp.put("data", item.getToken());
                    temp.put("sig", item.getSignature());
                    signatureString.add(temp.toString());
                }
            }

            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(true, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(true, postboxKey.hex,true, TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL,nodeDetail);
            RssComm rss_comm = new RssComm();

            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, rss_comm);
            CountDownLatch lock = new CountDownLatch(2);

            thresholdKey.initialize(postboxKey.hex, null, false, false, false, false, null, 0, null, result -> {
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
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock1.countDown();
            });
            lock1.await();

            String firstShare = thresholdKey.outputShare(share[0].getIndex());
            String firstShareIndex = share[0].getIndex();
            String tssTag = "testing";
            PrivateKey factorKey = PrivateKey.generate();
            String factorPub = factorKey.toPublic();

            CountDownLatch lock2 = new CountDownLatch(1);

            TSSModule.backupShareWithFactorKey(thresholdKey, firstShareIndex, factorKey.hex);

            TSSModule.createTaggedTSSTagShare(thresholdKey, tssTag, null, factorPub, 2, nodeDetail, torusUtils, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not create tagged tss shares for tkey");
                }
                lock2.countDown();
            });
            lock2.await();

            Pair<String, String> tssShareResponse = TSSModule.getTSSShare(thresholdKey, tssTag, factorKey.hex, 0);
            String tssIndex = tssShareResponse.first;
            String tssShare = tssShareResponse.second;

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
            Pair<String, String> tssShareResponseUpdated = TSSModule.getTSSShare(thresholdKey, tssTag, factorKey.hex, 0);
            String tssShareUpdated = tssShareResponseUpdated.second;


            // after refresh (generate new share), existing tss_share is not valid anymore, new tss share1 is return
            assertNotEquals(tssShare3, tssShareUpdated);
            assertNotEquals(tssIndex3, tssIndex);
            assertNotEquals(tssShare, tssShareUpdated);

            // Initialize on Instance 2
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, rss_comm);

            CountDownLatch lock5 = new CountDownLatch(3);
            thresholdKey2.initialize(postboxKey.hex, null, false, false, false, false, null, 0, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock5.countDown();
            });
            thresholdKey2.inputFactorKey(factorKey.hex, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not inputFactorKey for tkey");
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
            assertNotEquals(tssShare, tssShare2);
            assertEquals(tssShareUpdated, tssShare2);
            assertEquals(tssIndex, tssIndex2);
            Pair<String, String> tssShareResponse2_3 = TSSModule.getTSSShare(thresholdKey2, tssTag, newFactorKey.hex, 0);
            String tssIndex2_3 = tssShareResponse2_3.first;
            String tssShare2_3 = tssShareResponse2_3.second;
            assertEquals(tssIndex3, tssIndex2_3);
            assertEquals(tssShare3, tssShare2_3);

            // 2/3 -> 2/2 tss
            CountDownLatch lock6 = new CountDownLatch(1);
            TSSModule.deleteTSSShare(thresholdKey, tssTag, tssShare3, Integer.parseInt(tssIndex3), signatureString, newFactorPub, nodeDetail, torusUtils, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not deleteTSSShare mpc tkey");
                }
                lock6.countDown();
            });
            lock6.await();
            Pair<String, String> tssShareResponseUpdated2 = TSSModule.getTSSShare(thresholdKey, tssTag, factorKey.hex, 0);
            String tssIndexUpdated2 = tssShareResponseUpdated2.first;
            String tssShareUpdated2 = tssShareResponseUpdated2.second;
            assertNotEquals(tssShare, tssShareUpdated2);
            assertNotEquals(tssShareUpdated, tssShareUpdated2);
            assertEquals(tssIndex, tssIndexUpdated2);

            // 2/2 -> 2/3 tss using addFactorPub
            CountDownLatch lock7 = new CountDownLatch(1);
            TSSModule.AddFactorPub(thresholdKey, tssTag, factorKey.hex, signatureString, newFactorPub, 3, null, nodeDetail, torusUtils, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not Add Factor Pub");
                }
                lock7.countDown();
            });
            lock7.await();

            CountDownLatch lock8 = new CountDownLatch(1);
            // 2/3 -> 2/2 tss using deleteFactorPub
            TSSModule.DeleteFactorPub(thresholdKey, tssTag, factorKey.hex, signatureString, newFactorPub, nodeDetail, torusUtils, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not Delete Factor Pub");
                }
                lock8.countDown();
            });
            lock8.await();
            System.gc();
        } catch (Exception | RuntimeError e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMultipleTSSTags() {
        try {
            String TORUS_TEST_EMAIL = "saasa2123@tr.us";
            String TORUS_TEST_VERIFIER = "torus-test-health";

            FetchNodeDetails nodeManager = new FetchNodeDetails(TorusNetwork.SAPPHIRE_DEVNET);

            CompletableFuture<NodeDetails> nodeDetailResult = nodeManager.getNodeDetails(TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL);
            NodeDetails nodeDetail = nodeDetailResult.get();

            TorusCtorOptions torusOptions = new TorusCtorOptions("Custom");
            torusOptions.setNetwork(TorusNetwork.SAPPHIRE_DEVNET.toString());
            torusOptions.setClientId("BG4pe3aBso5SjVbpotFQGnXVHgxhgOxnqnNBKyjfEJ3izFvIVWUaMIzoCrAfYag8O6t6a6AOvdLcS4JR2sQMjR4");
            TorusUtils torusUtils = new TorusUtils(torusOptions);

            String idToken = JwtUtils.generateIdToken(TORUS_TEST_EMAIL);

            RetrieveSharesResponse retrievedShare = torusUtils.retrieveShares(nodeDetail.getTorusNodeEndpoints(), nodeDetail.getTorusIndexes(), TORUS_TEST_VERIFIER, new HashMap<String, Object>() {{
                put("verifier_id", TORUS_TEST_EMAIL);
            }} , idToken).get();

            ArrayList<String> signatureString = new ArrayList<>();
            List<SessionToken> signature = retrievedShare.getSessionData().sessionTokenData;

            for (SessionToken item : signature) {
                if (item != null) {
                    signatureString.add(item.getSignature());
                }
            }

            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(true, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(true, postboxKey.hex,true, TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL,nodeDetail);
            RssComm rss_comm = new RssComm();

            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, rss_comm);
            CountDownLatch lock = new CountDownLatch(2);

            thresholdKey.initialize(postboxKey.hex, null, false, false, false, false, null, 0, null, result -> {
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
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock1.countDown();
            });
            lock1.await();
            String firstShare = thresholdKey.outputShare(share[0].getIndex());
            String[] testTags = {"tag1", "tag2", "tag3", "tag4", "tag5"};
            List<TSSMod> tssMods = new ArrayList<>();
            List<PrivateKey> factorKeys = new ArrayList<>();
            List<String> tssIndexes = new ArrayList<>();
            List<String> tssShares = new ArrayList<>();

            for (String tag: testTags) {
                tssMods.add(new TSSMod(thresholdKey, tag));

                PrivateKey factorKey = PrivateKey.generate();
                String factorPub = factorKey.toPublic();
                factorKeys.add(factorKey);

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

                TSSMod tssMod = tssMods.get(i);

                CountDownLatch lock10 = new CountDownLatch(1);
                TSSModule.AddFactorPub(thresholdKey, tssMod.getTag(), factorKeys.get(i).hex, signatureString, newFactorPub, 3, null, nodeDetail , torusUtils, result -> {
                    if (result instanceof Result.Error) {
                        fail("Could not AddFactorPub");
                    }
                    lock10.countDown();
                });
                lock10.await();

                Pair<String, String> tssShareResponse = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), factorKeys.get(i).hex, 0);
                assertEquals(tssIndexes.get(i), tssShareResponse.first);
                assertNotEquals(tssShares.get(i), tssShareResponse.second);

                Pair<String, String> tssShareResponse2 = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), newFactorKey.hex, 0);
                assertNotEquals(tssIndexes.get(i), tssShareResponse2.first);
                assertNotEquals(tssShares.get(i), tssShareResponse2.second);
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

            for (int i = 0; i < tssMods.size(); i++) {
                PrivateKey newFactorKey2 = PrivateKey.generate();
                String newFactorPub2 = newFactorKey2.toPublic();
                newFactorKeys2.add(newFactorKey2);

                TSSMod tssMod = tssMods.get(i);
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
                assertNotEquals(tssShares.get(i), tssShareResponse.second);

                Pair<String, String> tssShareResponse1 = TSSModule.getTSSShare(thresholdKey, tssMod.getTag(), newFactorKeys.get(i).hex, 0);
                assertNotEquals(tssIndexes.get(i), tssShareResponse1.first);
                assertNotEquals(tssShares.get(i), tssShareResponse1.second);

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
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, true, false, rss_comm);
            CountDownLatch lock7 = new CountDownLatch(3);
            thresholdKey2.initialize(postboxKey.hex, null, false, false, false, false, null, 0, null, result -> {
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

            List<TSSMod> tssMods2 = new ArrayList<>();

            for (int i=0; i< testTags.length; i++) {
                tssMods2.add(new TSSMod(thresholdKey, testTags[i]));
                TSSModule.getTSSShare(thresholdKey, testTags[i], factorKeys.get(i).hex, 0);
            }
            CountDownLatch lock8 = new CountDownLatch(1);
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock8.countDown();
            });
            lock8.await();

            for (int i = 0; i < tssMods2.size(); i++) {
                PrivateKey newFactorKey2 = PrivateKey.generate();
                newFactorKeys2.add(newFactorKey2);

                TSSMod tssMod = tssMods2.get(i);
                CountDownLatch lock9 = new CountDownLatch(1);
                TSSModule.DeleteFactorPub(thresholdKey, tssMod.getTag(), newFactorKeys.get(i).hex, signatureString, newFactorPubs.get(i), nodeDetail , torusUtils, null, result -> {
                    if (result instanceof Result.Error) {
                        fail("Could not DeleteFactorPub");
                    }
                    lock9.countDown();
                });
                lock9.await();
            }
            CountDownLatch lock10 = new CountDownLatch(1);
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
                }
                lock10.countDown();
            });
            lock10.await();
            System.gc();
        } catch (Exception | RuntimeError e) {
            throw new RuntimeException(e);
        }
    }
}
