package com.androidto.cardboard;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.Object3D;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.textures.ATexture;
import rajawali.materials.textures.VideoTexture;
import rajawali.math.vector.Vector3;
import rajawali.primitives.Plane;
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
        ALight mLight = new DirectionalLight(0, 0, 1);
        getCurrentScene().addLight(mLight);

//        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(getContext(), R.raw.robotchicken);
        Material material = new Material();
        material.setColor( new float[] { 1f, 1f, 1f, 1f });
        material.setColorInfluence(0);

        videoTexture = new VideoTexture("test", mMediaPlayer);
        videoTexture.setInfluence(1);
        try {
            material.addTexture(videoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        Object3D object = new Plane(2, 2, 1, 1);
        object.setMaterial(material);
        object.setPosition(0, 0, -5f);
        object.rotateAround(new Vector3(0, 1, 0), 180);
        //object.setLookAt(getCurrentCamera().getPosition());
        getCurrentScene().addChild(object);
        //getCurrentCamera().rotateAround(new Vector3(0, 1, 0), 270);
        //object.setLookAt(getCurrentCamera().getPosition());

        //getCurrentCamera().setLookAt(object.getPosition());


        


        mMediaPlayer.setLooping(true);
        mMediaPlayer.seekTo(5000);
        mMediaPlayer.start();
        mMediaPlayer.pause();
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

//        if (!visible) {
//            if (mMediaPlayer != null) {
//                mMediaPlayer.pause();
//            }
//        } else if (mMediaPlayer != null) {
//            mMediaPlayer.start();
//        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        super.onDrawFrame(glUnused);
        videoTexture.update();

        if (getCurrentCamera() == null || getCurrentCamera().getLookAt() == null) {
            return;
        }
        Log.w("look at", getCurrentCamera().getLookAt().toString());
    }

    @Override
    public void onCardboardTrigger() {

    }
}
