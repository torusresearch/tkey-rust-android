package com.web3auth.tkey.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.Modules.ShareSerializationModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.CountDownLatch;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyShareSerializationModuleTest {
    static {
        System.loadLibrary("tkey-native");
    }

    private static ThresholdKey thresholdKey;

    @BeforeClass
    public static void setupTest() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex,false, null,null,null,null,null);
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
            tkeyShareSerializationModuleTest.thresholdKey = thresholdKey;
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void testConstructorIsPrivate() {
        try {
            Constructor<ShareSerializationModule> constructor = ShareSerializationModule.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (InvocationTargetException | IllegalAccessException |
                 InstantiationException ignored) {

        } catch (NoSuchMethodException e) {
            fail(e.toString());
        }
    }

    @Test
    public void test() {
        try {
            final GenerateShareStoreResult[] share = new GenerateShareStoreResult[2];
            CountDownLatch lock = new CountDownLatch(1);
            thresholdKey.generateNewShare(false, null, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                share[0] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock.countDown();
            });
            lock.await();
            String output = thresholdKey.outputShare(share[0].getIndex(), null);
            String serialized = ShareSerializationModule.serializeShare(thresholdKey, output, null);
            String deserialized = ShareSerializationModule.deserializeShare(thresholdKey, serialized, null);
            assertEquals(output, deserialized);

            CountDownLatch lock1 = new CountDownLatch(1);
            thresholdKey.generateNewShare(false, null,result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                share[1] = ((Result.Success<GenerateShareStoreResult>) result).data;
                lock1.countDown();
            });
            lock1.await();
            String output2 = thresholdKey.outputShare(share[1].getIndex(), "mnemonic");
            String deserialized2 = ShareSerializationModule.deserializeShare(thresholdKey, output2, "mnemonic");
            String serialized2 = ShareSerializationModule.serializeShare(thresholdKey, deserialized2, "mnemonic");
            assertEquals(output2, serialized2);
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }
}