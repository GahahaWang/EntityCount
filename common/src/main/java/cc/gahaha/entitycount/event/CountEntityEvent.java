package cc.gahaha.entitycount.event;

import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.utils.DisplayEntryEntityType;
import cc.gahaha.entitycount.utils.ExtendString;
import cc.gahaha.entitycount.utils.ExtendString2IntEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.List;

public class CountEntityEvent{

    public static final List<Object2IntMap.Entry<ExtendString>> defaultList = List.of(
            ExtendString2IntEntry.of("Gahaha", DisplayEntryEntityType.NORMAL, 1),
            ExtendString2IntEntry.of("Is", DisplayEntryEntityType.NORMAL, 1),
            ExtendString2IntEntry.of("So", DisplayEntryEntityType.ITEM, 1),
            ExtendString2IntEntry.of("Handsome", DisplayEntryEntityType.ITEM, 1)
    );

    public static final Object2IntOpenHashMap<ExtendString> entityCountMap = new Object2IntOpenHashMap<>(30);

    public static void updateEntityCount() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;
        Iterable<Entity> entities = client.level.entitiesForRendering();
        entityCountMap.clear();

        String entityType = ConfigManager.getEntityType();

        for (Entity entity : entities) {
            // 根據實體類型過濾實體
            if ("Living".equals(entityType) && !(entity instanceof LivingEntity)) {
                continue; // 跳過非生物實體
            }

            String classTranslationKey = entity.getType().toString();
            String className = Component.translatable(classTranslationKey).getString();

            boolean isItemEntity = entity instanceof ItemEntity;

            // 如果是物品實體且啟用了擴展顯示，額外記錄物品類型
            if (isItemEntity && ConfigManager.isExpandItemDisplay()) {
                String itemName = Component.translatable(((ItemEntity)entity).getItem().getItem().getDescriptionId()).getString();
                entityCountMap.addTo(new ExtendString(itemName, DisplayEntryEntityType.ITEM), ((ItemEntity)entity).getItem().getCount());
            } else {
                entityCountMap.addTo(new ExtendString(className, DisplayEntryEntityType.NORMAL), 1);
            }
        }
    }
}
