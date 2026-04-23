package cc.gahaha.entitycount.clothconfighook;

import cc.gahaha.entitycount.utils.AtomicVec3d;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Vec3dConsumerButtonEntry extends TooltipListEntry<Vec3> {
    private final AtomicVec3d value;
    private final Vec3 original;
    private final Button buttonWidget;
    private final Button resetButton;
    private final Supplier<Vec3> defaultValue;
    private final List<AbstractWidget> widgets;
    private final Function<Vec3, Component> nameProvider;

    /** @deprecated */
    @Deprecated
    @Internal
    public Vec3dConsumerButtonEntry(Component fieldName, Vec3 value, Component resetButtonKey, Supplier<Vec3> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3> saveConsumer) {
        this(fieldName, value, resetButtonKey, defaultValue, buttonFunction, saveConsumer, (Function)null);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public Vec3dConsumerButtonEntry(Component fieldName, Vec3 value, Component resetButtonKey, Supplier<Vec3> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3> saveConsumer, Function<Vec3, Component> nameProvider) {
        this(fieldName, value, resetButtonKey, defaultValue, buttonFunction, saveConsumer, nameProvider, (Supplier)null);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public Vec3dConsumerButtonEntry(Component fieldName, Vec3 value, Component resetButtonKey, Supplier<Vec3> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3> saveConsumer, Function<Vec3, Component> nameProvider, Supplier<Optional<Component[]>> tooltipSupplier) {
        this(fieldName, value, resetButtonKey, defaultValue, buttonFunction, saveConsumer, nameProvider, tooltipSupplier, false);
    }

    /** @deprecated */
    @Deprecated
    @Internal
    public Vec3dConsumerButtonEntry(Component fieldName, Vec3 value, Component resetButtonKey, Supplier<Vec3> defaultValue, Consumer<AtomicVec3d> buttonFunction, Consumer<Vec3> saveConsumer, Function<Vec3, Component> nameProvider, Supplier<Optional<Component[]>> tooltipSupplier, boolean requiresRestart) {
        super(fieldName, tooltipSupplier, requiresRestart);

        this.defaultValue = defaultValue;
        this.value = new AtomicVec3d(value);
        this.original = value;
        this.buttonWidget = Button.builder(Component.empty(), (widget) -> buttonFunction.accept(this.value)).bounds(0, 0, 150, 20).build();
        this.resetButton = Button.builder(resetButtonKey, (widget) -> this.value.set((Vec3)this.getDefaultValue().get())).bounds(0, 0, Minecraft.getInstance().font.width(resetButtonKey) + 6, 20).build();
        this.saveCallback = saveConsumer;
        this.widgets = Lists.newArrayList(new AbstractWidget[]{this.buttonWidget, this.resetButton});
        this.nameProvider = nameProvider == null ? (t) -> Component.translatable(t instanceof Translatable ? ((Translatable)t).getKey() : t.toString()) : nameProvider;
    }

    public boolean isEdited() {
        return super.isEdited() || !this.value.get().equals(this.original);
    }

    @Override
    public Vec3 getValue() {
        return this.value.get();
    }

    @Override
    public Optional<Vec3> getDefaultValue() {
        return this.defaultValue == null ? Optional.empty() : Optional.ofNullable((Vec3)this.defaultValue.get());
    }

    public void extractRenderState(GuiGraphicsExtractor graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.extractRenderState(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = Minecraft.getInstance().getWindow();
        this.resetButton.active = this.isEditable() && this.getDefaultValue().isPresent() && !Objects.equals(this.defaultValue.get(), this.value.get());
        this.resetButton.setY(y);
        this.buttonWidget.active = this.isEditable();
        this.buttonWidget.setY(y);
        this.buttonWidget.setMessage((Component)this.nameProvider.apply(this.value.get()));
        Component displayedFieldName = this.getDisplayedFieldName();
        if (Minecraft.getInstance().font.isBidirectional()) {
            graphics.text(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(),
                    window.getGuiScaledWidth() - x - Minecraft.getInstance().font.width(displayedFieldName), y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x);
            this.buttonWidget.setX(x + this.resetButton.getWidth() + 2);
        } else {
            graphics.text(Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(), x, y + 6, this.getPreferredTextColor());
            this.resetButton.setX(x + entryWidth - this.resetButton.getWidth());
            this.buttonWidget.setX(x + entryWidth - 150);
        }

        this.buttonWidget.setWidth(150 - this.resetButton.getWidth() - 2);
        this.resetButton.extractRenderState(graphics, mouseX, mouseY, delta);
        this.buttonWidget.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    public List<? extends NarratableEntry> narratables() {
        return this.widgets;
    }

    public interface Translatable {
        @NotNull String getKey();
    }
}
