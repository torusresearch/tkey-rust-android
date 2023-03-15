package com.web3auth.tkey.modules;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.Modules.ShareSerializationModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

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
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            tkeyShareSerializationModuleTest.thresholdKey = thresholdKey;
        } catch (RuntimeError e) {
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
            GenerateShareStoreResult share = thresholdKey.generateNewShare();
            String output = thresholdKey.outputShare(share.getIndex(), null);
            String serialized = ShareSerializationModule.serializeShare(thresholdKey, output, null);
            String deserialized = ShareSerializationModule.deserializeShare(thresholdKey, serialized, null);
            assertEquals(output, deserialized);

            GenerateShareStoreResult share2 = thresholdKey.generateNewShare();
            String output2 = thresholdKey.outputShare(share2.getIndex(), "mnemonic");
            String deserialized2 = ShareSerializationModule.deserializeShare(thresholdKey, output2, "mnemonic");
            String serialized2 = ShareSerializationModule.serializeShare(thresholdKey, deserialized2, "mnemonic");
            assertEquals(output2, serialized2);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }
}
