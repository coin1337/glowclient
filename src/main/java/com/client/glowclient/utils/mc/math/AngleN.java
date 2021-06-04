/*
 * Decompiled with CFR 0.150.
 */
package com.client.glowclient.utils.mc.math;

import com.client.glowclient.utils.mc.math.AngleHelper;
import java.util.Arrays;

public class AngleN {
    public static final AngleN ZERO = new AngleN(true, 0.0, 0.0, 0.0);
    private final boolean radians;
    private final double pitch;
    private final double yaw;
    private final double roll;
    private AngleN twin = null;
    private AngleN normal = null;

    public static AngleN radians(double pitch, double yaw, double roll) {
        return new AngleN(true, pitch, yaw, roll);
    }

    public static AngleN radians(float pitch, float yaw, float roll) {
        return AngleN.radians((double)pitch, (double)yaw, (double)roll);
    }

    public static AngleN radians(int pitch, int yaw, int roll) {
        return AngleN.radians((double)pitch, (double)yaw, (double)roll);
    }

    public static AngleN radians(double pitch, double yaw) {
        return AngleN.radians(pitch, yaw, 0.0);
    }

    public static AngleN radians(float pitch, float yaw) {
        return AngleN.radians(pitch, yaw, 0.0f);
    }

    public static AngleN radians(int pitch, int yaw) {
        return AngleN.radians(pitch, yaw, 0);
    }

    public static AngleN degrees(double pitch, double yaw, double roll) {
        return new AngleN(false, pitch, yaw, roll);
    }

    public static AngleN degrees(float pitch, float yaw, float roll) {
        return AngleN.degrees((double)pitch, (double)yaw, (double)roll);
    }

    public static AngleN degrees(int pitch, int yaw, int roll) {
        return AngleN.degrees((double)pitch, (double)yaw, (double)roll);
    }

    public static AngleN degrees(double pitch, double yaw) {
        return AngleN.degrees(pitch, yaw, 0.0);
    }

    public static AngleN degrees(float pitch, float yaw) {
        return AngleN.degrees(pitch, yaw, 0.0f);
    }

    public static AngleN degrees(int pitch, int yaw) {
        return AngleN.degrees(pitch, yaw, 0);
    }

    public static AngleN copy(AngleN ang) {
        return new AngleN(ang);
    }

    protected AngleN(boolean radians, double pitch, double yaw, double roll) {
        this.radians = radians;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    protected AngleN(AngleN ang) {
        this(ang.isRadians(), ang.pitch(), ang.yaw(), ang.roll());
    }

    public double pitch() {
        return this.pitch;
    }

    public double yaw() {
        return this.yaw;
    }

    public double roll() {
        return this.roll;
    }

    public boolean isRadians() {
        return this.radians;
    }

    public boolean isDegrees() {
        return !this.radians;
    }

    public AngleN toRadians() {
        if (this.isRadians()) {
            return this;
        }
        if (this.twin == null) {
            this.twin = AngleN.radians(Math.toRadians(this.pitch()), Math.toRadians(this.yaw()), Math.toRadians(this.roll()));
            this.twin.twin = this;
        }
        return this.twin;
    }

    public AngleN toDegrees() {
        if (this.isDegrees()) {
            return this;
        }
        if (this.twin == null) {
            this.twin = AngleN.degrees(Math.toDegrees(this.pitch()), Math.toDegrees(this.yaw()), Math.toDegrees(this.roll()));
            this.twin.twin = this;
        }
        return this.twin;
    }

    public AngleN add(double p, double y, double r) {
        return this.create(this.pitch() + p, this.yaw() + y, this.roll() + r);
    }

    public AngleN add(double p, double y) {
        return this.add(p, y, 0.0);
    }

    public AngleN add(AngleN ang) {
        if (this.isRadians() != ang.isRadians()) {
            ang = this.isRadians() ? ang.toRadians() : ang.toDegrees();
        }
        return this.add(ang.pitch(), ang.yaw(), ang.roll());
    }

    public AngleN sub(double p, double y, double r) {
        return this.add(-p, -y, -r);
    }

    public AngleN sub(double p, double y) {
        return this.sub(p, y, 0.0);
    }

    public AngleN sub(AngleN ang) {
        if (this.isRadians() != ang.isRadians()) {
            ang = this.isRadians() ? ang.toRadians() : ang.toDegrees();
        }
        return this.sub(ang.pitch(), ang.yaw(), ang.roll());
    }

    public AngleN scale(double factor) {
        return this.create(this.pitch() * factor, this.yaw() * factor, this.roll() * factor);
    }

    public AngleN normalize() {
        if (this.normal == null) {
            double nr;
            double ny;
            double np;
            if (this.isRadians()) {
                np = AngleHelper.normalizeInRadians(this.pitch());
                ny = AngleHelper.normalizeInRadians(this.yaw());
                nr = AngleHelper.normalizeInRadians(this.roll());
            } else {
                np = AngleHelper.normalizeInDegrees(this.pitch());
                ny = AngleHelper.normalizeInDegrees(this.yaw());
                nr = AngleHelper.normalizeInDegrees(this.roll());
            }
            if (AngleHelper.isAngleEqual(this.pitch(), np) && AngleHelper.isAngleEqual(this.yaw(), ny) && AngleHelper.isAngleEqual(this.roll(), nr)) {
                this.normal = this;
            } else {
                AngleN norm;
                norm.normal = norm = this.create(np, ny, nr);
                this.normal = norm;
            }
        }
        return this.normal;
    }

    public double[] forward() {
        double kps = Math.sin(this.toRadians().pitch());
        double kpc = Math.cos(this.toRadians().pitch());
        double kys = Math.sin(this.toRadians().yaw());
        double kyc = Math.cos(this.toRadians().yaw());
        return new double[]{kpc * kyc, kps, kpc * kys};
    }

    public double[] toArray() {
        return new double[]{this.pitch(), this.yaw(), this.roll()};
    }

    protected AngleN create(double p, double y, double r) {
        return new AngleN(this.isRadians(), p, y, r);
    }

    protected AngleN create(double p, double y) {
        return this.create(p, y, 0.0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AngleN) {
            AngleN self = this.normalize();
            AngleN ang = ((AngleN)obj).normalize();
            if (self.isRadians() != ang.isRadians()) {
                ang = self.isRadians() ? ang.toRadians() : ang.toDegrees();
            }
            return AngleHelper.isAngleEqual(self.pitch(), ang.pitch()) && AngleHelper.isAngleEqual(self.yaw(), ang.yaw()) && AngleHelper.isAngleEqual(self.roll(), ang.roll());
        }
        return false;
    }

    public int hashCode() {
        AngleN a = this.normalize().toRadians();
        return Arrays.hashCode(new double[]{AngleHelper.roundAngle(a.pitch()), AngleHelper.roundAngle(a.yaw()), AngleHelper.roundAngle(a.roll())});
    }

    public String toString() {
        return String.format("(%.15f, %.15f, %.15f)[%s]", this.pitch(), this.yaw(), this.roll(), this.isRadians() ? "rad" : "deg");
    }
}

