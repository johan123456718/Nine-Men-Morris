package com.example.nine_men_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.example.nine_men_morris.model.NineMenMorrisRules;

/**
 * https://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
 */
public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameView = new GameView(this);

        setContentView(gameView);
    }

    public void gameFinished(){

    }

}