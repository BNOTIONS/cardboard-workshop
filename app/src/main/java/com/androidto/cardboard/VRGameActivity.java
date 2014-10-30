package com.androidto.cardboard;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;

import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.vr.RajawaliVRActivity;

public class VRGameActivity extends RajawaliVRActivity {

    private MagnetSensor magnetSensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final VRGameRenderer renderer = new VRGameRenderer(this);
        renderer.getCurrentCamera().setOrientation(new Quaternion(new Vector3(0, 1, 0), 90));
        renderer.setSurfaceView(mSurfaceView);
        setRenderer(renderer);

        magnetSensor = new MagnetSensor(this);
//        magnetSensor.setOnCardboardTriggerListener(renderer);
//
//        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                renderer.onCardboardTrigger();
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();

        magnetSensor.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        magnetSensor.stop();
    }
}
