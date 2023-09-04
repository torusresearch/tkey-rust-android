package com.web3auth.tkey.ThresholdKey.Common;

import java.util.List;

public class GetTSSPubKeyResult {
    public static class Point {
        public String x;
        public String y;

        public String toFullAddress() {
            return "04" + x + y;
        }

        public Point(String x, String y) {
            this.x = x;
            this.y = y;
        }
    }

    public Point publicKey;
    public List<Integer> nodeIndexes;

    /**
     * Instantiate a GetTSSPubKeyResult object using the public key and nodeIndexes.
     * @param publicKey The publicKey from torus utils getPublicAddress
     * @param nodeIndexes The node indexes from torus utils nodes data
     */
    public GetTSSPubKeyResult(Point publicKey, List<Integer> nodeIndexes) {
        this.publicKey = publicKey;
        this.nodeIndexes = nodeIndexes;
    }
}