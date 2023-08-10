package com.web3auth.tkey;

import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyServiceProviderTest {
    static {
        System.loadLibrary("tkey-native");
    }

    @Test
    public void provider() {
        try {
            PrivateKey key = PrivateKey.generate();
            new ServiceProvider(false, key.hex,false, null,null,null);
            System.gc();
        } catch (RuntimeError | JSONException e) {
            fail(e.toString());
        }
    }
}
