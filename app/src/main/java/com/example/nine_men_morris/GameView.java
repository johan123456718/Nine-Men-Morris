package com.example.nine_men_morris;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
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
    private float touchedX, touchedY;

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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        playerRed = new Player(2); //RED
        playerBlue = new Player(1); //Blue

       // validPlaces = new ArrayList<>();
        //initHitboxes();

        bg = BitmapFactory.decodeResource(getResources(), R.mipmap.nnm);

        rect = new Rect(0,height/4, width, (int) (height*0.75));

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

       /* for(int i = 0; i < playerRed.getMoves().size(); i++){
            paint.setColor(Color.RED);
            canvas.drawRect(validPlaces.get(playerRed.getMoves().get(i)-1) , paint);

        }
        for(int i = 0; i< playerBlue.getMoves().size(); i++){
            paint.setColor(Color.BLUE);
            canvas.drawRect(validPlaces.get(playerBlue.getMoves().get(i)-1) , paint);
        }*/

        paint.setColor(Color.BLACK);
        paint.setTextSize(45);

        canvas.drawText("Red markers to place: " + (playerRed.getNrOfMarkers()), 100, 100, paint);
        canvas.drawText("Blue markers to place: " + (playerBlue.getNrOfMarkers()), 100, 150, paint);

        canvas.drawText("Red markers on board: " + (playerRed.getMoves().size()), 100, 300, paint);
        canvas.drawText("Blue markers on board: " + (playerBlue.getMoves().size()), 100, 350, paint);

        if(rules.getTurn() == playerRed.getColorID()) {
            canvas.drawText("Red's turn", 100, 400, paint);
        }else if(rules.getTurn() == playerBlue.getColorID()){
            canvas.drawText("Blue's turn", 100, 400, paint);
        }

        if(rules.getState() == 1) {
            if (rules.remove(placeInBoard) && rules.getTurn() == playerRed.getColorID()) {
                canvas.drawText("Red can remove", 100, 500, paint);
            }

            if (rules.remove(placeInBoard) && rules.getTurn() == playerBlue.getColorID()) {
                canvas.drawText("Blue can remove", 100, 500, paint);
            }
        }

    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchedX = event.getX();
        touchedY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                if (rules.getState() != 1){

                    if ((playerRed.getNrOfMarkersPlaced() < 9)
                        || (playerBlue.getNrOfMarkersPlaced() < 9)) {

                    if (rules.getTurn() == playerBlue.getColorID()) {

                        if (checkValid() && rules.legalMove(placeInBoard, playerBlue.getMovePieceTo(), playerBlue.getColorID())) {
                            playerBlue.getMoves().add(placeInBoard);
                            playerBlue.setNrOfMarkersPlaced(playerBlue.getNrOfMarkersPlaced() + 1);
                            playerBlue.setNrOfMarkers(playerBlue.getNrOfMarkers() - 1);
                            invalidate();

                            if(rules.remove(placeInBoard)){
                                rules.setState(1);
                                rules.setTurn(playerBlue.getColorID());
                            }
                        }
                    } else {
                        if (checkValid() && rules.legalMove(placeInBoard, playerRed.getMovePieceTo(), playerRed.getColorID())) {
                            playerRed.getMoves().add(placeInBoard);
                            playerRed.setNrOfMarkersPlaced(playerRed.getNrOfMarkersPlaced() + 1);
                            playerRed.setNrOfMarkers(playerRed.getNrOfMarkers() - 1);
                            invalidate();
                            if(rules.remove(placeInBoard)){
                                rules.setState(1);
                                rules.setTurn(playerRed.getColorID());
                            }
                        }
                    }
                } else {
                    if (rules.getTurn() == playerBlue.getColorID()) {
                        if (playerBlue.getMovePieceTo() != 0) {
                            if (checkValid() && rules.legalMove(placeInBoard, playerBlue.getMovePieceTo(), playerBlue.getColorID())) {
                                for (int i = 0; i < playerBlue.getMoves().size(); i++) {

                                    int tmp = playerBlue.getMoves().get(i);
                                    if (playerBlue.getMovePieceFrom() == tmp) {
                                        playerBlue.getMoves().remove(i);
                                        rules.remove(playerBlue.getMovePieceFrom(), 4);
                                    }
                                }
                                playerBlue.getMoves().add(placeInBoard);
                                playerBlue.setMovePieceTo(0);
                                playerBlue.setMovePieceFrom(0);
                                invalidate();

                                if(rules.remove(placeInBoard)){
                                    rules.setState(1);
                                    rules.setTurn(playerBlue.getColorID());
                                }
                            }
                            else {
                                playerBlue.setMovePieceTo(0);
                                playerBlue.setMovePieceFrom(0);
                            }
                        } else {

                            if (checkValid() && (rules.board(placeInBoard) == 4)) {
                                playerBlue.setMovePieceTo(placeInBoard);
                                playerBlue.setMovePieceFrom(placeInBoard);
                            }
                        }
                    } else {
                        if (playerRed.getMovePieceTo() != 0) {
                            if (checkValid() && rules.legalMove(placeInBoard, playerRed.getMovePieceTo(), playerRed.getColorID())) {
                                for (int i = 0; i < playerRed.getMoves().size(); i++) {

                                    int tmp = playerRed.getMoves().get(i);
                                    if (playerRed.getMovePieceFrom() == tmp) {
                                        playerRed.getMoves().remove(i);
                                        rules.remove(playerRed.getMovePieceFrom(), 5);
                                    }
                                }
                                playerRed.getMoves().add(placeInBoard);
                                playerRed.setMovePieceTo(0);
                                playerRed.setMovePieceFrom(0);
                                invalidate();

                                if(rules.remove(placeInBoard)){
                                    rules.setState(1);
                                    rules.setTurn(playerRed.getColorID());
                                }
                            } else {

                                playerRed.setMovePieceTo(0);
                                playerRed.setMovePieceFrom(0);
                            }
                        } else if (checkValid() && (rules.board(placeInBoard) == 5)) {

                                playerRed.setMovePieceTo(placeInBoard);
                                playerRed.setMovePieceFrom(placeInBoard);
                            }
                        }
                    }
                }

            else if(rules.getState() == 1){

                if(rules.remove(placeInBoard) && (rules.getTurn() == playerBlue.getColorID())){
                    if(checkValid() && (rules.board(placeInBoard) == 5)){

                        for(int i = 0; i < playerRed.getMoves().size(); i++) {
                            int tmp = playerRed.getMoves().get(i);
                            if(tmp == placeInBoard) {
                                rules.setTurn(playerRed.getColorID());
                                playerRed.getMoves().remove(i);
                                rules.remove(placeInBoard, 5);
                                rules.setState(0);
                                invalidate();
                            }
                        }
                    }
                } else if(rules.remove(placeInBoard) && (rules.getTurn() == playerRed.getColorID())){

                    if(checkValid() && (rules.board(placeInBoard) == 4)){

                        for(int i = 0; i < playerBlue.getMoves().size(); i++) {
                            int tmp = playerBlue.getMoves().get(i);
                            if(tmp == placeInBoard) {
                                playerBlue.getMoves().remove(i);
                                rules.remove(placeInBoard, 4);
                                rules.setState(0);
                                rules.setTurn(playerBlue.getColorID());
                                invalidate();
                            }
                        }
                    }
                }
            }
            if (checkWin(playerBlue) || checkWin(playerRed)){
                gameFinished();
            }

                AnimatorSet animationSet = new AnimatorSet();

                animationSet.play(ObjectAnimator.ofFloat(image, "x", 400))
                        .with(ObjectAnimator.ofFloat(image , "y", 400));
                animationSet.setDuration(1000);
                animationSet.start();
            return true;


            default:
                return false;
        }
    }*/

    private void gameFinished(){

    }

    private boolean checkWin(Player color){
        if((color.getMoves().size() < 3) && (color.getNrOfMarkersPlaced() == 9)){
            return true;
        }
        else return false;
    }

    private boolean checkValid(){
        boolean validity = false;
        for(int i = 0; i < validPlaces.size(); i++){
            if ((validPlaces.get(i).left <= touchedX ) && (validPlaces.get(i).top >= touchedY) && (validPlaces.get(i).right >= touchedX) && (validPlaces.get(i).bottom <= touchedY)){
                validity = true;
                placeInBoard = i+1;
            } else if ((validPlaces.get(i).left <= touchedX ) && (validPlaces.get(i).top <= touchedY) && (validPlaces.get(i).right >= touchedX) && (validPlaces.get(i).bottom >= touchedY)){
                validity = true;
                placeInBoard = i+1;
            } else if ((validPlaces.get(i).left >= touchedX ) && (validPlaces.get(i).top >= touchedY) && (validPlaces.get(i).right <= touchedX) && (validPlaces.get(i).bottom <= touchedY)){
                validity = true;
                placeInBoard = i+1;
            }
        }
        return validity;
    }

    private void initHitboxes(){

        //1
        Rect validplace = new Rect();
        validplace.left =  ((width/6)*3)/2 + 100 ;
        validplace.right = validplace.left + 100;
        validplace.top =((((height/4) - (((height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //2
        validplace = new Rect();
        validplace.right = width/6;
        validplace.left = validplace.right + 100;
        validplace.top =((height/4) - (((height/4) - (height/4 + height/4 +50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //nr 3
        validplace = new Rect();
        validplace.left = 25;
        validplace.top = height/4;
        validplace.right = validplace.left + 100;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //4
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =((((height/4) - (((height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //5
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =((height/4) - (((height/4) - (height/4 + height/4 +50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //nr 6
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.top = height/4;
        validplace.right = validplace.left + 100;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //7
        validplace = new Rect();
        validplace.left =  width - (((width/6)*3)/2 + 200) ;
        validplace.right = validplace.left + 100;
        validplace.top =((((height/4) - (((height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //8
        validplace = new Rect();
        validplace.right = ((width) - (width/6));
        validplace.left = validplace.right -100;
        validplace.top =((height/4) - (((height/4) - (height/4 + height/4 +50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //nr 9
        validplace = new Rect();
        validplace.right = width - 25;
        validplace.left = validplace.right -100;
        validplace.top = height/4;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        // 10
        validplace = new Rect();
        validplace.left =  width - (((width/6)*3)/2 + 200) ;
        validplace.right = validplace.left + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //11
        validplace = new Rect();
        validplace.right = ((width) - (width/6));
        validplace.left = validplace.right -100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //nr 12
        validplace = new Rect();
        validplace.right = width - 25;
        validplace.left = validplace.right -100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //13
        validplace = new Rect();
        validplace.left =  width - (((width/6)*3)/2 + 200) ;
        validplace.right = validplace.left + 100;
        validplace.top =(( ((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+25;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //14
        validplace = new Rect();
        validplace.right = ((width) - (width/6));
        validplace.left = validplace.right -100;
        validplace.top =((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3));
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //15
        validplace = new Rect();
        validplace.right = width - 25;
        validplace.left = validplace.right -100;
        validplace.top = height - height/4;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //16
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =(( ((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+25;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //17
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3));
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //18
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top = height - height/4;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //19
        validplace = new Rect();
        validplace.left =  ((width/6)*3)/2 + 100 ;
        validplace.right = validplace.left + 100;
        validplace.top =((((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+25;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //20
        validplace = new Rect();
        validplace.right = width/6;
        validplace.left = validplace.right + 100;
        validplace.top =((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3));
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //21
        validplace = new Rect();
        validplace.left = 25;
        validplace.right = validplace.left + 100;
        validplace.top = height - height/4;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //22
        validplace = new Rect();
        validplace.left =  ((width/6)*3)/2 + 100 ;
        validplace.right = validplace.left + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //23
        validplace = new Rect();
        validplace.right = width/6;
        validplace.left = validplace.right + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //24
        validplace = new Rect();
        validplace.left = 25;
        validplace.right = validplace.left + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

    }

}
