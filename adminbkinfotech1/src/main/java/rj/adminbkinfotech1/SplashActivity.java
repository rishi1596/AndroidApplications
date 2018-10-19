package rj.adminbkinfotech1;

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

import rj.adminbkinfotech1.Constants.Constants;

/**
 * Created by jimeet29 on 23-11-2017.
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView bk_logo, bk_name;
    private TextView tv_admin;
    private Animation blink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        initialize();

        startAnimation();

        startActivityWithDelay();
    }

    private void initialize() {
        ReusableCodeAdmin.createNotificationChannel(SplashActivity.this);
        bk_logo = findViewById(R.id.iv_id_bk_logo);
        bk_name = findViewById(R.id.iv_id_bk_name);
        tv_admin = findViewById(R.id.tv_id_admin);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
    }

    private void startAnimation() {
        bk_logo.startAnimation(blink);
        bk_name.startAnimation(blink);
        tv_admin.startAnimation(blink);
    }

    private void startActivityWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences firstrun = getSharedPreferences(Constants.sharedPreferencesFileNameSettings, Constants.sharedPreferencesAccessMode);
                final boolean state = firstrun.getBoolean("fr", false);
                if (state) {
                    // Registered user
                    Intent userActivity = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(userActivity);
                    finish();
                } else {
                    //Registeration page will be displayed
                    Intent mainActivity = new Intent(getApplicationContext(), AdminLogin.class);
                    startActivity(mainActivity);
                    finish();
                }
            }
        }, 2000);
    }

}
