package cc.gahaha.entitycount.clothconfighook;

import cc.gahaha.entitycount.utils.AtomicFloat;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FloatSliderEntry extends TooltipListEntry<Float> {
    protected Slider sliderWidget;
    protected Button resetButton;
    protected AtomicFloat value;
    protected final float orginial;
    private float minimum;
    private float maximum;
    private final Supplier<Float> defaultValue;
    private Function<Float, Component> textGetter;
    private final List<AbstractWidget> widgets;

    /** @deprecated */
    @Deprecated
    @Internal
    public FloatSliderEntry(Component fieldName, int minimum, int maximum, int value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer) {
        this(fieldName, minimum, maximum, value, resetButtonKey, defaultValue, saveConsumer, (Supplier)null);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public FloatSliderEntry(Component fieldName, int minimum, int maximum, float value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, minimum, maximum, value, resetButtonKey, defaultValue, saveConsumer, tooltipSupplier, false);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public FloatSliderEntry(Component fieldName, float minimum, float maximum, float value, Component resetButtonKey, Supplier<Float> defaultValue, Consumer<Float> saveConsumer, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);
        this.textGetter = (Float) -> {
            return Component.literal(String.format("Value: %f", Float));
        };
        this.orginial = value;
        this.defaultValue = defaultValue;
        this.value = new AtomicFloat(value);
        this.saveCallback = saveConsumer;
        this.maximum = maximum;
        this.minimum = minimum;
        this.sliderWidget = new Slider(0, 0, 152, 20, ((double)this.value.get() - (double)minimum) / (double)Math.abs(maximum - minimum));
        this.resetButton = Button.builder(resetButtonKey, (widget) -> this.setValue((Float)defaultValue.get())).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.sliderWidget.setMessage((Component)this.textGetter.apply(this.value.get()));
        this.widgets = Lists.newArrayList(new AbstractWidget[]{this.sliderWidget, this.resetButton});
    }

    public Function<Float, Component> getTextGetter() {
        return this.textGetter;
    }

    public FloatSliderEntry setTextGetter(Function<Float, Component> textGetter) {
        this.textGetter = textGetter;
        this.sliderWidget.setMessage((Component)textGetter.apply(this.value.get()));
        return this;
    }

    public Float getValue() {
        return this.value.get();
    }

    /** @deprecated */
    @Deprecated
    public void setValue(float value) {
        this.sliderWidget.setValue((double)(Math.clamp(value, this.minimum, this.maximum) - this.minimum) / (double)Math.abs(this.maximum - this.minimum));
        this.value.set(Math.clamp(value, this.minimum, this.maximum));
        this.sliderWidget.updateMessage();
    }

    public boolean isEdited() {
        return super.isEdited() || this.getValue() != this.orginial;
    }

    public Optional<Float> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable((Float)this.defaultValue.get());
    }

    public List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    public List<? extends NarratableEntry> narratables() {
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

    public void render(GuiGraphicsExtractor graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.extractRenderState(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && (Float)this.defaultValue.get() != this.value.get();
        this.resetButton.setY(y);
        this.sliderWidget.active = this.isEditable();
        this.sliderWidget.setY(y);
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            graphics.text(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x);
            this.sliderWidget.setX(x + this.resetButton.getWidth() + 1);
        } else {
            graphics.text(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x + entryWidth - this.resetButton.getWidth());
            this.sliderWidget.setX(x + entryWidth - 150);
        }

        this.sliderWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.extractRenderState(graphics, mouseX, mouseY, delta);
        this.sliderWidget.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    private class Slider extends AbstractSliderButton {
        protected Slider(int int_1, int int_2, int int_3, int int_4, double double_1) {
            super(int_1, int_2, int_3, int_4, Component.empty(), double_1);
        }

        public void updateMessage() {
            this.setMessage((Component)FloatSliderEntry.this.textGetter.apply(FloatSliderEntry.this.value.get()));
        }

        protected void applyValue() {
            FloatSliderEntry.this.value.set((float)((double)FloatSliderEntry.this.minimum + (double)Math.abs(FloatSliderEntry.this.maximum - FloatSliderEntry.this.minimum) * this.value));
        }

        public boolean keyPressed(KeyEvent input) {
            return FloatSliderEntry.this.isEditable() && super.keyPressed(input);
        }

        public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
            return FloatSliderEntry.this.isEditable() && super.mouseDragged(click, offsetX, offsetY);
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