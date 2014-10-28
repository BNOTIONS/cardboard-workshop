package com.androidto.cardboard;

import android.content.Context;
import android.graphics.Color;

import com.androidto.cardboard.anim.ShiftingRotateAroundAnimation3D;
import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rajawali.animation.Animation;
import rajawali.animation.RotateAroundAnimation3D;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.math.vector.Vector3;
import rajawali.primitives.Cube;
import rajawali.scene.RajawaliScene;
import rajawali.vr.RajawaliVRRenderer;

/**
 * Created by marcashman on 2014-10-28.
 */
public class VRGameRenderer extends RajawaliVRRenderer implements MagnetSensor.OnCardboardTriggerListener {

    private static final int NUM_CUBES = 16;
    private static final int CUBE_DIST = 5;
    private static final int MAX_CUBE_SHIFT = 3;
    private static final int ROTATION_TIME = 25 * 1000; //10 sec

    private MagnetSensor magnetSensor;
    private List<Cube> cubeList = new ArrayList<Cube>();

    public VRGameRenderer(Context context) {
        super(context);
        setFrameRate(60);

        magnetSensor = new MagnetSensor(getContext());
        magnetSensor.setOnCardboardTriggerListener(this);
        magnetSensor.start();
    }

    @Override
    public void initScene() {
        super.initScene();

        RajawaliScene scene = getCurrentScene();
        ALight light = new DirectionalLight(-1, -1, 0);
        light.setPosition(5, 5, 0);
        scene.addLight(light);

        light = new DirectionalLight(1, 1, 0);
        light.setPosition(-5, -5, 0);
        scene.addLight(light);

        //generate cubes
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            Cube cube = new Cube(.5f);
            cubeList.add(cube);

            RotateAroundAnimation3D animation = new ShiftingRotateAroundAnimation3D(new Vector3(0, 0, 0), Vector3.Axis.X, CUBE_DIST, MAX_CUBE_SHIFT, random.nextDouble());
            animation.setTransformable3D(cube);
            animation.setRepeatMode(Animation.RepeatMode.INFINITE);
            animation.setDurationMilliseconds(ROTATION_TIME);
            animation.setStartTime(i * (ROTATION_TIME / NUM_CUBES));
            scene.registerAnimation(animation);
            animation.play();

            Material cubeMaterial = new Material();
            cubeMaterial.setColor(Color.RED);
            cubeMaterial.enableLighting(true);
            cubeMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());

            cube.setMaterial(cubeMaterial);
            scene.addChild(cube);
        }
    }

    @Override
    public void onSurfaceDestroyed() {
        super.onSurfaceDestroyed();
        magnetSensor.stop();
    }

    @Override
    public void onCardboardTrigger() {

    }
}
