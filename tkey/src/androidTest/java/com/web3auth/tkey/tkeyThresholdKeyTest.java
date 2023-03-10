package com.web3auth.tkey;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.KeyDetails;
import com.web3auth.tkey.ThresholdKey.KeyReconstructionDetails;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

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
            KeyDetails details = thresholdKey.initialize(key.hex, null, false, false);
            assertNotNull(details.getPublicKeyPoint().getAsCompressedPublicKey("elliptic-compressed"));
            KeyReconstructionDetails reconstruct_details = thresholdKey.reconstruct();
            assertNotEquals(reconstruct_details.getKey().length(), 0);
        } catch (RuntimeError e) {
            fail();
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
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            thresholdKey.getKeyDetails();
            thresholdKey.getLastFetchedCloudMetadata();
            thresholdKey.getLocalMetadataTransitions();
            GenerateShareStoreResult share = thresholdKey.generateNewShare();
            String output = thresholdKey.outputShare(share.getIndex(), null);
            thresholdKey.outputShareStore(share.getIndex(), null);
            thresholdKey.shareToShareStore(output);
            thresholdKey.deleteShare(share.getIndex());

            GenerateShareStoreResult share2 = thresholdKey.generateNewShare();

            String input = thresholdKey.outputShare(share2.getIndex(), null);
            ShareStore inputStore = thresholdKey.outputShareStore(share2.getIndex(), null);

            ThresholdKey thresholdKey2 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            thresholdKey2.initialize(null, null, true, false);
            thresholdKey2.inputShare(input, null);
            thresholdKey2.reconstruct();

            ThresholdKey thresholdKey3 = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            thresholdKey3.initialize(null, null, true, false);
            thresholdKey3.inputShareStore(inputStore);
            thresholdKey3.reconstruct();
            thresholdKey3.deleteTKey();
        } catch (RuntimeError e) {
            fail();
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
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            thresholdKey.generateNewShare();
            thresholdKey.syncLocalMetadataTransitions();
            thresholdKey.reconstruct();
        } catch (RuntimeError e) {
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
            KeyDetails details = thresholdKey.initialize(key.hex, null, false, false);
            KeyDetails details2 = thresholdKey2.initialize(key2.hex, null, false, false);
            assertNotNull(details.getPublicKeyPoint().getAsCompressedPublicKey("elliptic-compressed"));
            assertNotNull(details2.getPublicKeyPoint().getAsCompressedPublicKey("elliptic-compressed"));
            KeyReconstructionDetails reconstruct_details = thresholdKey.reconstruct();
            KeyReconstructionDetails reconstruct_details2 = thresholdKey2.reconstruct();
            assertNotEquals(reconstruct_details.getKey().length(), 0);
            assertNotEquals(reconstruct_details2.getKey().length(), 0);
            assertNotEquals(reconstruct_details.getKey(), reconstruct_details2.getKey());
        } catch (RuntimeError e) {
            fail();
        }
    }
    @Test
    public void threshold_key_share_Description_test() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, true);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            String keystr = "test share";
            String old_description = "test share description";
            String new_description = "new test share description";
            thresholdKey.addShareDescription(keystr, old_description, false);
            String result1 = thresholdKey.getShareDescriptions();
            assertEquals(result1,"{\"test share\":[\"test share description\"]}");
            thresholdKey.updateShareDescription(keystr, old_description, new_description, false);
            String result2 = thresholdKey.getShareDescriptions();
            assertEquals(result2, "{\"test share\":[\"new test share description\"]}");
            thresholdKey.deleteShareDescription(keystr, new_description, false);
            String result3 = thresholdKey.getShareDescriptions();
            assertEquals(result3, "{\"test share\":[]}");
        } catch (RuntimeError e) {
            fail();
        }
    }
}