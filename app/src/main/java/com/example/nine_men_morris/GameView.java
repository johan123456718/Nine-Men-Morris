package com.example.nine_men_morris;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.nine_men_morris.model.NineMenMorrisRules;
import com.example.nine_men_morris.model.Player;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class GameView extends View {

    private NineMenMorrisRules rules;
    private Bitmap bg;
    private Rect rect;
    private ArrayList<Rect> validPlaces;
    private Player playerRed, playerBlue;
    private int height, width, placeInBoard;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;

            Log.d("TAG", "portrait: height: " + height + " width: " + width);

            playerRed = new Player(2); //RED
            playerBlue = new Player(1); //Blue

            validPlaces = new ArrayList<>();
            //initHitboxesPortrait();

            bg = BitmapFactory.decodeResource(getResources(), R.mipmap.nnm);

            rect = new Rect(0,height/4, width, (int) (height*0.75));
        }
        else{
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            Log.d("TAG", "landscape: height: " + height + " width: " + width);
            playerRed = new Player(2); //RED
            playerBlue = new Player(1); //Blue

            validPlaces = new ArrayList<>();
            //initHitboxesLandscape();

            bg = BitmapFactory.decodeResource(getResources(), R.mipmap.nnm);

           // Log.d("TAG", "init: " + getResources().getDimension(R.id.drawView));
            rect = new Rect(0,0, (int) (width*0.70), (int) (height*0.93));
            //rect = new Rect(left,top,right,bottom)
        }

        rules = new NineMenMorrisRules();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(251,251,203));
        canvas.drawRect(0,0,width, height, paint);

        //Draw background
        canvas.drawBitmap(bg,null,rect,null);


    }



}
