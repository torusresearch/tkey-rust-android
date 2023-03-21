package com.web3auth.tkey;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.LocalMetadataTransitions;
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
            CountDownLatch lock = new CountDownLatch(4);
            thresholdKey.initialize(key.hex, null, false, false, result -> {
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
                    fail("Could not generate new share for tkey");
                }

                GenerateShareStoreResult share = ((Result.Success<GenerateShareStoreResult>) result).data;
                String index = null;
                try {
                    index = share.getIndex();
                } catch (RuntimeError e) {
                    fail(e.toString());
                }
                thresholdKey.deleteShare(index, result1 -> {
                    if (result1 instanceof Result.Error) {
                        fail("Could not generate new share for tkey");
                    }
                    lock.countDown();
                });
                lock.countDown();
            });
            lock.await();
            tkeyLocalMetadataTransitionsTest.details = thresholdKey.getLocalMetadataTransitions();
        } catch (RuntimeError | InterruptedException e) {
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
