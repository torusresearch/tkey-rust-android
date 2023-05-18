package com.web3auth.tkey.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Modules.SeedPhraseModule;
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
public class tkeySeedPhraseModuleTest {
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
                    fail("Could not input share for tkey");
                }
                lock.countDown();
            });
            lock.await();
            tkeySeedPhraseModuleTest.thresholdKey = thresholdKey;
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
            Constructor<SeedPhraseModule> constructor = SeedPhraseModule.class.getDeclaredConstructor();
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
            String phrase = "seed sock milk update focus rotate barely fade car face mechanic mercy";
            String phrase2 = "object brass success calm lizard science syrup planet exercise parade honey impulse";
            CountDownLatch lock = new CountDownLatch(1);
            SeedPhraseModule.setSeedPhrase(thresholdKey, "HD Key Tree", phrase, 5, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not set seed phrase for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock.countDown();
            });
            lock.await();
            assertNotEquals(SeedPhraseModule.getPhrases(thresholdKey).length(), 0);
            CountDownLatch lock1 = new CountDownLatch(3);
            SeedPhraseModule.deletePhrase(thresholdKey, phrase, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not set seed phrase for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock1.countDown();
            });
            SeedPhraseModule.setSeedPhrase(thresholdKey, "HD Key Tree", phrase, 0, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not set seed phrase for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock1.countDown();
            });
            SeedPhraseModule.changePhrase(thresholdKey, phrase, phrase2, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not set seed phrase for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock1.countDown();
            });
            lock1.await();
            ArrayList<JSONObject> list = thresholdKey.getTKeyStore("seedPhraseModule");
            String id = list.get(0).getString("id");
            String item = thresholdKey.getTKeyStoreItem("seedPhraseModule", id).getString("seedPhrase");
            assertEquals(phrase2, item);
        } catch (JSONException | RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }
}
