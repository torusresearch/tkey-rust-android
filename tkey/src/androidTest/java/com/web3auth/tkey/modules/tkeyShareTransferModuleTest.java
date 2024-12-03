package com.web3auth.tkey.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.core.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.KeyReconstructionDetails;
import com.web3auth.tkey.ThresholdKey.Modules.SharetransferModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.ShareTransferStore;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class tkeyShareTransferModuleTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static ThresholdKey thresholdKey, thresholdKey2;
    private static KeyReconstructionDetails k1;

    @BeforeClass
    public static void setupTest() {
        try {
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            PrivateKey postboxKey = PrivateKey.generate();
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            CountDownLatch lock = new CountDownLatch(2);
            thresholdKey.initialize(null, null, false, false,false, result ->
            {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey instance 1");
                }
                lock.countDown();
            });
            thresholdKey.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey instance 1");
                }
                k1 = ((Result.Success<KeyReconstructionDetails>) result).data;
                lock.countDown();
            });
            lock.await();
            tkeyShareTransferModuleTest.thresholdKey = thresholdKey;
            CountDownLatch lock1 = new CountDownLatch(1);
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            thresholdKey2.initialize(null, null, true, false,false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey instance 2");
                }
                lock1.countDown();
            });
            lock1.await();
            tkeyShareTransferModuleTest.thresholdKey2 = thresholdKey2;
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void testConstructorIsPrivate() {
        try {
            Constructor<SharetransferModule> constructor = SharetransferModule.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (InvocationTargetException | IllegalAccessException |
                 InstantiationException ignored) {

        } catch (NoSuchMethodException e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_share_transfer_store_and_encryption_key_retrieval() {
        try {
            //encryption key should be empty before a request is made
            assertEquals("", SharetransferModule.getCurrentEncryptionKey(thresholdKey2));
            CountDownLatch lock = new CountDownLatch(1);
            SharetransferModule.requestNewShare(thresholdKey2, "agent", "[]", result -> {
                if (result instanceof Result.Error) {
                    fail("Could not request new share for tkey instance 2");
                }
                lock.countDown();
            });
            lock.await();
            //encryption key should not be empty after a request is made
            assertNotEquals("", SharetransferModule.getCurrentEncryptionKey(thresholdKey2));
            CountDownLatch lock1 = new CountDownLatch(2);
            final ShareTransferStore[] transfer_store = new ShareTransferStore[1];
            SharetransferModule.getStore(thresholdKey, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not get store for tkey instance 1");
                }
                transfer_store[0] = ((Result.Success<ShareTransferStore>) result).data;
                lock1.countDown();
            });
            final AtomicReference<ArrayList<String>> lookup = new AtomicReference<ArrayList<String>>();
            SharetransferModule.lookForRequest(thresholdKey, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not look for request for tkey instance 1");
                }
                lookup.set(((Result.Success<ArrayList<String>>) result).data);
                lock1.countDown();
            });
            lock1.await();
            String encPubKey = lookup.get().get(0);
            CountDownLatch lock2 = new CountDownLatch(3);
            SharetransferModule.deleteStore(thresholdKey, encPubKey, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not delete store for tkey instance 1");
                }
                lock2.countDown();
            });
            SharetransferModule.setStore(thresholdKey, transfer_store[0], result -> {
                if (result instanceof Result.Error) {
                    fail("Could not set store for tkey instance 1");
                }
                lock2.countDown();
            });
            SharetransferModule.getStore(thresholdKey, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not get store for tkey instance 1");
                }
                lock2.countDown();
            });
            lock2.await();
            SharetransferModule.cleanupRequest(thresholdKey2);
            //encryption key should be empty after request is cleaned up
            assertEquals("", SharetransferModule.getCurrentEncryptionKey(thresholdKey2));
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_approve_with_index() {
        try {
            AtomicReference<String> request = new AtomicReference<>("");
            CountDownLatch lock = new CountDownLatch(1);
            SharetransferModule.requestNewShare(thresholdKey2, "agent", "[]", result -> {
                if (result instanceof Result.Error) {
                    fail("Could not request new share for tkey instance 2");
                }
                request.set(((Result.Success<String>) result).data);
                lock.countDown();
            });
            lock.await();
            CountDownLatch lock1 = new CountDownLatch(1);
            AtomicReference<ArrayList<String>> lookup = new AtomicReference<>(new ArrayList<String>());
            SharetransferModule.lookForRequest(thresholdKey, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not look for request for for tkey instance 1");
                }
                lookup.set(((Result.Success<ArrayList<String>>) result).data);
                lock1.countDown();
            });
            lock1.await();
            String encPubKey = lookup.get().get(0);
            final GenerateShareStoreResult[] share = new GenerateShareStoreResult[1];
            CountDownLatch lock2 = new CountDownLatch(1);
            thresholdKey.generateNewShare(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not look for request for for tkey instance 1");
                }
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock2.countDown();
            });
            lock2.await();
            CountDownLatch lock3 = new CountDownLatch(1);
            SharetransferModule.approveRequestWithShareIndex(thresholdKey, encPubKey, share[0].getIndex(), result -> {
                if (result instanceof Result.Error) {
                    fail("Could approve request with share index for tkey instance 1");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock3.countDown();
            });
            lock3.await();
            CountDownLatch lock4 = new CountDownLatch(1);
            SharetransferModule.requestStatusCheck(thresholdKey2, request.get(), false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could approve request status chech for tkey instance 2");
                }
                lock4.countDown();
            });
            lock4.await();
            SharetransferModule.cleanupRequest(thresholdKey2);
            CountDownLatch lock5 = new CountDownLatch(1);
            final KeyReconstructionDetails[] k2 = new KeyReconstructionDetails[1];
            thresholdKey2.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey instance 2");
                }
                k2[0] = ((Result.Success<KeyReconstructionDetails>) result).data;
                lock5.countDown();
            });
            lock5.await();
            assertEquals(k1.getKey(), k2[0].getKey());
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_approve() {
        try {
            AtomicReference<String> request = new AtomicReference<>("");
            CountDownLatch lock = new CountDownLatch(1);
            SharetransferModule.requestNewShare(thresholdKey2, "agent", "[]", result -> {
                if (result instanceof Result.Error) {
                    fail("Could not request new share for tkey instance 2");
                }
                request.set(((Result.Success<String>) result).data);
                lock.countDown();
            });
            lock.await();
            CountDownLatch lock2 = new CountDownLatch(1);
            SharetransferModule.addCustomInfoToRequest(thresholdKey2, request.get(), "device 2 share", result -> {
                if (result instanceof Result.Error) {
                    fail("Could not add custom info to request for tkey instance 2");
                }
                lock2.countDown();
            });
            lock2.await();
            CountDownLatch lock3 = new CountDownLatch(1);
            AtomicReference<ArrayList<String>> lookup = new AtomicReference<>(new ArrayList<>());
            SharetransferModule.lookForRequest(thresholdKey, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not look for request for tkey instance 1");
                }
                lookup.set(((Result.Success<ArrayList<String>>) result).data);
                lock3.countDown();
            });
            lock3.await();
            CountDownLatch lock4 = new CountDownLatch(1);
            String encPubKey = lookup.get().get(0);
            final GenerateShareStoreResult[] share = new GenerateShareStoreResult[1];
            thresholdKey.generateNewShare(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not look for request for tkey instance 1");
                }
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock4.countDown();
            });
            lock4.await();
            ShareStore store = null;
            for (Pair<String, ShareStore> share_store : share[0].getShareStoreMap().getShareStores()) {
                if (share_store.first.equals(share[0].getIndex())) {
                    store = share_store.second;
                }
            }
            assertNotEquals(store, null);
            CountDownLatch lock5 = new CountDownLatch(1);
            SharetransferModule.approveRequest(thresholdKey, encPubKey, store, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not approve request for tkey instance 1");
                }
                lock5.countDown();
            });
            lock5.await();
            CountDownLatch lock6 = new CountDownLatch(2);
            SharetransferModule.requestStatusCheck(thresholdKey2, request.get(), true, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not request status check for tkey instance 2");
                }
                lock6.countDown();
            });
            final KeyReconstructionDetails[] k2 = new KeyReconstructionDetails[1];
            thresholdKey2.reconstruct(result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey instance 2");
                }
                k2[0] = ((Result.Success<KeyReconstructionDetails>) result).data;
                lock6.countDown();
            });
            lock6.await();
            assertEquals(k1.getKey(), k2[0].getKey());
        } catch (RuntimeError | JSONException | InterruptedException e) {
            fail(e.toString());
        }
    }
}