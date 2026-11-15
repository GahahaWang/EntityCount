package cc.gahaha.entitycount.utils;

import java.util.Arrays;

public class Enums {
    public enum FilterMode {
        BLACKLIST("Blacklist"),
        WHITELIST("Whitelist")
        ;
        public final String str;
        public static final String[] strs = Arrays.stream(values()).map(e -> e.str).toArray(String[]::new);
        FilterMode(String str) {
            this.str = str;
        }
    }
    public enum DisplayEntryEntityType {
        NORMAL,
        ITEM,
    }
}
