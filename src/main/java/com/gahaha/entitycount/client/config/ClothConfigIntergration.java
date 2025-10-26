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
                    .setTitle(Text.translatable("entitycount"));

            builder.getOrCreateCategory(Text.translatable("entitycount.general"))
                    .addEntry(builder.entryBuilder()
                            .startBooleanToggle(Text.translatable("entitycount.config.master_toggle"), ConfigManager.isShowEntitiesCount())
                            .setDefaultValue(ConfigManager.Default.showEntitiesCount)
                            .setSaveConsumer(ConfigManager::setShowEntitiesCount)
                            .setTooltip(Text.translatable("entitycount.config.master_toggle.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStringDropdownMenu(Text.translatable("entitycount.config.display_mode"),
                                    ConfigManager.getEntityType().equals("Living") ? "Living" : "All")
                            .setDefaultValue(ConfigManager.Default.entityType)
                            .setSelections(java.util.List.of("All", "Living"))
                            .setSaveConsumer(ConfigManager::setEntityType)
                            .setTooltip(Text.translatable("entitycount.config.display_mode.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStringDropdownMenu(Text.translatable("entitycount.config.filter_mode"),
                                    ConfigManager.getListMode())
                            .setDefaultValue(ConfigManager.Default.listMode)
                            .setSelections(java.util.List.of("Blacklist", "Whitelist"))
                            .setSaveConsumer(ConfigManager::setListMode)
                            .setTooltip(Text.translatable("entitycount.config.filter_mode.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.translatable("entitycount.config.text_color"), ConfigManager.getTextColor())
                            .setDefaultValue(ConfigManager.Default.textColor)
                            .setSaveConsumer(ConfigManager::setTextColor)
                            .setTooltip(Text.translatable("entitycount.config.text_color.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.translatable("entitycount.config.background_color"), ConfigManager.getBackgroundColor())
                            .setDefaultValue(ConfigManager.Default.backgroundColor)
                            .setSaveConsumer(ConfigManager::setBackgroundColor)
                            .setTooltip(Text.translatable("entitycount.config.background_color.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startFloatField(Text.translatable("entitycount.config.text_scale"), ConfigManager.getScale())
                            .setDefaultValue(ConfigManager.Default.scale)
                            .setSaveConsumer(ConfigManager::setScale)
                            .setTooltip(Text.translatable("entitycount.config.text_scale.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.translatable("entitycount.config.type_limit"), ConfigManager.getMaxListLength(), -1, 50)
                            .setDefaultValue(ConfigManager.Default.maxListLength)
                            .setSaveConsumer(ConfigManager::setMaxListLength)
                            .setTooltip(Text.translatable("entitycount.config.type_limit.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.translatable("entitycount.config.threshold"), ConfigManager.getThreshold(), -1, 100)
                            .setDefaultValue(ConfigManager.Default.threshold)
                            .setSaveConsumer(ConfigManager::setThreshold)
                            .setTooltip(Text.translatable("entitycount.config.threshold.tooltip"))
                            .build()
                    )
                    .addEntry(new FloatSliderBuilder(Text.translatable("text.cloth-config.reset_value"), Text.translatable("entitycount.config.position_x"), ConfigManager.getX(), 0f, 1f)
                            .setDefaultValue(ConfigManager.Default.x)
                            .setSaveConsumer(ConfigManager::setX)
                            .setTooltip(Text.translatable("entitycount.config.position_x.tooltip"))
                            .build()
                    )
                    .addEntry(new FloatSliderBuilder(Text.translatable("text.cloth-config.reset_value"), Text.translatable("entitycount.config.position_y"), ConfigManager.getY(), 0f, 1f)
                            .setDefaultValue(ConfigManager.Default.y)
                            .setSaveConsumer(ConfigManager::setY)
                            .setTooltip(Text.translatable("entitycount.config.position_y.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStrList(Text.translatable("entitycount.config.whitelist"), ConfigManager.getWhiteList())
                            .setDefaultValue(ConfigManager.Default.whiteList)
                            .setSaveConsumer(ConfigManager::setWhiteList)
                            .setTooltip(Text.translatable("entitycount.config.whitelist.tooltip"))
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startStrList(Text.translatable("entitycount.config.blacklist"), ConfigManager.getBlackList())
                            .setDefaultValue(ConfigManager.Default.blackList)
                            .setSaveConsumer(ConfigManager::setBlackList)
                            .setTooltip(Text.translatable("entitycount.config.blacklist.tooltip"))
                            .build()
                    )
            ;
            return builder.build();
        };
    }
}
