package com.androidto.cardboard.util;

import android.graphics.PointF;

/**
 * Created by marcashman on 2014-10-27.
 */
public class Utils {

    /**
     * Returns a point on the circumference of a circle given an angle
     */
    public static PointF getPointAroundCenter(float angle, PointF center, float radius) {
        PointF point = new PointF();
        point.y = center.y + radius * (float) Math.sin(Math.toRadians(angle));
        point.x = center.x + radius * (float) Math.cos(Math.toRadians(angle));
        return point;
    }

    /**
     * <p>Use this to normalize angles which have math applied on them</p>
     *
     * <p>e.g. <br>
     *     normalizeAngle(0 - 90) = 270<br>
     *     normalizeAngle(270 + 270) = 180</p>
     */
    public static float normalizeAngle(float angle) {
        angle = angle % 360;
        if (angle <= 0) {
            angle = 360 + angle;
        }
        return angle;
    }
}
