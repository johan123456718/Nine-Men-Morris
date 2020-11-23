package com.example.nine_men_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.nine_men_morris.model.NineMenMorrisRules;

/**
 * https://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
 */
public class GameActivity extends AppCompatActivity {

    private NineMenMorrisRules rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rules = new NineMenMorrisRules();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Test", "isLegalMove: "  + rules.legalMove(1, 0 , 2));
        Log.d("Test", "isLegalMove: "  + rules.legalMove(2, 0 , 1));
    }


}