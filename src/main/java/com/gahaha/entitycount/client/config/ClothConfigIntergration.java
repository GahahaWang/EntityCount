package com.gahaha.entitycount.client.config;

import com.gahaha.entitycount.client.FloatSlider.FloatSliderBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ClothConfigIntergration implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("實體數量顯示器"));

            builder.getOrCreateCategory(Text.literal("General"))
                    .addEntry(builder.entryBuilder()
                            .startBooleanToggle(Text.literal("總開關"), ConfigManager.getShowEntitiesCount())
                            .setDefaultValue(ConfigManager.Default.showEntitiesCount)
                            .setSaveConsumer(ConfigManager::setShowEntitiesCount)
                            .setTooltip(Text.literal("開啟或關閉實體數量顯示功能"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStringDropdownMenu(Text.literal("顯示模式"),
                                    ConfigManager.getEntityType().equals("Living") ? "Living" : "All")
                            .setDefaultValue(ConfigManager.Default.entityType)
                            .setSelections(java.util.List.of("All", "Living"))
                            .setSaveConsumer(ConfigManager::setEntityType)
                            .setTooltip(Text.literal("All: 顯示所有實體 | Living: 僅顯示生物實體"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStringDropdownMenu(Text.literal("過濾模式"),
                                    ConfigManager.getListMode())
                            .setDefaultValue(ConfigManager.Default.listMode)
                            .setSelections(java.util.List.of("Blacklist", "Whitelist"))
                            .setSaveConsumer(ConfigManager::setListMode)
                            .setTooltip(Text.literal("Blacklist: 不顯示黑名單中的實體 | Whitelist: 僅顯示白名單中的實體"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.literal("文字顏色"), ConfigManager.getTextColor())
                            .setDefaultValue(ConfigManager.Default.textColor)
                            .setSaveConsumer(ConfigManager::setTextColor)
                            .setTooltip(Text.literal("設定實體數量顯示的文字顏色"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.literal("背景顏色"), ConfigManager.getBackgroundColor())
                            .setDefaultValue(ConfigManager.Default.backgroundColor)
                            .setSaveConsumer(ConfigManager::setBackgroundColor)
                            .setTooltip(Text.literal("設定實體數量顯示的背景顏色和透明度"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startFloatField(Text.literal("文字大小"), (int)(ConfigManager.getScale()))
                            .setDefaultValue(ConfigManager.Default.scale)
                            .setSaveConsumer(ConfigManager::setScale)
                            .setTooltip(Text.literal("調整實體數量顯示的文字縮放比例"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("類型上限(-1無上限)"), ConfigManager.getMaxListLength(), -1, 50)
                            .setDefaultValue(ConfigManager.Default.maxListLength)
                            .setSaveConsumer(ConfigManager::setMaxListLength)
                            .setTooltip(Text.literal("限制最多顯示多少種實體類型，-1表示無限制"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("閾值(-1無下限)"), ConfigManager.getThreshold(), -1, 100)
                            .setDefaultValue(ConfigManager.Default.threshold)
                            .setSaveConsumer(ConfigManager::setThreshold)
                            .setTooltip(Text.literal("只顯示數量大於等於此值的實體類型，-1表示無限制"))
                            .build()
                    )
                    .addEntry(new FloatSliderBuilder(Text.translatable("text.cloth-config.reset_value"), Text.literal("x"), ConfigManager.getX(), 0f, 1f)
                            .setDefaultValue(ConfigManager.Default.x)
                            .setSaveConsumer(ConfigManager::setX)
                            .setTooltip(Text.literal("調整實體數量顯示的水平位置 (0.0 = 左邊, 1.0 = 右邊)"))
                            .build()
                    )
                    .addEntry(new FloatSliderBuilder(Text.translatable("text.cloth-config.reset_value"), Text.literal("y"), ConfigManager.getY(), 0f, 1f)
                            .setDefaultValue(ConfigManager.Default.y)
                            .setSaveConsumer(ConfigManager::setY)
                            .setTooltip(Text.literal("調整實體數量顯示的垂直位置 (0.0 = 上方, 1.0 = 下方)"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStrList(Text.literal("白名單"), ConfigManager.getWhiteList())
                            .setDefaultValue(ConfigManager.Default.whiteList)
                            .setSaveConsumer(ConfigManager::setWhiteList)
                            .setTooltip(Text.literal("只顯示此列表中的實體 (當過濾模式為白名單時)"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStrList(Text.literal("黑名單"), ConfigManager.getBlackList())
                            .setDefaultValue(ConfigManager.Default.blackList)
                            .setSaveConsumer(ConfigManager::setBlackList)
                            .setTooltip(Text.literal("不顯示此列表中的實體 (當過濾模式為黑名單時)"))
                            .build()
                    )
            ;
            return builder.build();
        };
    }
}
