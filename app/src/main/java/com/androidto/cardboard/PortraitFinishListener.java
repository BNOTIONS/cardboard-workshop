package com.androidto.cardboard;

import android.app.Activity;
import android.view.OrientationEventListener;

/**
 * Created by marcashman on 2014-10-25.
 */
public class PortraitFinishListener extends OrientationEventListener {

    private Activity activity;
    private boolean startedInPortrait = true;

    public PortraitFinishListener(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onOrientationChanged(int i) {
        if (i == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        }

        if ((350 <= i && i <= 360) ||
                (0 <= i && i <= 10) ||
                (170 <= i && i <= 190)){
            if (!startedInPortrait) {
                activity.finish();
            }
        } else {
            startedInPortrait = false;
        }
    }
}
