package com.web3auth.tkey_android_distribution;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey_android_distribution.ThresholdKey.Common.PrivateKey;

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
    public void generate_private_key() throws RuntimeError {
        PrivateKey key = PrivateKey.generate();
        assertNotEquals(key.hex.length(), 0);
        PrivateKey key2 = new PrivateKey(key.hex);
        assertEquals(key.hex, key2.hex);
    }
}