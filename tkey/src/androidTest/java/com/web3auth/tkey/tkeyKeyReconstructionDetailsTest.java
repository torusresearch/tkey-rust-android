package com.web3auth.tkey;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.KeyReconstructionDetails;
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
public class tkeyKeyReconstructionDetailsTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static KeyReconstructionDetails details;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            tkeyKeyReconstructionDetailsTest.details = thresholdKey.reconstruct();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void getKey() {
        try {
            details.getKey();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @Test
    public void getAllKeys() {
        try {
            details.getAllKeys();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @Test
    public void getSeedPhrase() {
        try {
            details.getSeedPhrase();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }
}
