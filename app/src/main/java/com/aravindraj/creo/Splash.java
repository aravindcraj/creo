package com.aravindraj.creo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aravindraj.creo.activities.DispatchActivity;

/**
 * Created by AravindRaj on 24-04-2016.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(Splash.this, DispatchActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);

    }
}
