package com.example.mustafa.bloodbank.ui.activity.splashcycle;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.activity.UserCycleActivity;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.REMEBER;

public class SplashActivity extends AppCompatActivity {


    private long POST_DILAY_TIME=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean rember = SharedPreferencesManger.LoadBoolean(SplashActivity.this, REMEBER);

                if (rember) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(SplashActivity.this, SliderActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },POST_DILAY_TIME);

    }
}
