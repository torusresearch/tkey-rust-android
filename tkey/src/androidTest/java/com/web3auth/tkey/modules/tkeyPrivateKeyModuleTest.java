package com.web3auth.tkey.modules;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.Modules.PrivateKeysModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;
import com.web3auth.tkey.tkeyGenerateShareStoreResultTest;

import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyPrivateKeyModuleTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static ThresholdKey thresholdKey;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            tkeyPrivateKeyModuleTest.thresholdKey = thresholdKey;
        } catch (RuntimeError e) {
            fail();
        }
    }

    @Test
    public void test() {
        try {
            PrivateKey key = PrivateKey.generate();
            PrivateKeysModule.setPrivateKey(thresholdKey, key.hex, "secp256k1n");
            String keys = PrivateKeysModule.getPrivateKeys(thresholdKey);
            assertNotEquals(keys.length(),0);
            ArrayList<String> accounts = PrivateKeysModule.getPrivateKeyAccounts(thresholdKey);
            assertNotEquals(accounts.size(),0);
        } catch (RuntimeError | JSONException e) {
            fail();
        }
    }
}
