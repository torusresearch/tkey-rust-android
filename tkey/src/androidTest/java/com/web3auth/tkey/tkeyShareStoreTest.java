package com.web3auth.tkey;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.ShareStore;
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
public class tkeyShareStoreTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static ShareStore details;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            ArrayList<String> indexes = thresholdKey.getShareIndexes();
            assertNotEquals(indexes.size(), 0);
            String lastIndex = indexes.get(indexes.size() - 1);
            tkeyShareStoreTest.details = thresholdKey.outputShareStore(lastIndex, null);
        } catch (RuntimeError | JSONException e) {
            fail();
        }
    }

    @Test
    public void share() {
        try {
            assertNotEquals(details.share().length(), 0);
        } catch (RuntimeError e) {
            fail();
        }
    }

    @Test
    public void polynomialId() {
        try {
            assertNotEquals(details.polynomialId().length(), 0);
        } catch (RuntimeError e) {
            fail();
        }
    }

    @Test
    public void shareIndex() {
        try {
            assertNotEquals(details.shareIndex().length(), 0);
        } catch (RuntimeError e) {
            fail();
        }
    }

    @Test
    public void jsonify() {
        try {
            String json = details.toJsonString();
            assertNotEquals(json.length(), 0);
            ShareStore newStore = new ShareStore(json);
            assertEquals(details.shareIndex(), newStore.shareIndex());
        } catch (RuntimeError e) {
            fail();
        }
    }
}
