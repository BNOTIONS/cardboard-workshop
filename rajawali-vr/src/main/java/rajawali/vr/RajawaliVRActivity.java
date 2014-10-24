package rajawali.vr;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.vrtoolkit.cardboard.sensors.HeadTracker;

import rajawali.RajawaliActivity;

public class RajawaliVRActivity extends RajawaliActivity {
	private HeadTracker mHeadTracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }
		mSurfaceView.setSystemUiVisibility(flags);
		mHeadTracker = new HeadTracker(this);
	}
	
	protected void setRenderer(RajawaliVRRenderer renderer) {
		renderer.setHeadTracker(mHeadTracker);
		super.setRenderer(renderer);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mHeadTracker.startTracking();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mHeadTracker.stopTracking();
	}
}

