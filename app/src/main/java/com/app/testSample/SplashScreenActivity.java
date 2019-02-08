package com.app.testSample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.testSample.homescreen.HomeScreen;
import com.app.testSample.utility.network.Response;
import com.app.testSample.utility.ui.BaseActivity;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000L);
    }

    @Override
    protected void updateUi(Response response) {

    }
}
