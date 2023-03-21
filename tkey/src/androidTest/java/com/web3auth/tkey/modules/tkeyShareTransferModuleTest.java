package com.web3auth.tkey.modules;

import androidx.core.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.KeyReconstructionDetails;
import com.web3auth.tkey.ThresholdKey.Modules.SharetransferModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.ShareTransferStore;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

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
            thresholdKey.initialize(null, null, false, false);
            tkeyShareTransferModuleTest.k1 = thresholdKey.reconstruct();
            tkeyShareTransferModuleTest.thresholdKey = thresholdKey;
            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            thresholdKey2.initialize(null, null, true, false);
            tkeyShareTransferModuleTest.thresholdKey2 = thresholdKey2;
        } catch (RuntimeError e) {
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
            SharetransferModule.requestNewShare(thresholdKey2, "agent", "[]");
            //encryption key should not be empty after a request is made
            assertNotEquals("", SharetransferModule.getCurrentEncryptionKey(thresholdKey2));
            ShareTransferStore transfer_store = SharetransferModule.getStore(thresholdKey);
            ArrayList<String> lookup = SharetransferModule.lookForRequest(thresholdKey);
            String encPubKey = lookup.get(0);
            SharetransferModule.deleteStore(thresholdKey, encPubKey);
            SharetransferModule.setStore(thresholdKey, transfer_store);
            SharetransferModule.getStore(thresholdKey);
            SharetransferModule.cleanupRequest(thresholdKey2);
            //encryption key should be empty after request is cleaned up
            assertEquals("", SharetransferModule.getCurrentEncryptionKey(thresholdKey2));
        } catch (RuntimeError | JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_approve_with_index() {
        try {
            String request = SharetransferModule.requestNewShare(thresholdKey2, "agent", "[]");
            ArrayList<String> lookup = SharetransferModule.lookForRequest(thresholdKey);
            String encPubKey = lookup.get(0);
            GenerateShareStoreResult share = thresholdKey.generateNewShare();
            SharetransferModule.approveRequestWithShareIndex(thresholdKey, encPubKey, share.getIndex());
            SharetransferModule.requestStatusCheck(thresholdKey2, request, false);
            SharetransferModule.cleanupRequest(thresholdKey2);
            KeyReconstructionDetails k2 = thresholdKey2.reconstruct();
            assertEquals(k1.getKey(), k2.getKey());
        } catch (RuntimeError | JSONException e) {
            fail(e.toString());
        }
    }

    @Test
    public void test_approve() {
        try {
            String request = SharetransferModule.requestNewShare(thresholdKey2, "agent", "[]");
            SharetransferModule.addCustomInfoToRequest(thresholdKey2, request, "device 2 share");
            ArrayList<String> lookup = SharetransferModule.lookForRequest(thresholdKey);
            String encPubKey = lookup.get(0);
            GenerateShareStoreResult share = thresholdKey.generateNewShare();
            ShareStore store = null;
            for (Pair<String, ShareStore> share_store : share.getShareStoreMap().getShareStores()) {
                if (share_store.first.equals(share.getIndex())) {
                    store = share_store.second;
                }
            }
            assertNotEquals(store, null);
            SharetransferModule.approveRequest(thresholdKey, encPubKey, store);
            SharetransferModule.requestStatusCheck(thresholdKey2, request, true);
            KeyReconstructionDetails k2 = thresholdKey2.reconstruct();
            assertEquals(k1.getKey(), k2.getKey());
        } catch (RuntimeError | JSONException e) {
            fail(e.toString());
        }
    }
}
