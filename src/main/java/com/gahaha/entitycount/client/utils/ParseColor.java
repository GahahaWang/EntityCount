package com.gahaha.entitycount.client.utils;

public class ParseColor {
    public static int parseColorARGBString(String input) {
        try {
            if (input.startsWith("#")) {
                input = input.substring(1);
            }
            return (int)Long.parseLong(input, 16);
        } catch (NumberFormatException ignored) {
            return 0xFFFFFFFF;
        }
    }
}
