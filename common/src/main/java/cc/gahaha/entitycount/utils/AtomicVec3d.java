package cc.gahaha.entitycount.utils;

import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;

public class AtomicVec3d {

    private final AtomicLong xBits;
    private final AtomicLong yBits;
    private final AtomicLong zBits;

    public AtomicVec3d() {
        this(0.0, 0.0, 0.0);
    }

    public AtomicVec3d(double x, double y, double z) {
        this.xBits = new AtomicLong(doubleToLongBits(x));
        this.yBits = new AtomicLong(doubleToLongBits(y));
        this.zBits = new AtomicLong(doubleToLongBits(z));
    }

    public AtomicVec3d(Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public final void set(double x, double y, double z) {
        xBits.set(doubleToLongBits(x));
        yBits.set(doubleToLongBits(y));
        zBits.set(doubleToLongBits(z));
    }

    public final void set(Vec3d vec) {
        set(vec.x, vec.y, vec.z);
    }

    public final Vec3d get() {
        return new Vec3d(
                longBitsToDouble(xBits.get()),
                longBitsToDouble(yBits.get()),
                longBitsToDouble(zBits.get())
        );
    }

    public final double getX() {
        return longBitsToDouble(xBits.get());
    }

    public final double getY() {
        return longBitsToDouble(yBits.get());
    }

    public final double getZ() {
        return longBitsToDouble(zBits.get());
    }

    public final void setX(double x) {
        xBits.set(doubleToLongBits(x));
    }

    public final void setY(double y) {
        yBits.set(doubleToLongBits(y));
    }

    public final void setZ(double z) {
        zBits.set(doubleToLongBits(z));
    }

    public final Vec3d getAndSet(double x, double y, double z) {
        return new Vec3d(
                longBitsToDouble(xBits.getAndSet(doubleToLongBits(x))),
                longBitsToDouble(yBits.getAndSet(doubleToLongBits(y))),
                longBitsToDouble(zBits.getAndSet(doubleToLongBits(z)))
        );
    }

    public final Vec3d getAndSet(Vec3d vec) {
        return getAndSet(vec.x, vec.y, vec.z);
    }

    public final boolean compareAndSet(Vec3d expect, Vec3d update) {
        return xBits.compareAndSet(doubleToLongBits(expect.x), doubleToLongBits(update.x))
                && yBits.compareAndSet(doubleToLongBits(expect.y), doubleToLongBits(update.y))
                && zBits.compareAndSet(doubleToLongBits(expect.z), doubleToLongBits(update.z));
    }

    public final boolean weakCompareAndSet(Vec3d expect, Vec3d update) {
        return xBits.weakCompareAndSet(doubleToLongBits(expect.x), doubleToLongBits(update.x))
                && yBits.weakCompareAndSet(doubleToLongBits(expect.y), doubleToLongBits(update.y))
                && zBits.weakCompareAndSet(doubleToLongBits(expect.z), doubleToLongBits(update.z));
    }

    @Override
    public String toString() {
        Vec3d vec = get();
        return String.format("AtomicVec3d(%.2f, %.2f, %.2f)", vec.x, vec.y, vec.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AtomicVec3d)) return false;
        AtomicVec3d other = (AtomicVec3d) obj;
        Vec3d thisVec = this.get();
        Vec3d otherVec = other.get();
        return thisVec.equals(otherVec);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }
}
