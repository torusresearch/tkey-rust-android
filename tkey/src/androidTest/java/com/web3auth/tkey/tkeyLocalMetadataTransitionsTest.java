package com.web3auth.tkey;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.LocalMetadataTransitions;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyLocalMetadataTransitionsTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static LocalMetadataTransitions details;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, true);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            GenerateShareStoreResult share = thresholdKey.generateNewShare();
            thresholdKey.deleteShare(share.getIndex());
            tkeyLocalMetadataTransitionsTest.details = thresholdKey.getLocalMetadataTransitions();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void export() {
        try {
            assertNotEquals(details.export().length(), 0);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @Test
    public void create() {
        try {
            LocalMetadataTransitions transitions = new LocalMetadataTransitions(details.export());
            assertNotEquals(transitions.export().length(), 0);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }
}
