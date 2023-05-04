package com.web3auth.tkey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.KeyPoint;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.KeyPointArray;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyKeyPointArrayTest {
    static {
        System.loadLibrary("tkey-native");
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void key_point_array() {
        try {
            KeyPointArray arr = new KeyPointArray();
            assertEquals(0, arr.length());
            KeyPoint point = new KeyPoint(PrivateKey.generate().hex, PrivateKey.generate().hex);
            KeyPoint point2 = new KeyPoint(PrivateKey.generate().hex, PrivateKey.generate().hex);
            arr.insert(point);
            assertEquals(1, arr.length());
            arr.insert(point);
            assertEquals(2, arr.length());
            arr.insert(point);
            assertEquals(3, arr.length());
            arr.removeAt(0);
            assertEquals(2, arr.length());
            arr.updateAt(point2, 1);
            KeyPoint ret1 = arr.getAt(0);
            KeyPoint ret2 = arr.getAt(1);
            assertNotEquals(ret1, ret2);
            arr.lagrange();
        } catch (RuntimeError e) {
            fail(e.toString());
        }
    }
}