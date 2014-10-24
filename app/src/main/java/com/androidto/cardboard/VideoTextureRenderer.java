package com.androidto.cardboard;

import android.content.Context;
import android.media.MediaPlayer;

import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.textures.ATexture;
import rajawali.materials.textures.VideoTexture;
import rajawali.math.vector.Vector3;
import rajawali.primitives.Plane;
import rajawali.scene.RajawaliScene;
import rajawali.vr.RajawaliVRRenderer;


/**
 * Created by marcashman on 2014-10-23.
 */
public class VideoTextureRenderer extends RajawaliVRRenderer implements MagnetSensor.OnCardboardTriggerListener {

    private MediaPlayer mMediaPlayer;
    private VideoTexture videoTexture;

    public VideoTextureRenderer(Context context) {
        super(context);
        setFrameRate(60);

        MagnetSensor magnetSensor = new MagnetSensor(getContext());
        magnetSensor.setOnCardboardTriggerListener(this);
    }

    public void initScene() {
        super.initScene();

        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.robotchicken);
        mMediaPlayer.setLooping(true);
        //start and pause so the first frame is shown
        mMediaPlayer.start();
        mMediaPlayer.pause();

        videoTexture = new VideoTexture("video", mMediaPlayer);
        videoTexture.setInfluence(1);

        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(videoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        Object3D videoPane = new Plane(2, 2, 1, 1);
        videoPane.setMaterial(material);
        videoPane.setPosition(-5, 0, 0);
        videoPane.rotateAround(new Vector3(0, 1, 0), 90);

        RajawaliScene scene = getCurrentScene();
        scene.addLight(new DirectionalLight(0, 0, 1));
        scene.addChild(videoPane);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceDestroyed() {
        super.onSurfaceDestroyed();

        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        super.onVisibilityChanged(visible);

        if (!visible) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
            }
        } else if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
        videoTexture.update();
    }

    @Override
    public void onCardboardTrigger() {

    }
}
