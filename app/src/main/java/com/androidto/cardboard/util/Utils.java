package com.androidto.cardboard.util;

import android.graphics.PointF;
import android.opengl.Matrix;

import rajawali.ATransformable3D;
import rajawali.Object3D;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;

public class Utils {

    private static final float LOOK_AT_YAW_LIMIT = 0.12f;
    private static final float LOOK_AT_PITCH_LIMIT = 0.12f;

    /**
     * Returns a point on the circumference of a circle given an angle
     */
    public static PointF getPointOnCircle(double angle, PointF centerPoint, double radius) {
        PointF point = new PointF();
        point.y = (float) (centerPoint.y + radius * Math.sin(Math.toRadians(angle)));
        point.x = (float) (centerPoint.x + radius * Math.cos(Math.toRadians(angle)));
        return point;
    }

    /**
     * performs a rough estimate whether the head is looking at the object or not
     */
    public static boolean isLookingAt(float[] headView, Object3D object3D) {
        float[] initVec = {0, 0, 0, 1.0f};
        float[] objPositionVec = new float[4];

        float[] mModelView = new float[16];
        Matrix.multiplyMM(mModelView, 0, headView, 0, object3D.getModelMatrix().getFloatValues(), 0);
        Matrix.multiplyMV(objPositionVec, 0, mModelView, 0, initVec, 0);

        float pitch = (float)Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float)Math.atan2(objPositionVec[0], -objPositionVec[2]);

        return (Math.abs(pitch) < LOOK_AT_PITCH_LIMIT) && (Math.abs(yaw) < LOOK_AT_YAW_LIMIT);
    }

    /**
     * Make the given object face the camera. Note: this method assumes the camera is pinned to (0, 0, 0)
     */
    public static Quaternion getCameraFacingQuat(ATransformable3D object) {
        return Quaternion.lookAtAndCreate(object.getPosition().inverse(), new Vector3(0, 1, 0), true);
    }
}
