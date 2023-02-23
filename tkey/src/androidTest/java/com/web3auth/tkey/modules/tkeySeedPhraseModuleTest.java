package com.web3auth.tkey.modules;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.web3auth.tkey.RuntimeError;
import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.GenerateShareStoreResult;
import com.web3auth.tkey.ThresholdKey.Modules.SeedPhraseModule;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;
import com.web3auth.tkey.ThresholdKey.StorageLayer;
import com.web3auth.tkey.ThresholdKey.ThresholdKey;
import com.web3auth.tkey.tkeyGenerateShareStoreResultTest;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class tkeySeedPhraseModuleTest {
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
            tkeySeedPhraseModuleTest.thresholdKey = thresholdKey;
        } catch (RuntimeError e) {
            fail();
        }
    }

    @Test
    public void test() {
        try {
            String phrase = "seed sock milk update focus rotate barely fade car face mechanic mercy";
            String phrase2 =  "object brass success calm lizard science syrup planet exercise parade honey impulse";
            SeedPhraseModule.setSeedPhrase(thresholdKey,"HD Key Tree",phrase,5);
            assertNotEquals(SeedPhraseModule.getPhrases(thresholdKey).length(), 0);
            SeedPhraseModule.deletePhrase(thresholdKey,phrase);
            SeedPhraseModule.setSeedPhrase(thresholdKey,"HD Key Tree",phrase,0);
            SeedPhraseModule.changePhrase(thresholdKey,phrase,phrase2);
        } catch (RuntimeError e) {
            fail();
        }
    }
}
