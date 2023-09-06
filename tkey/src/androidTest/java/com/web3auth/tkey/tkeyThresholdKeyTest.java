package com.web3auth.tkey;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.KeyDetails;
import com.web3auth.tkey.ThresholdKey.KeyReconstructionDetails;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class tkeyThresholdKeyTest {
    static {
        System.loadLibrary("tkey-native");
    }

    @Test
    public void basic_threshold_key_reconstruct() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(2);
            thresholdKey.initialize(key.hex, null, false, false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                KeyDetails details = ((Result.Success<KeyDetails>) result).data;
                String compressed = null;
                try {
                    compressed = details.getPublicKeyPoint().getAsCompressedPublicKey("elliptic-compressed");
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                assertNotNull(compressed);
                lock.countDown();
            });
            thresholdKey.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                KeyReconstructionDetails details = ((Result.Success<KeyReconstructionDetails>) result).data;
                int len = 0;
                try {
                    len = details.getKey().length();
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                assertNotEquals(len, 0);
                lock.countDown();
            });
            lock.await();
            System.gc();
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @Test
    public void basic_threshold_key_method_test() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(2);
            thresholdKey.initialize(key.hex, null, false, false, result -> {
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
            GenerateShareStoreResult[] share = new GenerateShareStoreResult[1];
            CountDownLatch lock1 = new CountDownLatch(1);
            thresholdKey.generateNewShare(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock1.countDown();
            });
            lock1.await();
            String output = thresholdKey.outputShare(share[0].getIndex());
            thresholdKey.outputShareStore(share[0].getIndex(), null);
            thresholdKey.shareToShareStore(output);
            CountDownLatch lock2 = new CountDownLatch(2);
            thresholdKey.deleteShare(share[0].getIndex(), result -> {
                if (result instanceof Result.Error) {
                    fail("Could not delete share for tkey");
                }
                lock2.countDown();
            });

            GenerateShareStoreResult[] share2 = new GenerateShareStoreResult[1];
            thresholdKey.generateNewShare(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                share2[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock2.countDown();
            });
            lock2.await();
            String input = thresholdKey.outputShare(share2[0].getIndex());
            ShareStore inputStore = thresholdKey.outputShareStore(share2[0].getIndex(), null);

            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            CountDownLatch lock3 = new CountDownLatch(3);
            thresholdKey2.initialize(null, null, true, false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock3.countDown();
            });
            thresholdKey2.inputShare(input, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not input share for tkey");
                }
                lock3.countDown();
            });
            thresholdKey2.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock3.countDown();
            });
            lock3.await();
            ThresholdKey thresholdKey3 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            CountDownLatch lock4 = new CountDownLatch(4);
            thresholdKey3.initialize(null, null, true, false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                lock4.countDown();
            });
            thresholdKey3.inputShareStore(inputStore, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not input share store for tkey");
                }
                lock4.countDown();
            });
            thresholdKey3.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock4.countDown();
            });
            thresholdKey3.CRITICALDeleteTKey(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not delete tkey");
                }
                lock4.countDown();
            });
            lock4.await();
            System.gc();
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @Test
    public void threshold_key_manual_sync_test() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, true);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(5);
            thresholdKey.initialize(key.hex, null, false, false, result -> {
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
            thresholdKey.generateNewShare(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                lock.countDown();
            });
            thresholdKey.syncLocalMetadataTransitions(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not sync local metadata transitions tkey");
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
            System.gc();
        } catch (RuntimeError | InterruptedException e) {
            fail();
        }
    }

    @Test
    public void threshold_key_multi_instance() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            PrivateKey postboxKey2 = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            StorageLayer storageLayer2 = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider2 = new ServiceProvider(false, postboxKey2.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer2, serviceProvider2, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            PrivateKey key2 = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(4);

            thresholdKey.initialize(key.hex, null, false, false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                String pub = null;
                try {
                    pub = ((Result.Success<KeyDetails>) result).data.getPublicKeyPoint().getAsCompressedPublicKey("elliptic-compressed");
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                assertNotNull(pub);
                lock.countDown();
            });
            thresholdKey2.initialize(key2.hex, null, false, false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                String pub = null;
                try {
                    pub = ((Result.Success<KeyDetails>) result).data.getPublicKeyPoint().getAsCompressedPublicKey("elliptic-compressed");
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                assertNotNull(pub);
                lock.countDown();
            });
            AtomicReference<String> reconstruct_key_1 = new AtomicReference<>("");
            thresholdKey.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                String pub = null;
                try {
                    pub = ((Result.Success<KeyReconstructionDetails>) result).data.getKey();
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                assertNotNull(pub);
                reconstruct_key_1.set(pub);
                lock.countDown();
            });
            AtomicReference<String> reconstruct_key_2 = new AtomicReference<>("");
            thresholdKey2.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                String pub = null;
                try {
                    pub = ((Result.Success<KeyReconstructionDetails>) result).data.getKey();
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                assertNotNull(pub);
                reconstruct_key_2.set(pub);
                lock.countDown();
            });
            lock.await();
            assertNotEquals(reconstruct_key_1.get().length(), 0);
            assertNotEquals(reconstruct_key_2.get().length(), 0);
            assertNotEquals(reconstruct_key_1.get(), reconstruct_key_2.get());

            //Best effort attempt to have the garbage collector execute finalizers so that they are explicitly tested,
            // however they are not guaranteed by Java to be called on demand.
            System.gc();
        } catch (RuntimeError | InterruptedException e) {
            fail();
        }
    }

    @Test
    public void share_descriptions_test() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(3);
            thresholdKey.initialize(key.hex, null, false, false, result -> {
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
            final GenerateShareStoreResult[] share = new GenerateShareStoreResult[1];
            thresholdKey.generateNewShare(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock.countDown();
            });
            lock.await();
            CountDownLatch lock1 = new CountDownLatch(1);
            thresholdKey.addShareDescription(share[0].getIndex(), "Device share 2", true, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not add share description for tkey");
                }
                lock1.countDown();
            });
            lock1.await();
            HashMap<String, ArrayList<String>> descriptions = thresholdKey.getShareDescriptions();
            ArrayList<String> description_specific = descriptions.get(share[0].getIndex());
            assert description_specific != null;
            assertTrue(description_specific.contains("Device share 2"));
            CountDownLatch lock2 = new CountDownLatch(1);
            thresholdKey.updateShareDescription(share[0].getIndex(), "Device share 2", "Emulator share", true, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not update share description for tkey");
                }
                lock2.countDown();
            });
            lock2.await();
            CountDownLatch lock3 = new CountDownLatch(1);
            thresholdKey.deleteShareDescription(share[0].getIndex(), "Emulator share", true, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not delete share description for tkey");
                }
                lock3.countDown();
            });
            lock3.await();
            System.gc();
        } catch (RuntimeError | JSONException | InterruptedException e) {
            fail(e.toString());
        }
    }
}
