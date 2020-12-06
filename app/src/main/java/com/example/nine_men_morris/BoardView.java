package com.example.nine_men_morris;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * View class of the gameboard
 */
public class BoardView extends View {

    private Bitmap bg;
    private Rect rect;

    private int height, width;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            bg = BitmapFactory.decodeResource(getResources(), R.mipmap.nnm);
            rect = new Rect(0,0, (int) (width*0.70), (int) (height*0.93));
        }
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
