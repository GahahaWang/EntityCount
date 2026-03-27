package cc.gahaha.entitycount.utils;

import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Double.doubleToLongBits;

public class AtomicVec3d {
    private final AtomicReference<Vec3d> ref;

    public AtomicVec3d() {
        this(0.0, 0.0, 0.0);
    }

    public AtomicVec3d(double x, double y, double z) {
        this.ref = new AtomicReference<>(new Vec3d(x, y, z));
    }

    public AtomicVec3d(Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public final void set(double x, double y, double z) {
        ref.set(new Vec3d(x, y, z));
    }

    public final void set(Vec3d vec) {
        ref.set(new Vec3d(vec.x, vec.y, vec.z));
    }

    public final Vec3d get() {
        return ref.get();
    }

    public final double getX() {
        return ref.get().x;
    }

    public final double getY() {
        return ref.get().y;
    }

    public final double getZ() {
        return ref.get().z;
    }

    public final void setX(double x) {
        ref.updateAndGet(vec -> new Vec3d(x, vec.y, vec.z));
    }

    public final void setY(double y) {
        ref.updateAndGet(vec -> new Vec3d(vec.x, y, vec.z));
    }

    public final void setZ(double z) {
        ref.updateAndGet(vec -> new Vec3d(vec.x, vec.y, z));
    }

    public final Vec3d getAndSet(double x, double y, double z) {
        return ref.getAndSet(new Vec3d(x, y, z));
    }

    public final Vec3d getAndSet(Vec3d vec) {
        return ref.getAndSet(new Vec3d(vec.x, vec.y, vec.z));
    }

    public final boolean compareAndSet(Vec3d expect, Vec3d update) {
        Vec3d updated = new Vec3d(update.x, update.y, update.z);
        Vec3d current = ref.get();
        return ref.compareAndSet(current, updated);
    }

    public final boolean weakCompareAndSet(Vec3d expect, Vec3d update) {
        return compareAndSet(expect, update);
    }

    private static boolean sameBits(Vec3d a, Vec3d b) {
        return doubleToLongBits(a.x) == doubleToLongBits(b.x)
            && doubleToLongBits(a.y) == doubleToLongBits(b.y)
            && doubleToLongBits(a.z) == doubleToLongBits(b.z);
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
