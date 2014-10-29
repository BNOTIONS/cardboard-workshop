package com.androidto.cardboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_rajawali).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);
        findViewById(R.id.btn_game).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rajawali:
                startActivity(new Intent(this, RajawaliVRExampleActivity.class));
                break;
            case R.id.btn_video:
                startActivity(new Intent(this, VideoTextureActivity.class));
                break;
            case R.id.btn_game:
                startActivity(new Intent(this, VRGameActivity.class));
                break;
        }
    }
}
