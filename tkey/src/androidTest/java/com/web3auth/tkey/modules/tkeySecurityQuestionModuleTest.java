package com.web3auth.tkey.modules;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.Modules.SecurityQuestionModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

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
            ServiceProvider serviceProvider = new ServiceProvider(false, postboxKey.hex);
            ThresholdKey thresholdKey = new ThresholdKey(null, null, storageLayer, serviceProvider, null, null, false, false);
            PrivateKey key = PrivateKey.generate();
            thresholdKey.initialize(key.hex, null, false, false);
            thresholdKey.reconstruct();
            tkeySecurityQuestionModuleTest.thresholdKey = thresholdKey;
        } catch (RuntimeError e) {
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
            SecurityQuestionModule.generateNewShare(thresholdKey, question, answer);
            assertEquals(question, SecurityQuestionModule.getQuestions(thresholdKey));
            assertEquals(true, SecurityQuestionModule.inputShare(thresholdKey, answer));
            assertEquals(answer, SecurityQuestionModule.getAnswer(thresholdKey));
            assertEquals(true, SecurityQuestionModule.changeSecurityQuestionAndAnswer(thresholdKey, question, answer_2));
            assertEquals(answer_2, SecurityQuestionModule.getAnswer(thresholdKey));
            assertEquals(true, SecurityQuestionModule.storeAnswer(thresholdKey, answer_2));
            ArrayList<JSONObject> list = thresholdKey.getTKeyStore("securityQuestions");
            String id = list.get(0).getString("id");
            String item = thresholdKey.getTKeyStoreItem("securityQuestions", id).getString("answer");
            assertEquals(answer_2, item);
        } catch (JSONException | RuntimeError e) {
            fail(e.toString());
        }
    }
}
