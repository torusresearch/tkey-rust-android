package com.web3auth.tkey_android_distribution;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey_android_distribution.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey_android_distribution.ThresholdKey.KeyDetails;
import com.web3auth.tkey_android_distribution.ThresholdKey.ServiceProvider;
import com.web3auth.tkey_android_distribution.ThresholdKey.StorageLayer;
import com.web3auth.tkey_android_distribution.ThresholdKey.ThresholdKey;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TKeyInstrumentedTest {
    static {
        System.loadLibrary("native-lib");
    }

    @Test
    public void get_version() throws RuntimeError {
        assertNotEquals(Version.current().length(), 0);
    }

    @Test
    public void generate_private_key() {
        try {
            PrivateKey key = PrivateKey.generate();
            assertNotEquals(key.hex.length(), 0);
            PrivateKey key2 = new PrivateKey(key.hex);
            assertEquals(key.hex, key2.hex);
        } catch (RuntimeError e) {
            fail();
        }
    }

    @Test
    public void basic_threshold_key_tests() {
        try {
            PrivateKey postboxKey = PrivateKey.generate();
            StorageLayer storageLayer = new StorageLayer(false, "https://metadata.tor.us/", 2);
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            KeyDetails details = thresholdKey.initialize(key.hex, null, false, false);
        } catch (RuntimeError e) {
            fail();
        } catch (NoSuchMethodException e) {
            fail();
        }
    }
}