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
import com.web3auth.tkey.ThresholdKey.KeyReconstructionDetails;
import com.web3auth.tkey.ThresholdKey.Modules.SharetransferModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

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
            fail();
        }
    }

    @Test
    public void test() {
        try {
            String request = SharetransferModule.requestNewShare(thresholdKey2,"agent","[]");
            ArrayList<String> lookup = SharetransferModule.lookForRequest(thresholdKey);
            String encPubKey = lookup.get(0);
            GenerateShareStoreResult share = thresholdKey.generateNewShare();
            SharetransferModule.approveRequestWithShareIndex(thresholdKey,encPubKey,share.getIndex());
            SharetransferModule.requestStatusCheck(thresholdKey2, request, true);
            KeyReconstructionDetails k2 = thresholdKey2.reconstruct();
            assertEquals(k1.getKey(),k2.getKey());
        } catch (RuntimeError | JSONException e) {
            fail();
        }
    }
}
