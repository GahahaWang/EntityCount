package com.gahaha.entitycount.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.text.Text;

public class ModMenuIntergration implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("實體數量顯示器"));

            builder.getOrCreateCategory(Text.literal("General"))
                    .addEntry(builder.entryBuilder()
                            .startBooleanToggle(Text.literal("總開關"), ConfigManager.showEntitiesCount)
                            .setDefaultValue(true)
                            .setSaveConsumer(newValue -> ConfigManager.showEntitiesCount = newValue)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.literal("文字顏色"), ConfigManager.textColor)
                            .setDefaultValue(0xFFFFFFFF)
                            .setSaveConsumer(newVal -> ConfigManager.textColor = newVal)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startAlphaColorField(Text.literal("背景顏色"), ConfigManager.backgroundColor)
                            .setDefaultValue(0x3F808080)
                            .setSaveConsumer(newVal -> ConfigManager.backgroundColor = newVal)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("文字大小"), (int)(ConfigManager.scale*10), 1, 100)
                            .setDefaultValue(10)
                            .setSaveConsumer(newVal -> ConfigManager.scale = (float)newVal / 10)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("類型上限(-1無上限)"), ConfigManager.maxListLength, -1, 50)
                            .setDefaultValue(-1)
                            .setSaveConsumer(newVal -> ConfigManager.maxListLength = newVal)
                            .build()
                    )
                    .addEntry(builder.entryBuilder()
                            .startIntSlider(Text.literal("閾值(-1無下限)"), ConfigManager.threshold, -1, 100)
                            .setDefaultValue(-1)
                            .setSaveConsumer(newVal -> ConfigManager.threshold = newVal)
                            .build()
                    )
            ;
            return builder.build();
        };
    }
}
