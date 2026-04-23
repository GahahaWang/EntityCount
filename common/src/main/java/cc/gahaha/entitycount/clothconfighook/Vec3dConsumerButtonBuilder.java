package cc.gahaha.entitycount.clothconfighook;

import cc.gahaha.entitycount.utils.AtomicVec3d;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class Vec3dConsumerButtonBuilder extends AbstractFieldBuilder<Vec3, Vec3dConsumerButtonEntry, Vec3dConsumerButtonBuilder> {
    private Function<Vec3, Component> nameProvider = null;
    private Consumer<AtomicVec3d> buttonFunction = atomicVec3 -> {};
    public Vec3dConsumerButtonBuilder(Component resetButtonKey, Component fieldNameKey, Vec3 value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(value);
        this.value = value;
    }

    public Vec3dConsumerButtonBuilder setErrorSupplier(Function<Vec3, Optional<Component>> errorSupplier) {
        return (Vec3dConsumerButtonBuilder)super.setErrorSupplier(errorSupplier);
    }

    public Vec3dConsumerButtonBuilder requireRestart() {
        return (Vec3dConsumerButtonBuilder)super.requireRestart();
    }

    public Vec3dConsumerButtonBuilder setButtonFunction(Consumer<AtomicVec3d> buttonFunction) {
        this.buttonFunction = buttonFunction;
        return this;
    }

    public Vec3dConsumerButtonBuilder setSaveConsumer(Consumer<Vec3> saveConsumer) {
        return (Vec3dConsumerButtonBuilder)super.setSaveConsumer(saveConsumer);
    }

    public Vec3dConsumerButtonBuilder setDefaultValue(Supplier<Vec3> defaultValue) {
        return (Vec3dConsumerButtonBuilder)super.setDefaultValue(defaultValue);
    }

    public Vec3dConsumerButtonBuilder setDefaultValue(Vec3 defaultValue) {
        return (Vec3dConsumerButtonBuilder)super.setDefaultValue(defaultValue);
    }

    public Vec3dConsumerButtonBuilder setTooltipSupplier(Function<Vec3, Optional<Component[]>> tooltipSupplier) {
        return (Vec3dConsumerButtonBuilder)super.setTooltipSupplier(tooltipSupplier);
    }

    public Vec3dConsumerButtonBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (Vec3dConsumerButtonBuilder)super.setTooltipSupplier(tooltipSupplier);
    }

    public Vec3dConsumerButtonBuilder setTooltip(Component... tooltip) {
        return (Vec3dConsumerButtonBuilder)super.setTooltip(tooltip);
    }

    public Vec3dConsumerButtonBuilder setNameProvider(Function<Vec3, Component> enumNameProvider) {
        this.nameProvider = enumNameProvider;
        return this;
    }

    public @NotNull Vec3dConsumerButtonEntry build() {
        Vec3dConsumerButtonEntry entry = new Vec3dConsumerButtonEntry(this.getFieldNameKey(), this.value,
                this.getResetButtonKey(), this.defaultValue, this.buttonFunction, this.getSaveConsumer(), this.nameProvider, (Supplier)null, this.isRequireRestart());
        //entry.setTooltipSupplier(() -> (Optional)this.getTooltipSupplier().apply(entry.getValue()));
        //if (this.errorSupplier != null) {
        //    entry.setErrorSupplier(() -> (Optional)this.errorSupplier.apply(entry.getValue()));
        //}

        return (Vec3dConsumerButtonEntry)this.finishBuilding(entry);
    }
}
