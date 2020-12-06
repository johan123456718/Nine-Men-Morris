package com.example.nine_men_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

/**
 * Model for the splashScreen.
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        EasySplashScreen splashScreenConfig = new EasySplashScreen(SplashScreen.this)
                        .withFullScreen()
                        .withTargetActivity(MainActivity.class)
                        .withSplashTimeOut(1500)
                        .withBackgroundColor(Color.parseColor("#1a1b29"))
                        .withHeaderText("")
                        .withFooterText("")
                        .withBeforeLogoText("Nine Men's Morris")
                        .withAfterLogoText("Loading...")
                        .withLogo(R.mipmap.nine_morris_image_round)
                        .withTargetActivity(GameActivity.class);

        splashScreenConfig.getHeaderTextView().setTextColor(Color.WHITE);
        splashScreenConfig.getFooterTextView().setTextColor(Color.WHITE);
        splashScreenConfig.getBeforeLogoTextView().setTextColor(Color.WHITE);
        splashScreenConfig.getAfterLogoTextView().setTextColor(Color.WHITE);

        View splashScreen = splashScreenConfig.create();
        setContentView(splashScreen);
    }
}