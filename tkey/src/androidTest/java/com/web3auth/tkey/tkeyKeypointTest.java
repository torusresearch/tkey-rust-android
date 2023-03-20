package com.web3auth.tkey;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.KeyPoint;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.KeyDetails;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

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
public class tkeyKeypointTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static KeyPoint details;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(1);
            thresholdKey.initialize(key.hex, null, false, false, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not initialize tkey");
                }
                try {
                    tkeyKeypointTest.details = ((Result.Success<KeyDetails>) result).data.getPublicKeyPoint();
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                lock.countDown();
            });
            lock.await();
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void get_x() {
        try {
            assertNotEquals(details.getX().length(), 0);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @Test
    public void get_y() {
        try {
            assertNotEquals(details.getY().length(), 0);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @Test
    public void get_as_public_key() {
        try {
            assertNotEquals(details.getAsCompressedPublicKey("elliptic-compressed").length(), 0);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }
}
