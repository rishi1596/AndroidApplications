package rj.engineerbkinfotech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import rj.engineerbkinfotech.Constants.Constants;

/**
 * Created by jimeet29 on 23-11-2017.
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView bk_logo, bk_name;
    private Animation blink;
    private TextView tv_engineer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ReusableCodeAdmin.createNotificationChannel(SplashActivity.this);

        initialize();

        startAnimation();

        startActivityWithDelay();
    }

    private void initialize() {
        ReusableCodeAdmin.createNotificationChannel(SplashActivity.this);
        bk_logo = findViewById(R.id.iv_id_bk_logo);
        bk_name = findViewById(R.id.iv_id_bk_name);
        tv_engineer = findViewById(R.id.tv_id_engineer);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
    }

    private void startAnimation() {
        bk_logo.startAnimation(blink);
        bk_name.startAnimation(blink);
        tv_engineer.startAnimation(blink);
    }

    private void startActivityWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences firstrun = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                final boolean state = firstrun.getBoolean("fr", false);
                if (state) {
                    Intent userActivity = new Intent(getApplicationContext(), EngineerActivity.class); // Registered user
                    startActivity(userActivity);
                    finish();
                } else {
                    Intent mainActivity = new Intent(getApplicationContext(), EngineerLogin.class);  //Registeration page will be displayed
                    startActivity(mainActivity);
                    finish();
                }
            }
        }, 3000);
    }
}
