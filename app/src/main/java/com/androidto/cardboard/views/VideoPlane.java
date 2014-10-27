package com.androidto.cardboard.views;

import android.content.Context;
import android.media.MediaPlayer;

import com.androidto.cardboard.FrameUpdater;
import com.androidto.cardboard.R;

import rajawali.materials.Material;
import rajawali.materials.textures.ATexture;
import rajawali.materials.textures.Texture;
import rajawali.materials.textures.VideoTexture;
import rajawali.primitives.Plane;

/**
 * Created by marcashman on 2014-10-27.
 */
public class VideoPlane extends Plane implements FrameUpdater {

    private VideoTexture videoTexture;
    private MediaPlayer mediaPlayer;

    private Material videoMaterial;

    public VideoPlane(Context context, String name, int width, int height, int segmentsWidth, int segmentsHeight, int videoResourceId) {
        super(width, height, segmentsWidth, segmentsHeight);

        mediaPlayer = MediaPlayer.create(context, videoResourceId);
        mediaPlayer.setLooping(true);

        videoTexture = new VideoTexture(name + "_video", mediaPlayer);
        videoTexture.setInfluence(1);

        Material previewMaterial = new Material();
        videoMaterial = new Material();
        videoMaterial.setColorInfluence(0);
        try {
            videoMaterial.addTexture(videoTexture);
            previewMaterial.addTexture(new Texture(name + "_preview", R.drawable.yellow));
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        setMaterial(previewMaterial);
    }

    public void start() {
        if (mediaPlayer == null) {
            return;
        }

        setMaterial(videoMaterial);
        try {
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            //ignore
        }
    }

    public void pause() {
        if (mediaPlayer == null) {
            return;
        }

        try {
            mediaPlayer.pause();
        } catch (IllegalStateException e) {
            //ignore
        }
    }

    public void stop() {
        if (mediaPlayer == null) {
            return;
        }

        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (IllegalStateException e) {
            //ignore
        }
    }

    @Override
    public void onFrameUpdated() {
        //video texture needs to be told to update on each new frame
        videoTexture.update();
    }
}
