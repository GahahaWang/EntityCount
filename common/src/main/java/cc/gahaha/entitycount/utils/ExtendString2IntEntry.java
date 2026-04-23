package cc.gahaha.entitycount.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class ExtendString2IntEntry implements Object2IntMap.Entry<ExtendString> {
    ExtendString key;
    int value;

    public ExtendString2IntEntry(ExtendString key, int value) {
        this.key = key;
        this.value = value;
    }

    public static ExtendString2IntEntry of(Object2IntMap.Entry<ExtendString> entry) {
        return new ExtendString2IntEntry(entry.getKey(), entry.getIntValue());
    }

    public static ExtendString2IntEntry of(ExtendString key, int value) {
        return new ExtendString2IntEntry(key, value);
    }

    public static ExtendString2IntEntry of(String key, DisplayEntryEntityType type, int value) {
        return new ExtendString2IntEntry(new ExtendString(key, type), value);
    }

    @Override
    public int getIntValue() {
        return value;
    }

    @Override
    public int setValue(int value) {
        return this.value = value;
    }

    @Override
    public ExtendString getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExtendString2IntEntry other)) return false;
        return this.key.equals(other.getKey()) && this.value == other.value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ Integer.hashCode(value);
    }
}