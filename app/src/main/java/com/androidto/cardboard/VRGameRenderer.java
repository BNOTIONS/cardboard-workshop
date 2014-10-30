package com.androidto.cardboard;

import android.content.Context;

import java.util.Random;

import rajawali.vr.RajawaliVRRenderer;

public class VRGameRenderer extends RajawaliVRRenderer {

    private static final Random RANDOM = new Random();

    public VRGameRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    @Override
    public void initScene() {



        //call super at the end
        super.initScene();
    }

}
