package com.gahaha.entitycount.client.utils;

public class ParseScale {
    public static float parseScaleString(String input) {
        try {
            return Float.parseFloat(input);

        }
        catch (NumberFormatException ignored) {
            return 1.0F;
        }
    }
}
