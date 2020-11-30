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
import android.widget.Switch;
import android.widget.Toast;

import com.example.nine_men_morris.model.NineMenMorrisRules;
import com.example.nine_men_morris.model.Player;

import java.util.ArrayList;

/**
 * https://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
 */
public class GameActivity extends AppCompatActivity implements View.OnTouchListener{

    private GameView gameView;
    private ImageView blueChecker1, redChecker1,
                        blueChecker2, redChecker2,
                        blueChecker3, redChecker3,
                        blueChecker4, redChecker4,
                        blueChecker5, redChecker5,
                        blueChecker6, redChecker6,
                        blueChecker7, redChecker7,
                        blueChecker8, redChecker8,
                        blueChecker9, redChecker9;
    private ArrayList<ImageView> redCheckers, blueCheckers;
    private NineMenMorrisRules rules;
    private ArrayList<Rect> validPlaces;
    private Player playerRed, playerBlue;
    private int height, width, placeInBoard, imageID;
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

        initCheckers();

        rules = new NineMenMorrisRules();
        validPlaces = new ArrayList<>();
        playerRed = new Player(2); //RED
        playerBlue = new Player(1); //Blue
        initHitboxes();
    }

    private void initCheckers(){
        blueCheckers = new ArrayList<>();
        redCheckers = new ArrayList<>();

        blueChecker1 = (ImageView) findViewById(R.id.blueCheck1);
        redChecker1 = (ImageView) findViewById(R.id.redCheck1);
        blueCheckers.add(blueChecker1);
        redCheckers.add(redChecker1);

        blueChecker2 = findViewById(R.id.blueCheck2);
        redChecker2 = findViewById(R.id.redCheck2);
        blueCheckers.add(blueChecker2);
        redCheckers.add(redChecker2);

        blueChecker3 = findViewById(R.id.blueCheck3);
        redChecker3 = findViewById(R.id.redCheck3);
        blueCheckers.add(blueChecker3);
        redCheckers.add(redChecker3);

        blueChecker4 = findViewById(R.id.blueCheck4);
        redChecker4 = findViewById(R.id.redCheck4);
        blueCheckers.add(blueChecker4);
        redCheckers.add(redChecker4);

        blueChecker5 = findViewById(R.id.blueCheck5);
        redChecker5 = findViewById(R.id.redCheck5);
        blueCheckers.add(blueChecker5);
        redCheckers.add(redChecker5);

        blueChecker6 = findViewById(R.id.blueCheck6);
        redChecker6 = findViewById(R.id.redCheck6);
        blueCheckers.add(blueChecker6);
        redCheckers.add(redChecker6);

        blueChecker7 = findViewById(R.id.blueCheck7);
        redChecker7 = findViewById(R.id.redCheck7);
        blueCheckers.add(blueChecker7);
        redCheckers.add(redChecker7);

        blueChecker8 = findViewById(R.id.blueCheck8);
        redChecker8 = findViewById(R.id.redCheck8);
        blueCheckers.add(blueChecker8);
        redCheckers.add(redChecker8);

        blueChecker9 = findViewById(R.id.blueCheck9);
        redChecker9 = findViewById(R.id.redCheck9);
        blueCheckers.add(blueChecker9);
        redCheckers.add(redChecker9);



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
        Log.d("TAG", "onTouch: "+ v.getId());
        switch (v.getId()){
            case R.id.redCheck1:
                Log.d("TAG", "onClick: REDCHECK11111111");
                imageID = 0;
                break;
            case R.id.blueCheck1:
                Log.d("TAG", "onClick: REDCHECK11111111");
                imageID = 0;
                break;
            case R.id.redCheck2:
                Log.d("TAG", "onClick: REDCHECK11111111");
                imageID = 1;
                break;
            case R.id.blueCheck2:
                imageID = 1;
                break;
            case R.id.redCheck3:
                imageID = 2;
                break;
            case R.id.blueCheck3:
                imageID = 2;
                break;
            case R.id.redCheck4:
                imageID = 3;
                break;
            case R.id.blueCheck4:
                imageID = 3;
                break;
            case R.id.redCheck5:
                imageID = 4;
                break;
            case R.id.blueCheck5:
                imageID = 4;
                break;
            case R.id.redCheck6:
                imageID = 5;
                break;
            case R.id.blueCheck6:
                imageID = 5;
                break;
            case R.id.redCheck7:
                imageID = 6;
                break;
            case R.id.blueCheck7:
                imageID = 6;
                break;
            case R.id.redCheck8:
                imageID = 7;
                break;
            case R.id.blueCheck8:
                imageID = 7;
                break;
            case R.id.redCheck9:
                imageID = 8;
                break;
            case R.id.blueCheck9:
                imageID = 8;
                break;
        }

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
                                animationSet.play(ObjectAnimator.ofFloat(getCurrentChecker(), "X", getCentrumXOfHitbox()))
                                        .after(ObjectAnimator.ofFloat(getCurrentChecker(), "Y", getCentrumYOfHitbox()));
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
                                animationSet.play(ObjectAnimator.ofFloat(getCurrentChecker(), "x", getCentrumXOfHitbox()))
                                        .after(ObjectAnimator.ofFloat(getCurrentChecker(), "y", getCentrumYOfHitbox()));
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
                                   // imageID = v.getId();
                                  //  Toast.makeText(this, "Image clicked: " + imageID, Toast.LENGTH_LONG).show();
                                    for (int i = 0; i < playerBlue.getMoves().size(); i++) {

                                        int tmp = playerBlue.getMoves().get(i);
                                        if (playerBlue.getMovePieceFrom() == tmp) {
                                            playerBlue.getMoves().remove(i);
                                            rules.remove(playerBlue.getMovePieceFrom(), 4);

                                        }
                                    }
                                    playerBlue.getMoves().add(placeInBoard);
                                    playerBlue.setMovePieceTo(0); //moveto = 0
                                    playerBlue.setMovePieceFrom(0); //movefrom = 0
                                    Log.d("TAG", "onTouch: " + imageID);
                                    AnimatorSet animationSet = new AnimatorSet();
                                    animationSet.play(ObjectAnimator.ofFloat(getSelectedChecker(), "x", getCentrumXOfHitbox()))
                                            .after(ObjectAnimator.ofFloat(getSelectedChecker(), "y", getCentrumYOfHitbox()));
                                    animationSet.setDuration(1000);
                                    animationSet.start();
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
                                   //imageID = v.getId();
                                    //Toast.makeText(this, "Image clicked: " + imageID, Toast.LENGTH_LONG).show();
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
                                    Log.d("TAG", "onTouch: " + imageID);
                                    AnimatorSet animationSet = new AnimatorSet();
                                    animationSet.play(ObjectAnimator.ofFloat(getSelectedChecker(), "x", getCentrumXOfHitbox()))
                                            .with(ObjectAnimator.ofFloat(getSelectedChecker(), "y", getCentrumYOfHitbox()));
                                    animationSet.setDuration(1000);
                                    animationSet.start();
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
                                    redCheckers.get(i+1).setVisibility(View.GONE);
                                    redCheckers.remove(playerRed.getMoves().get(i));

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
                                    blueCheckers.get(i+1).setVisibility(View.GONE);
                                    blueCheckers.remove(playerBlue.getMoves().get(i));
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

    private ImageView getCurrentChecker()
    {
       if (rules.getTurn() == 1){//Red
           return redCheckers.get(playerRed.getNrOfMarkersPlaced()-1);

       }else if (rules.getTurn() == 2){//blue
           return blueCheckers.get(playerBlue.getNrOfMarkersPlaced()-1);
       }
       return null;
    }

    private ImageView getSelectedChecker(){
        if (rules.getTurn() == 1){//Red
            Log.d("TAG", "getSelectedChecker: " + imageID);
            return redCheckers.get(imageID);

        }else if (rules.getTurn() == 2){//blue
            Log.d("TAG", "getSelectedChecker: " + imageID);
            return blueCheckers.get(imageID);
        }
        return null;
    }

    private float getCentrumXOfHitbox(){

        float x = width - validPlaces.get(placeInBoard-1).left;
        float z = width - validPlaces.get(placeInBoard-1).right;

        return (width - (z + ((x-z)/2)) - redChecker1.getWidth()/2);
    }

    private float getCentrumYOfHitbox(){
        float x = height - validPlaces.get(placeInBoard-1).bottom;
        float z = height - validPlaces.get(placeInBoard-1).top;

        return (height - (z + ((x-z)/2)) - redChecker1.getHeight()/2);
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