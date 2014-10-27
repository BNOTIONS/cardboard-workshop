package com.androidto.cardboard;

import android.content.Context;
import android.opengl.Matrix;
import android.widget.Toast;

import com.androidto.cardboard.views.VideoPlane;
import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.vector.Vector3;
import rajawali.scene.RajawaliScene;
import rajawali.vr.RajawaliVRRenderer;


/**
 * Created by marcashman on 2014-10-23.
 */
public class VideoTextureRenderer extends RajawaliVRRenderer implements MagnetSensor.OnCardboardTriggerListener {

    private static final float YAW_LIMIT = 0.12f;
    private static final float PITCH_LIMIT = 0.12f;

    private List<VideoPlane> videos = new ArrayList<VideoPlane>();

    private MagnetSensor magnetSensor;

    public VideoTextureRenderer(Context context) {
        super(context);
        setFrameRate(60);

        magnetSensor = new MagnetSensor(getContext());
        magnetSensor.setOnCardboardTriggerListener(this);
        magnetSensor.start();
    }

    private boolean isLookingAt(Object3D object3D) {
        float[] initVec = {0, 0, 0, 1.0f};
        float[] objPositionVec = new float[4];

        float[] mModelView = new float[16];
        Matrix.multiplyMM(mModelView, 0, getHeadView(), 0, object3D.getModelMatrix().getFloatValues(), 0);
        Matrix.multiplyMV(objPositionVec, 0, mModelView, 0, initVec, 0);

        float pitch = (float)Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float)Math.atan2(objPositionVec[0], -objPositionVec[2]);

        return (Math.abs(pitch) < PITCH_LIMIT) && (Math.abs(yaw) < YAW_LIMIT);
    }

    public void initScene() {
        super.initScene();

        VideoPlane videoPlane = new VideoPlane(getContext(), 2, 2, 1, 1, R.raw.rc);
        //Place video plane in front of us on start
        videoPlane.setPosition(-5, 0, 0);
        //Rotate so it faces us
        videoPlane.rotateAround(new Vector3(0, 1, 0), 90);
        videos.add(videoPlane);

        RajawaliScene scene = getCurrentScene();
        scene.addLight(new DirectionalLight(-1, 0, 0));
        for (VideoPlane video : videos) {
            scene.addChild(video);
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceDestroyed() {
        super.onSurfaceDestroyed();

        for (VideoPlane video : videos) {
            video.stop();
        }

        magnetSensor.stop();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        super.onVisibilityChanged(visible);

        if (!visible) {
            for (VideoPlane video : videos) {
                video.pause();
            }
        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);

        for (VideoPlane video : videos) {
            video.onFrameUpdated();
        }
    }

    @Override
    public void onCardboardTrigger() {
        VideoPlane lookingAt = null;
        for (VideoPlane video : videos) {
            if (isLookingAt(video)) {
                lookingAt = video;
            }
        }

        if (lookingAt != null) {
            Toast.makeText(getContext(), "Looking at video", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "_NOT_ Looking at video", Toast.LENGTH_SHORT).show();
        }
    }
}
