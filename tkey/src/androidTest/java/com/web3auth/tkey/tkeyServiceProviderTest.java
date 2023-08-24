package com.web3auth.tkey;

import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.web3auth.tkey.ThresholdKey.Common.PrivateKey;
import com.web3auth.tkey.ThresholdKey.ServiceProvider;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.torusresearch.fetchnodedetails.FetchNodeDetails;
import org.torusresearch.fetchnodedetails.types.NodeDetails;
import org.torusresearch.fetchnodedetails.types.TorusNetwork;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    @Test
    public void providerWithTss() {
        try {
            String TORUS_TEST_EMAIL = "saasa2123@tr.us";
            String TORUS_TEST_VERIFIER = "torus-test-health";
            FetchNodeDetails nodeManager = new FetchNodeDetails(TorusNetwork.SAPPHIRE_DEVNET);

            CompletableFuture<NodeDetails> nodeDetailResult = nodeManager.getNodeDetails(TORUS_TEST_VERIFIER, TORUS_TEST_EMAIL);
            NodeDetails nodeDetail = nodeDetailResult.get();

            PrivateKey key = PrivateKey.generate();
            new ServiceProvider(false, key.hex,true, TORUS_TEST_VERIFIER,TORUS_TEST_EMAIL,nodeDetail);
            System.gc();
        } catch (RuntimeError | JSONException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
