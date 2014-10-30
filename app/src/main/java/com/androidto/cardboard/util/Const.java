package com.androidto.cardboard.util;

import android.graphics.Color;

/**
 * Created by marcashman on 2014-10-29.
 */
public class Const {
    private static final int[] COLORS = { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA };

    private static final int NUM_OBSTACLES = 80;
    private static final int OBSTACLE_DIST = 5;
    private static final int OBSTACLE_ROTATION_TIME = 25 * 1000;        //25 seconds
    private static final int OBSTACLE_VERT_SHIFT = 4;
    private static final float OBSTACLE_SIZE = .5f;

    private static final int NUM_OBJECTIVES = 5;
    private static final int OBJECTIVE_DIST = 7;
    private static final int OBJECTIVE_ROTATION_TIME = 40 * 1000;       //40 seconds
    private static final int OBJECTIVE_VERT_SHIFT = 3;
    private static final float OBJECTIVE_SIZE = 3f;
}
