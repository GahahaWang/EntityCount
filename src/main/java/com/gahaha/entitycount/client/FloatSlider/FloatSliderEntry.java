//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gahaha.entitycount.client.FloatSlider;

import com.gahaha.entitycount.client.utils.AtomicFloat;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.ApiStatus.Internal;

@Environment(EnvType.CLIENT)
public class FloatSliderEntry extends TooltipListEntry<Float> {
    protected Slider sliderWidget;
    protected ButtonWidget resetButton;
    protected AtomicFloat value;
    protected final float orginial;
    private float minimum;
    private float maximum;
    private final Supplier<Float> defaultValue;
    private Function<Float, Text> textGetter;
    private final List<ClickableWidget> widgets;

    /** @deprecated */
    @Deprecated
    @Internal
    public FloatSliderEntry(Text fieldName, int minimum, int maximum, int value, Text resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer) {
        this(fieldName, minimum, maximum, value, resetButtonKey, defaultValue, saveConsumer, (Supplier)null);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public FloatSliderEntry(Text fieldName, int minimum, int maximum, float value, Text resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Supplier<Optional<Text[]>> tooltipSupplier) {
        this(fieldName, minimum, maximum, value, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, false);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public FloatSliderEntry(Text fieldName, float minimum, float maximum, float value, Text resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Supplier<Optional<Text[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);
        this.textGetter = (Float) -> {
            return Text.literal(String.format("Value: %f", Float));
        };
        this.orginial = value;
        this.defaultValue = defaultValue;
        this.value = new AtomicFloat(value);
        this.saveCallback = saveConsumer;
        this.maximum = maximum;
        this.minimum = minimum;
        this.sliderWidget = new Slider(0, 0, 152, 20, ((double)this.value.get() - (double)minimum) / (double)Math.abs(maximum - minimum));
        this.resetButton = ButtonWidget.builder(resetButtonKey, (widget) -> {
            this.setValue((Float)defaultValue.get());
        }).dimensions(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(resetButtonKey) + 6, 20).build();
        this.sliderWidget.setMessage((Text)this.textGetter.apply(this.value.get()));
        this.widgets = Lists.newArrayList(new ClickableWidget[]{this.sliderWidget, this.resetButton});
    }

    public Function<Float, Text> getTextGetter() {
        return this.textGetter;
    }

    public FloatSliderEntry setTextGetter(Function<Float, Text> textGetter) {
        this.textGetter = textGetter;
        this.sliderWidget.setMessage((Text)textGetter.apply(this.value.get()));
        return this;
    }

    public Float getValue() {
        return this.value.get();
    }

    /** @deprecated */
    @Deprecated
    public void setValue(float value) {
        this.sliderWidget.setValue((double)(MathHelper.clamp(value, this.minimum, this.maximum) - this.minimum) / (double)Math.abs(this.maximum - this.minimum));
        this.value.set(Math.min(Math.max(value, this.minimum), this.maximum));
        this.sliderWidget.updateMessage();
    }

    public boolean isEdited() {
        return super.isEdited() || this.getValue() != this.orginial;
    }

    public Optional<Float> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable((Float)this.defaultValue.get());
    }

    public List<? extends Element> children() {
        return this.widgets;
    }

    public List<? extends Selectable> narratables() {
        return this.widgets;
    }

    public FloatSliderEntry setMaximum(float maximum) {
        this.maximum = maximum;
        return this;
    }

    public FloatSliderEntry setMinimum(float minimum) {
        this.minimum = minimum;
        return this;
    }

    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = MinecraftClient.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && (Float)this.defaultValue.get() != this.value.get();
        this.resetButton.setY(y);
        this.sliderWidget.active = this.isEditable();
        this.sliderWidget.setY(y);
        Text displayedFieldName = this.getDisplayedFieldName();
        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayedFieldName.asOrderedText(), window.getScaledWidth() - x - MinecraftClient.getInstance().textRenderer.getWidth(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x);
            this.sliderWidget.setX(x + this.resetButton.getWidth() + 1);
        } else {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayedFieldName.asOrderedText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x + entryWidth - this.resetButton.getWidth());
            this.sliderWidget.setX(x + entryWidth - 150);
        }

        this.sliderWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.render(graphics, mouseX, mouseY, delta);
        this.sliderWidget.render(graphics, mouseX, mouseY, delta);
    }

    private class Slider extends SliderWidget {
        protected Slider(int int_1, int int_2, int int_3, int int_4, double double_1) {
            super(int_1, int_2, int_3, int_4, Text.empty(), double_1);
        }

        public void updateMessage() {
            this.setMessage((Text)FloatSliderEntry.this.textGetter.apply(FloatSliderEntry.this.value.get()));
        }

        protected void applyValue() {
            FloatSliderEntry.this.value.set((float)((double)FloatSliderEntry.this.minimum + (double)Math.abs(FloatSliderEntry.this.maximum - FloatSliderEntry.this.minimum) * this.value));
        }

        public boolean keyPressed(int int_1, int int_2, int int_3) {
            return !FloatSliderEntry.this.isEditable() ? false : super.keyPressed(int_1, int_2, int_3);
        }

        public boolean mouseDragged(double double_1, double double_2, int int_1, double double_3, double double_4) {
            return !FloatSliderEntry.this.isEditable() ? false : super.mouseDragged(double_1, double_2, int_1, double_3, double_4);
        }

        public double getProgress() {
            return this.value;
        }

        public void setProgress(double Float) {
            this.value = Float;
        }

        public void setValue(double Float) {
            this.value = Float;
        }
    }
}
