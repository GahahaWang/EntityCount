//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gahaha.entitycount.client.FloatSlider;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import me.shedaniel.clothconfig2.impl.builders.AbstractSliderFieldBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class FloatSliderBuilder extends AbstractSliderFieldBuilder<Float, FloatSliderEntry, FloatSliderBuilder> {
    public FloatSliderBuilder(Text resetButtonKey, Text fieldNameKey, float value, float min, float max) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
        this.max = max;
        this.min = min;
    }

    public FloatSliderBuilder setErrorSupplier(Function<Float, Optional<Text>> errorSupplier) {
        return (FloatSliderBuilder) super.setErrorSupplier(errorSupplier);
    }

    public FloatSliderBuilder requireRestart() {
        return (FloatSliderBuilder)super.requireRestart();
    }

    public FloatSliderBuilder setTextGetter(Function<Float, Text> textGetter) {
        return (FloatSliderBuilder)super.setTextGetter(textGetter);
    }

    public FloatSliderBuilder setSaveConsumer(Consumer<Float> saveConsumer) {
        return (FloatSliderBuilder)super.setSaveConsumer(saveConsumer);
    }

    public FloatSliderBuilder setDefaultValue(Supplier<Float> defaultValue) {
        return (FloatSliderBuilder)super.setDefaultValue(defaultValue);
    }

    public FloatSliderBuilder setDefaultValue(float defaultValue) {
        this.defaultValue = () -> {
            return defaultValue;
        };
        return this;
    }

    public FloatSliderBuilder setTooltipSupplier(Function<Float, Optional<Text[]>> tooltipSupplier) {
        return (FloatSliderBuilder)super.setTooltipSupplier(tooltipSupplier);
    }

    public FloatSliderBuilder setTooltipSupplier(Supplier<Optional<Text[]>> tooltipSupplier) {
        return (FloatSliderBuilder)super.setTooltipSupplier(tooltipSupplier);
    }

    public FloatSliderBuilder setTooltip(Optional<Text[]> tooltip) {
        return (FloatSliderBuilder) super.setTooltip(tooltip);
    }

    public FloatSliderBuilder setTooltip(Text... tooltip) {
        return (FloatSliderBuilder)super.setTooltip(tooltip);
    }

    public FloatSliderBuilder setMax(float max) {
        this.max = max;
        return this;
    }

    public FloatSliderBuilder setMin(float min) {
        this.min = min;
        return this;
    }

    public FloatSliderBuilder removeMin() {
        return this;
    }

    public FloatSliderBuilder removeMax() {
        return this;
    }

    public @NotNull FloatSliderEntry build() {
        FloatSliderEntry entry = new FloatSliderEntry(this.getFieldNameKey(), (Float)this.min, (Float)this.max, (Float)this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), (Supplier)null, this.isRequireRestart());
        if (this.textGetter != null) {
            entry.setTextGetter(this.textGetter);
        }

        entry.setTooltipSupplier(() -> {
            return (Optional)this.getTooltipSupplier().apply(entry.getValue());
        });
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> {
                return (Optional)this.errorSupplier.apply(entry.getValue());
            });
        }

        return (FloatSliderEntry) this.finishBuilding(entry);
    }
}
