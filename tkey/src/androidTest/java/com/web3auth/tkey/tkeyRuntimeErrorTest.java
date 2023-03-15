package com.web3auth.tkey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeyRuntimeErrorTest {
    @Test
    public void default_error() {
        try {
            RuntimeError e = new RuntimeError();
            throw e;
        } catch (RuntimeError e) {
            assertEquals(e.toString(), "RuntimeError{code=-1}");
        }
    }
}