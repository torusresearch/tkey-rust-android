package com.web3auth.tkey.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Modules.PrivateKeysModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
            CountDownLatch lock = new CountDownLatch(2);
            thresholdKey.initialize(key.hex, null, false, false,false, result -> {
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
            tkeyPrivateKeyModuleTest.thresholdKey = thresholdKey;
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
            Constructor<PrivateKeysModule> constructor = PrivateKeysModule.class.getDeclaredConstructor();
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
            PrivateKey key = PrivateKey.generate();
            CountDownLatch lock = new CountDownLatch(1);
            PrivateKeysModule.setPrivateKey(thresholdKey, key.hex, "secp256k1n", result -> {
                if (result instanceof Result.Error) {
                    fail("Could not reconstruct tkey");
                }
                lock.countDown();
            });
            lock.await();
            String keys = PrivateKeysModule.getPrivateKeys(thresholdKey);
            assertNotEquals(keys.length(), 0);
            ArrayList<String> accounts = PrivateKeysModule.getPrivateKeyAccounts(thresholdKey);
            assertNotEquals(accounts.size(), 0);
            ArrayList<JSONObject> list = thresholdKey.getTKeyStore("privateKeyModule");
            String id = list.get(0).getString("id");
            String item = thresholdKey.getTKeyStoreItem("privateKeyModule", id).getString("privateKey");
            assertEquals(key.hex, item);
        } catch (RuntimeError | JSONException | InterruptedException e) {
            fail(e.toString());
        }
    }
}
