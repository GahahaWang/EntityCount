package cc.gahaha.entitycount.event;

import cc.gahaha.entitycount.config.ConfigManager;
import cc.gahaha.entitycount.utils.Enums.*;
import cc.gahaha.entitycount.utils.ExtendString;
import cc.gahaha.entitycount.utils.ExtendString2IntEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ChunkPos;

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

        boolean countLivingEntity = ConfigManager.isCountLivingEntity();
        boolean countNonLivingEntity = ConfigManager.isCountNonLivingEntity();

        int range = ConfigManager.getCountRange();
        ChunkPos playerChunk = client.player.chunkPosition();

        for (Entity entity : entities) {

            ChunkPos entityChunk = entity.chunkPosition();
            // Chebyshev distance
            if (Math.max(Math.abs(entityChunk.x() - playerChunk.x()), Math.abs(entityChunk.z() - playerChunk.z())) > range) {
                continue;
            }

            boolean isItemEntity = entity instanceof ItemEntity;

            // 物品實體獨立處理，永遠計算，不受生物/非生物開關影響
            // 其餘實體依生物與非生物各自獨立的開關過濾
            if (!isItemEntity) {
                boolean isLivingEntity = entity instanceof LivingEntity;
                if (isLivingEntity ? !countLivingEntity : !countNonLivingEntity) {
                    continue;
                }
            }

            String classTranslationKey = entity.getType().toString();
            String className = Component.translatable(classTranslationKey).getString();

            // 如果是物品實體且啟用了擴展顯示，額外記錄物品類型
            if (isItemEntity && ConfigManager.isExpandItemDisplay()) {
                String itemName = Component.translatable(((ItemEntity)entity).getItem().getItem().getDescriptionId()).getString();
                entityCountMap.addTo(new ExtendString(itemName, DisplayEntryEntityType.ITEM), ((ItemEntity)entity).getItem().getCount());
            }
            entityCountMap.addTo(new ExtendString(className, DisplayEntryEntityType.NORMAL), 1);
        }
    }
}
