package com.gahaha.entitycount.client.config;

import com.gahaha.entitycount.client.FloatSlider.FloatSliderBuilder;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

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
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStringDropdownMenu(Text.literal("顯示模式"),
                                    ConfigManager.getMode().equals("Living") ? "Living" : "All")
                            .setDefaultValue(ConfigManager.Default.mode)
                            .setSelections(java.util.List.of("All", "Living"))
                            .setSaveConsumer(ConfigManager::setMode)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.literal("文字顏色"), ConfigManager.getTextColor())
                            .setDefaultValue(ConfigManager.Default.textColor)
                            .setSaveConsumer(ConfigManager::setTextColor)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.literal("背景顏色"), ConfigManager.getBackgroundColor())
                            .setDefaultValue(ConfigManager.Default.backgroundColor)
                            .setSaveConsumer(ConfigManager::setBackgroundColor)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startFloatField(Text.literal("文字大小"), (int)(ConfigManager.getScale()))
                            .setDefaultValue(ConfigManager.Default.scale)
                            .setSaveConsumer(ConfigManager::setScale)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("類型上限(-1無上限)"), ConfigManager.getMaxListLength(), -1, 50)
                            .setDefaultValue(ConfigManager.Default.maxListLength)
                            .setSaveConsumer(ConfigManager::setMaxListLength)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("閾值(-1無下限)"), ConfigManager.getThreshold(), -1, 100)
                            .setDefaultValue(ConfigManager.Default.threshold)
                            .setSaveConsumer(ConfigManager::setThreshold)
                            .build()
                    )
                    .addEntry(new FloatSliderBuilder(Text.translatable("text.cloth-config.reset_value"), Text.literal("x"), ConfigManager.getX(), 0f, 1f)
                            .setDefaultValue(ConfigManager.Default.x)
                            .setSaveConsumer(ConfigManager::setX)
                            .build()
                    )
                    .addEntry(new FloatSliderBuilder(Text.translatable("text.cloth-config.reset_value"), Text.literal("y"), ConfigManager.getY(), 0f, 1f)
                            .setDefaultValue(ConfigManager.Default.y)
                            .setSaveConsumer(ConfigManager::setY)
                            .build()
                    )
            ;
            return builder.build();
        };
    }
}
