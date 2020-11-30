package com.example.nine_men_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.nine_men_morris.model.NineMenMorrisRules;
import com.example.nine_men_morris.model.Player;

import java.util.ArrayList;

/**
 * https://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
 */
public class GameActivity extends AppCompatActivity implements View.OnTouchListener{

    private GameView gameView;
    private ImageView blueChecker, redChecker;

    private NineMenMorrisRules rules;
    private ArrayList<Rect> validPlaces;
    private Player playerRed, playerBlue;
    private int height, width, placeInBoard;
    private float touchedX, touchedY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        gameView = findViewById(R.id.drawView);

        blueChecker = findViewById(R.id.blueCheck1);
        redChecker = findViewById(R.id.redCheck1);
        rules = new NineMenMorrisRules();
        validPlaces = new ArrayList<>();
        playerRed = new Player(2); //RED
        playerBlue = new Player(1); //Blue
        initHitboxes();
    }

    @Override
    protected void onStart(){
        super.onStart();
        gameView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
                                AnimatorSet animationSet = new AnimatorSet();
                                animationSet.play(ObjectAnimator.ofFloat(blueChecker, "x", event.getX()))
                                        .with(ObjectAnimator.ofFloat(blueChecker, "y", event.getY()));
                                animationSet.setDuration(1000);
                                animationSet.start();
                                v.invalidate();

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
                                AnimatorSet animationSet = new AnimatorSet();

                                animationSet.play(ObjectAnimator.ofFloat(redChecker, "x", event.getX()))
                                        .with(ObjectAnimator.ofFloat(redChecker, "y", event.getY()));
                                animationSet.setDuration(1000);
                                animationSet.start();
                                v.invalidate();
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
                                    v.invalidate();

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
                                    v.invalidate();

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
                                    v.invalidate();
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
                                    v.invalidate();
                                }
                            }
                        }
                    }
                }
                if (checkWin(playerBlue) || checkWin(playerRed)){
                   // gameFinished();
                }

                return true;


            default:
                return false;
        }
    }

    private void checkHitboxForAnimation()
    {

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
                Log.d("TAG", "checkValid: hello");
                validity = true;
                placeInBoard = i+1;
            } else if ((validPlaces.get(i).left <= touchedX ) && (validPlaces.get(i).top <= touchedY) && (validPlaces.get(i).right >= touchedX) && (validPlaces.get(i).bottom >= touchedY)){
                Log.d("TAG", "checkValid: hello");
                validity = true;
                placeInBoard = i+1;
            } else if ((validPlaces.get(i).left >= touchedX ) && (validPlaces.get(i).top >= touchedY) && (validPlaces.get(i).right <= touchedX) && (validPlaces.get(i).bottom <= touchedY)){
                Log.d("TAG", "checkValid: hello");
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