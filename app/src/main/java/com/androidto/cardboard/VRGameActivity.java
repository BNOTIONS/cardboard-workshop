package com.androidto.cardboard;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.vr.RajawaliVRActivity;

/**
 * Created by marcashman on 2014-10-28.
 */
public class VRGameActivity extends RajawaliVRActivity {

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

        VRGameRenderer mRenderer = new VRGameRenderer(this);
        mRenderer.getCurrentCamera().setOrientation(new Quaternion(new Vector3(0, 1, 0), 90));
        mRenderer.setSurfaceView(mSurfaceView);
        setRenderer(mRenderer);

    }

}
