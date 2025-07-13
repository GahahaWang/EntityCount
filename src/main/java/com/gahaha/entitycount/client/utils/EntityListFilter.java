package com.gahaha.entitycount.client.utils;

import com.gahaha.entitycount.client.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityListFilter {
    public static List<Map.Entry<String, Integer>> getProcessedList(Map<String, Integer> sourceMap) {
        // 排序
        List<Map.Entry<String, Integer>> sortedList = sourceMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                // threshold >= 0 時，僅保留 value >= threshold
                .filter(e -> ConfigManager.getThreshold() < 0 || e.getValue() >= ConfigManager.getThreshold())
                .toList();

        // 若有設定 maxListLength
        if (ConfigManager.getMaxListLength() > 0 && sortedList.size() > ConfigManager.getMaxListLength()) {
            return new ArrayList<>(sortedList.subList(0, ConfigManager.getMaxListLength()));
        }
        return new ArrayList<>(sortedList);
    }
}
