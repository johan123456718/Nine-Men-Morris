package com.example.nine_men_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        EasySplashScreen splashScreenConfig = new EasySplashScreen(SplashScreen.this)
                        .withFullScreen()
                        .withTargetActivity(MainActivity.class)
                        .withSplashTimeOut(1000)
                        .withBackgroundColor(Color.parseColor("#1a1b29"))
                        .withHeaderText("Header")
                        .withFooterText("Footer")
                        .withBeforeLogoText("Before logo text")
                        .withAfterLogoText("After logo text")
                        .withLogo(R.mipmap.ic_launcher_round)
                        .withTargetActivity(GameActivity.class);

        splashScreenConfig.getHeaderTextView().setTextColor(Color.WHITE);
        splashScreenConfig.getFooterTextView().setTextColor(Color.WHITE);
        splashScreenConfig.getBeforeLogoTextView().setTextColor(Color.WHITE);
        splashScreenConfig.getAfterLogoTextView().setTextColor(Color.WHITE);

        View splashScreen = splashScreenConfig.create();
        setContentView(splashScreen);
    }
}