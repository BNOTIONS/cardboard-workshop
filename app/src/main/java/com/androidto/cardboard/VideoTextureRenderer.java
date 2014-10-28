package com.androidto.cardboard;

import android.content.Context;
import android.graphics.PointF;

import com.androidto.cardboard.util.Utils;
import com.androidto.cardboard.views.VideoPlane;
import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.lights.DirectionalLight;
import rajawali.scene.RajawaliScene;
import rajawali.vr.RajawaliVRRenderer;


/**
 * Created by marcashman on 2014-10-23.
 */
public class VideoTextureRenderer extends RajawaliVRRenderer implements MagnetSensor.OnCardboardTriggerListener {

    private static final int[] videosToShow = new int[] {
            R.raw.rc, R.raw.review };

    private List<VideoPlane> videos = new ArrayList<VideoPlane>();
    private MagnetSensor magnetSensor;

    public VideoTextureRenderer(Context context) {
        super(context);
        setFrameRate(60);

        magnetSensor = new MagnetSensor(getContext());
        magnetSensor.setOnCardboardTriggerListener(this);
        magnetSensor.start();
    }

    public void initScene() {
        super.initScene();

        int size = videosToShow.length;
        for (int i = 0; i < size; i++) {
            //Place video plane in front of us on start
            double angle = Utils.normalizeAngle(180 - (i * 40));     //do not question this value
            PointF point = Utils.getPointAroundCenter(angle, new PointF(0, 0), 5);

            VideoPlane videoPlane = new VideoPlane(getContext(), "video" + i, 2, 2, 1, 1, videosToShow[i]);
            videoPlane.setPosition(point.x, 0, point.y);
            //Rotate so it faces us
            Utils.faceCamera(videoPlane);

            videos.add(videoPlane);
        }

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
            video.pause();
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
            if (Utils.isLookingAt(mHeadViewMatrix, video)) {
                lookingAt = video;
                break;
            }
        }

        if (lookingAt != null) {
            lookingAt.start();
        }
    }
}
