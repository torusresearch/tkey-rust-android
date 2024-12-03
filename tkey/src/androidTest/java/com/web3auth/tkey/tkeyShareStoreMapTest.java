package com.web3auth.tkey;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.ShareStoreMap;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyShareStoreMapTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static ShareStoreMap details;

    @BeforeClass
    public static void setupTest() throws RuntimeException {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(3);
            thresholdKey.initialize(key.hex, null, false, false, false, result -> {
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
                    fail("Could not generate share for tkey");
                }
                try {
                    tkeyShareStoreMapTest.details = ((Result.Success<GenerateShareStoreResult>) result).data.getShareStoreMap();
                } catch (RuntimeError e) {
                    fail();
                }
                lock.countDown();
            });
            lock.await();
        } catch (RuntimeError | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void getShareStores() {
        try {
            assertNotEquals(details.getShareStores().size(), 0);
        } catch (RuntimeError | JSONException e) {
            fail(e.toString());
        }
    }
}
