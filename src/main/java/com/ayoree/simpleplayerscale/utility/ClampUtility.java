package com.ayoree.simpleplayerscale.utility;

public class ClampUtility {

    private static final double MAX_POSSIBLE = 50;
    private static final double MIN_POSSIBLE = 0.01;

    public double clampScale(double value) {
        value = Math.min(MAX_POSSIBLE, value);
        value = Math.max(MIN_POSSIBLE, value);
        return value;
    }
}
