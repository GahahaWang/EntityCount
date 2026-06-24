package cc.gahaha.entitycount.utils;

import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.utils.Enums.*;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityListFilter {
    public static List<Object2IntOpenHashMap.Entry<ExtendString>> getProcessedList(Object2IntOpenHashMap<ExtendString> sourceMap) {
        List<ExtendString> pinnedList = ConfigManager.getPinnedList();

        // 分離固定項目和非固定項目
        List<Object2IntOpenHashMap.Entry<ExtendString>> pinnedEntries = new ArrayList<>();
        List<Object2IntOpenHashMap.Entry<ExtendString>> unpinnedEntries = new ArrayList<>();

        // 首先為所有 pinned 項目創建條目（根據 pinnedShowEvenZero 設定決定是否包含數量為 0 的項目）
        for (ExtendString pinnedEntityName : pinnedList) {
            // 根據黑白名單模式過濾
            if (!processListMode(pinnedEntityName)) {
                continue;
            }

            int count = sourceMap.getInt(pinnedEntityName);
            if (ConfigManager.isPinnedShowEvenZero() || count > 0) {
                String tmp = pinnedEntityName.value();
                tmp  = "📌 " + tmp;
                ExtendString displayName = new ExtendString(tmp, pinnedEntityName.displayEntryEntityType());
                pinnedEntries.add(new ExtendString2IntEntry( displayName, count));
            }
        }

        // 處理非固定項目
        for (Object2IntOpenHashMap.Entry<ExtendString> entry : sourceMap.object2IntEntrySet()) {
            if (!pinnedList.contains(entry.getKey())) {
                // 根據黑白名單模式過濾
                if (processListMode(entry.getKey())) {
                    unpinnedEntries.add(entry);
                }
            }
        }

        // 對固定項目和非固定項目分別排序
        // 固定項目不受 threshold 條件影響
        pinnedEntries = pinnedEntries.stream()
                .sorted((a, b) -> Integer.compare(b.getIntValue(), a.getIntValue()) )
                .collect(Collectors.toList());

        // 非固定項目受 threshold 條件影響
        unpinnedEntries = unpinnedEntries.stream()
                .sorted((a, b) -> Integer.compare(b.getIntValue(), a.getIntValue()) )
                .filter(e -> ConfigManager.getThreshold() < 0 || e.getIntValue() >= ConfigManager.getThreshold())
                .collect(Collectors.toList());

        // 合併列表：固定項目在前，非固定項目在後
        List<Object2IntOpenHashMap.Entry<ExtendString>> resultList = new ArrayList<>();
        resultList.addAll(pinnedEntries);
        resultList.addAll(unpinnedEntries);

        // 若有設定 maxListLength
        if (ConfigManager.getMaxListLength() > 0 && resultList.size() > ConfigManager.getMaxListLength()) {
            return new ArrayList<>(resultList.subList(0, ConfigManager.getMaxListLength()));
        }
        return resultList;
    }

    private static boolean processListMode(ExtendString entityName) {
        String filterMode = ConfigManager.getListMode();
        var whiteListNormal = ConfigManager.getWhiteListNormal();
        var whiteListItem = ConfigManager.getWhiteListItem();
        var blackListNormal = ConfigManager.getBlackListNormal();
        var blackListItem = ConfigManager.getBlackListItem();

        if (FilterMode.WHITELIST.str.equals(filterMode)) {
            // 白名單模式
            if (entityName.displayEntryEntityType().equals(DisplayEntryEntityType.ITEM)) {
                return whiteListItem.contains(entityName.value());
            } else {
                return whiteListNormal.contains(entityName.value());
            }
        } else if (FilterMode.BLACKLIST.str.equals(filterMode)) {
            // 黑名單模式
            if (entityName.displayEntryEntityType().equals(DisplayEntryEntityType.ITEM)) {
                return !blackListItem.contains(entityName.value());
            } else {
                return !blackListNormal.contains(entityName.value());
            }
        }
        return true;
    }
}
