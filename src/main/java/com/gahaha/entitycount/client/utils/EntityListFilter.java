package com.gahaha.entitycount.client.utils;

import com.gahaha.entitycount.client.config.ConfigManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityListFilter {
    public static List<Map.Entry<String, Integer>> getProcessedList(Map<String, Integer> sourceMap) {
        List<String> pinnedList = ConfigManager.getPinnedList();
        
        // 分離固定項目和非固定項目
        List<Map.Entry<String, Integer>> pinnedEntries = new ArrayList<>();
        List<Map.Entry<String, Integer>> unpinnedEntries = new ArrayList<>();
        
        // 首先為所有 pinned 項目創建條目（即使數量為 0）
        for (String pinnedEntityName : pinnedList) {
            Integer count = sourceMap.getOrDefault(pinnedEntityName, 0);
            pinnedEntries.add(new AbstractMap.SimpleEntry<>(pinnedEntityName, count));
        }
        
        // 處理非固定項目
        for (Map.Entry<String, Integer> entry : sourceMap.entrySet()) {
            if (!pinnedList.contains(entry.getKey())) {
                unpinnedEntries.add(entry);
            }
        }
        
        // 對固定項目和非固定項目分別排序
        // 固定項目不受 threshold 條件影響
        pinnedEntries = pinnedEntries.stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());
        
        // 非固定項目受 threshold 條件影響
        unpinnedEntries = unpinnedEntries.stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .filter(e -> ConfigManager.getThreshold() < 0 || e.getValue() >= ConfigManager.getThreshold())
                .collect(Collectors.toList());
        
        // 合併列表：固定項目在前，非固定項目在後
        List<Map.Entry<String, Integer>> resultList = new ArrayList<>();
        resultList.addAll(pinnedEntries);
        resultList.addAll(unpinnedEntries);
        
        // 若有設定 maxListLength
        if (ConfigManager.getMaxListLength() > 0 && resultList.size() > ConfigManager.getMaxListLength()) {
            return new ArrayList<>(resultList.subList(0, ConfigManager.getMaxListLength()));
        }
        return resultList;
    }
}
