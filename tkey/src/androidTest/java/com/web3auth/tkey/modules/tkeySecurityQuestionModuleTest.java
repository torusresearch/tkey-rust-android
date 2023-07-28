package com.web3auth.tkey.modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Common.Result;
import com.web3auth.tkey.ThresholdKey.Modules.SecurityQuestionModule;
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
public class tkeySecurityQuestionModuleTest {
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
                    fail("Could not reconstruct tkey");
                }
                lock.countDown();
            });
            lock.await();
            tkeySecurityQuestionModuleTest.thresholdKey = thresholdKey;
        } catch (RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }

    @Test
    public void testConstructorIsPrivate() {
        try {
            Constructor<SecurityQuestionModule> constructor = SecurityQuestionModule.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (InvocationTargetException | IllegalAccessException |
                 InstantiationException ignored) {

        } catch (NoSuchMethodException e) {
            fail(e.toString());
        }
    }

    @AfterClass
    public static void cleanTest() {
        System.gc();
    }

    @Test
    public void test() {
        try {
            String question = "favorite marvel character";
            String answer = "iron man";
            String answer_2 = "captain america";
            CountDownLatch lock = new CountDownLatch(1);
            SecurityQuestionModule.generateNewShare(thresholdKey, question, answer, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not generate new share for tkey");
                }
                lock.countDown();
            });
            lock.await();
            assertEquals(question, SecurityQuestionModule.getQuestions(thresholdKey));
            CountDownLatch lock1 = new CountDownLatch(1);
            SecurityQuestionModule.inputShare(thresholdKey, answer, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not input share for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock1.countDown();
            });
            lock1.await();
            assertEquals(answer, SecurityQuestionModule.getAnswer(thresholdKey));
            CountDownLatch lock2 = new CountDownLatch(1);
            SecurityQuestionModule.changeSecurityQuestionAndAnswer(thresholdKey, question, answer_2, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not change security question and answer for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock2.countDown();
            });
            lock2.await();
            assertEquals(answer_2, SecurityQuestionModule.getAnswer(thresholdKey));
            CountDownLatch lock3 = new CountDownLatch(1);
            SecurityQuestionModule.storeAnswer(thresholdKey, answer_2, result -> {
                if (result instanceof Result.Error) {
                    fail("Could not input share for tkey");
                }
                assertEquals(true, ((Result.Success<Boolean>) result).data);
                lock3.countDown();
            });
            lock3.await();
            ArrayList<JSONObject> list = thresholdKey.getTKeyStore("securityQuestions");
            String id = list.get(0).getString("id");
            String item = thresholdKey.getTKeyStoreItem("securityQuestions", id).getString("answer");
            assertEquals(answer_2, item);
        } catch (JSONException | RuntimeError | InterruptedException e) {
            fail(e.toString());
        }
    }
}
