package com.androidto.cardboard.views;

import android.content.Context;
import android.media.MediaPlayer;

import com.androidto.cardboard.FrameUpdater;

import rajawali.materials.Material;
import rajawali.materials.textures.ATexture;
import rajawali.materials.textures.VideoTexture;
import rajawali.primitives.Plane;

/**
 * Created by marcashman on 2014-10-27.
 */
public class VideoPlane extends Plane implements FrameUpdater {

    private VideoTexture videoTexture;
    private MediaPlayer mediaPlayer;

    public VideoPlane(Context context, int width, int height, int segmentsWidth, int segmentsHeight, int videoResourceId) {
        super(width, height, segmentsWidth, segmentsWidth);

        mediaPlayer = MediaPlayer.create(context, videoResourceId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        videoTexture = new VideoTexture("video", mediaPlayer);
        videoTexture.setInfluence(1);

        Material material = new Material();
        material.setColorInfluence(0);
        try {
            material.addTexture(videoTexture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        setMaterial(material);
    }

    public void start() {
        if (mediaPlayer == null) {
            return;
        }

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
