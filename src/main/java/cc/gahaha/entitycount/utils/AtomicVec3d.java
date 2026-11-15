package cc.gahaha.entitycount.utils;

import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.world.phys.Vec3;

public class AtomicVec3d {
    private final AtomicReference<Vec3> ref;

    public AtomicVec3d() {
        this((double)0.0F, (double)0.0F, (double)0.0F);
    }

    public AtomicVec3d(double x, double y, double z) {
        this.ref = new AtomicReference<>(new Vec3(x, y, z));
    }

    public AtomicVec3d(Vec3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public final void set(double x, double y, double z) {
        this.ref.set(new Vec3(x, y, z));
    }

    public final void set(Vec3 vec) {
        this.ref.set(new Vec3(vec.x, vec.y, vec.z));
    }

    public final Vec3 get() {
        return (Vec3)this.ref.get();
    }

    public final double getX() {
        return ((Vec3)this.ref.get()).x;
    }

    public final double getY() {
        return ((Vec3)this.ref.get()).y;
    }

    public final double getZ() {
        return ((Vec3)this.ref.get()).z;
    }

    public final void setX(double x) {
        this.ref.updateAndGet((vec) -> new Vec3(x, vec.y, vec.z));
    }

    public final void setY(double y) {
        this.ref.updateAndGet((vec) -> new Vec3(vec.x, y, vec.z));
    }

    public final void setZ(double z) {
        this.ref.updateAndGet((vec) -> new Vec3(vec.x, vec.y, z));
    }

    public final Vec3 getAndSet(double x, double y, double z) {
        return (Vec3)this.ref.getAndSet(new Vec3(x, y, z));
    }

    public final Vec3 getAndSet(Vec3 vec) {
        return (Vec3)this.ref.getAndSet(new Vec3(vec.x, vec.y, vec.z));
    }

    public final boolean compareAndSet(Vec3 expect, Vec3 update) {
        Vec3 updated = new Vec3(update.x, update.y, update.z);
        Vec3 current = (Vec3)this.ref.get();
        return this.ref.compareAndSet(current, updated);
    }

    public final boolean weakCompareAndSet(Vec3 expect, Vec3 update) {
        return this.compareAndSet(expect, update);
    }

    private static boolean sameBits(Vec3 a, Vec3 b) {
        return Double.doubleToLongBits(a.x) == Double.doubleToLongBits(b.x) && Double.doubleToLongBits(a.y) == Double.doubleToLongBits(b.y) && Double.doubleToLongBits(a.z) == Double.doubleToLongBits(b.z);
    }

    public String toString() {
        Vec3 vec = this.get();
        return String.format("AtomicVec3d(%.2f, %.2f, %.2f)", vec.x, vec.y, vec.z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof AtomicVec3d)) {
            return false;
        } else {
            AtomicVec3d other = (AtomicVec3d)obj;
            Vec3 thisVec = this.get();
            Vec3 otherVec = other.get();
            return thisVec.equals(otherVec);
        }
    }

    public int hashCode() {
        return this.get().hashCode();
    }
}
