package com.androidto.cardboard.anim;

import rajawali.animation.RotateAroundAnimation3D;
import rajawali.math.MathUtil;
import rajawali.math.vector.Vector3;

public class ShiftingRotateAroundAnimation3D extends RotateAroundAnimation3D {

    private double maxShift;
    private double seed;
    private boolean useSin;

    public ShiftingRotateAroundAnimation3D(Vector3 center, Vector3.Axis axis,
                                           double distance, double maxShift,
                                           double seed, boolean useSin) {
        super(center, axis, distance);
        this.maxShift = maxShift;
        this.useSin = useSin;
        this.seed = seed;
    }

    @Override
    protected void applyTransformation() {
        super.applyTransformation();

        final double radians = 360f * ((seed + mInterpolatedTime) % 1) * MathUtil.PRE_PI_DIV_180;
        double value;
        if (useSin) {
            value = Math.sin(radians) * maxShift;
        } else {
            value = Math.cos(radians) * maxShift;
        }

        switch (mAxis) {
            case Z:
                mTransformable3D.setZ(value);
                break;
            case X:
                mTransformable3D.setY(value);
                break;
            case Y:
                mTransformable3D.setX(value);
                break;
        }
    }
}
