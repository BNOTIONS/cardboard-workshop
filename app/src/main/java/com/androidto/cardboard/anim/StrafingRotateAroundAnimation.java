package com.androidto.cardboard.anim;

import rajawali.animation.RotateAroundAnimation3D;
import rajawali.math.vector.Vector3;

/**
 * Only supports Y axis
 */
public class StrafingRotateAroundAnimation extends RotateAroundAnimation3D {

    private double lastElapsed;
    private double lastAngle;

    public StrafingRotateAroundAnimation(Vector3 center, Vector3.Axis axis, double distance) {
        super(center, axis, distance);
    }

    @Override
    protected void applyTransformation() {
        super.applyTransformation();

        double thisAngle = (mElapsedTime / mDuration) * 360;
        double angle = (lastElapsed > mElapsedTime) ? thisAngle : thisAngle - lastAngle;

        mTransformable3D.rotateAround(new Vector3(0, 1, 0), angle);

        lastAngle = thisAngle;
        lastElapsed = mElapsedTime;
    }

}
