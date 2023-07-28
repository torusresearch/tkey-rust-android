package com.web3auth.tkey;

import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Polynomial;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
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
public class tkeyKeyPolynomialTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static Polynomial details;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex, false, null,null,null,null,null);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false, null);
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(2);
            thresholdKey.initialize(key.hex, null, false, false, false, null, 0, null, result -> {
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
            lock.await();
            details = thresholdKey.reconstructLatestPolynomial();
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void publicPolynomial() {
        try {
            details.getPublicPolynomial();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }

    @Test
    public void generateShares() {
        try {
            String indexes = "[\"c9022864e78c175beb9931ba136233fce416ece4c9af258ac9af404f7436c281\",\"8cd35d2d246e475de2413732c2d134d39bb51a1ed07cb5b1d461b5184c62c1b6\",\"6e0ab0cb7e47bdce6b08c043ee449d94c3addf33968ae79b4c8d7014238c46e4\"]";
            details.generateShares(indexes);
        } catch (RuntimeError | JSONException e) {
            fail(e.toString());
        }
    }
}
