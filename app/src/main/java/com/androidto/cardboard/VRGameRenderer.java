package com.androidto.cardboard;

import android.content.Context;
import android.graphics.Color;

import com.androidto.cardboard.anim.ShiftingRotateAroundAnimation3D;
import com.androidto.cardboard.anim.StrafingRotateAroundAnimation;
import com.androidto.cardboard.util.Utils;
import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rajawali.Object3D;
import rajawali.animation.Animation;
import rajawali.animation.RotateAroundAnimation3D;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.lights.PointLight;
import rajawali.materials.Material;
import rajawali.materials.methods.DiffuseMethod;
import rajawali.materials.textures.NormalMapTexture;
import rajawali.materials.textures.Texture;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderAWD;
import rajawali.primitives.Cube;
import rajawali.scene.RajawaliScene;
import rajawali.vr.RajawaliVRRenderer;

public class VRGameRenderer extends RajawaliVRRenderer implements MagnetSensor.OnCardboardTriggerListener {

    private static final Random RANDOM = new Random();

    private static final int[] COLORS = { Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA };

    private static final int NUM_OBSTACLES = 120;
    private static final int OBSTACLE_DIST = 5;
    private static final int OBSTACLE_ROTATION_TIME = 25 * 1000;        //25 seconds
    private static final int OBSTACLE_VERT_SHIFT = 4;

    private static final int NUM_OBJECTIVES = 5;
    private static final int OBJECTIVE_DIST = 7;
    private static final int OBJECTIVE_ROTATION_TIME = 40 * 1000;       //40 seconds
    private static final int OBJECTIVE_VERT_SHIFT = 3;

    private List<Object3D> obstacles = new ArrayList<Object3D>();
    private List<Object3D> objectives = new ArrayList<Object3D>();

    public VRGameRenderer(Context context) {
        super(context);
        setFrameRate(60);
    }

    @Override
    public void initScene() {
        super.initScene();

        getCurrentScene().setBackgroundColor(Color.BLACK);
        setupLights();
        
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            addShiftingObstacle();
        }

        for (int objectiveNum = 0; objectiveNum < NUM_OBJECTIVES; objectiveNum++) {
            addObjective(objectiveNum);
        }
    }

    private void setupLights() {
        RajawaliScene scene = getCurrentScene();

        ALight light = new DirectionalLight(0, -1, 0);
        light.setPosition(0, 10, 0);
        scene.addLight(light);

        light = new DirectionalLight(0, 1, 0);
        light.setPosition(0, -10, 0);
        scene.addLight(light);

        light = new PointLight();
        light.setPosition(0, 0, 0);
        light.setPower(.2f);
        scene.addLight(light);
    }

    private void addShiftingObstacle() {
        RajawaliScene scene = getCurrentScene();
        Object3D obstacle = createObstacle();
        obstacles.add(obstacle);

        RotateAroundAnimation3D animation = new ShiftingRotateAroundAnimation3D(
                new Vector3(0, 0, 0),       //rotate around origin
                Vector3.Axis.X,             //bug?
                OBSTACLE_DIST,              //distance at which obstacles reside
                OBSTACLE_VERT_SHIFT,    //maximum vertical distance obstacles will shift
                RANDOM.nextDouble());
        animation.setRepeatMode(Animation.RepeatMode.INFINITE);
        animation.setDurationMilliseconds(OBSTACLE_ROTATION_TIME);
        animation.setStartTime((int) (RANDOM.nextFloat() * OBSTACLE_ROTATION_TIME));
        animation.setTransformable3D(obstacle);

        scene.registerAnimation(animation);
        animation.play();

        scene.addChild(obstacle);
    }

    private void addObjective(int objectiveNum) {
        RajawaliScene scene = getCurrentScene();
        Object3D objective = createObjective();
        objectives.add(objective);

        RotateAroundAnimation3D animation = new StrafingRotateAroundAnimation(
                new Vector3(0, 0, 0),
                Vector3.Axis.X,
                OBJECTIVE_DIST);
        animation.setRepeatMode(Animation.RepeatMode.INFINITE);
        animation.setDurationMilliseconds(OBJECTIVE_ROTATION_TIME);
        animation.setStartTime(objectiveNum * (OBJECTIVE_ROTATION_TIME / NUM_OBJECTIVES));
        animation.setTransformable3D(objective);

        scene.registerAnimation(animation);
        animation.play();
        scene.addChild(objective);
    }

    private Object3D createObjective() {

        Material capitalMaterial = new Material();
        capitalMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        capitalMaterial.setColorInfluence(0);
        capitalMaterial.enableLighting(true);

        Object3D capital = null;
        try {
            capitalMaterial.addTexture(new Texture("capitalTex", R.drawable.hullw));
            capitalMaterial.addTexture(new NormalMapTexture("capitalNormTex", R.drawable.hulln));

            LoaderAWD loader = new LoaderAWD(mContext.getResources(), mTextureManager, R.raw.capital);
            loader.parse();

            capital = loader.getParsedObject();
            capital.setMaterial(capitalMaterial);
            capital.setScale(3);

            capital.setY((RANDOM.nextDouble() * OBJECTIVE_VERT_SHIFT) - (OBJECTIVE_VERT_SHIFT / 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return capital;

    }

    private Object3D createObstacle() {
        Object3D cube = new Cube(.5f);

        Material cubeMaterial = new Material();
        cubeMaterial.setColor(COLORS[RANDOM.nextInt(COLORS.length)]);
        cubeMaterial.enableLighting(true);
        cubeMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        cube.setMaterial(cubeMaterial);
        return cube;
    }

    @Override
    public void onCardboardTrigger() {
        boolean hitObstacle = false;
        for (Object3D obstacle : obstacles) {
            if (Utils.isLookingAt(mHeadViewMatrix, obstacle)) {
                hitObstacle = true;
                break;
            }
        }

        if (!hitObstacle) {
            for (Object3D objective : objectives) {
                if (Utils.isLookingAt(mHeadViewMatrix, objective)) {
                    moveObjective(objective);
                    break;
                }
            }
        }
    }

    private void moveObjective(Object3D objective) {
        double oldY = objective.getY();
        double y = (RANDOM.nextDouble() * OBJECTIVE_VERT_SHIFT) - (OBJECTIVE_VERT_SHIFT / 2);
        while (-1 <= (oldY - y) && (oldY - y) <= 1) {
            y = (RANDOM.nextDouble() * OBJECTIVE_VERT_SHIFT) - (OBJECTIVE_VERT_SHIFT / 2);
        }
        objective.setY(y);
    }
}
