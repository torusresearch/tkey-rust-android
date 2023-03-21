package com.web3auth.tkey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class tkeyPrivateKeyTest {
    static {
        System.loadLibrary("tkey-native");
    }

    @Test
    public void generate_private_key() {
        try {
            PrivateKey key = PrivateKey.generate();
            assertNotEquals(key.hex.length(), 0);
            PrivateKey key2 = new PrivateKey(key.hex);
            assertEquals(key.hex, key2.hex);
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }
}
