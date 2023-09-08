package com.web3auth.tkey.ThresholdKey.Common;

import java.util.List;

public class TSSPubKeyResult {
    public static class Point {
        public String x;
        public String y;

        public String toFullAddr() {
            return "04" + x + y;
        }

        public Point(String x, String y) {
            this.x = x;
            this.y = y;
        }
    }

    public Point publicKey;
    public List<Integer> nodeIndexes;

    public TSSPubKeyResult(Point publicKey, List<Integer> nodeIndexes) {
        this.publicKey = publicKey;
        this.nodeIndexes = nodeIndexes;
    }
}