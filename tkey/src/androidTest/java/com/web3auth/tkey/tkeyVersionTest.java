package com.web3auth.tkey;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyVersionTest {
    static {
        System.loadLibrary("tkey-native");
    }

    @Test
    public void get_version() throws RuntimeError {
        assertNotEquals(Version.current().length(), 0);
    }
}