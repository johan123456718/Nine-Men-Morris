package com.example.nine_men_morris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.nine_men_morris.model.InternalFile;
import com.example.nine_men_morris.model.NineMenMorrisRules;
import com.example.nine_men_morris.model.Player;
import com.example.nine_men_morris.model.godClass;

import java.io.File;
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
    private ArrayList<godClass> RedGods, BlueGods;
    private InternalFile internalFile;
    private File dir;
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

        internalFile = InternalFile.getInstance();


        initCheckers();

        rules = new NineMenMorrisRules();
        validPlaces = new ArrayList<>();
        playerRed = new Player(2); //RED
        playerBlue = new Player(1); //Blue
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            initHitboxesPortrait();
        }else {
            initHitboxesLandscape();
        }

        gameView = findViewById(R.id.drawView);


    }

    private void initCheckers(){
        blueCheckers = new ArrayList<>();
        redCheckers = new ArrayList<>();
        RedGods = new ArrayList<>();
        BlueGods = new ArrayList<>();

        blueChecker1 = (ImageView) findViewById(R.id.blueCheck1);
        redChecker1 = (ImageView) findViewById(R.id.redCheck1);
        blueCheckers.add(blueChecker1);
        redCheckers.add(redChecker1);
        RedGods.add(new godClass(redChecker1));
        BlueGods.add(new godClass(blueChecker1));

        blueChecker2 = findViewById(R.id.blueCheck2);
        redChecker2 = findViewById(R.id.redCheck2);
        blueCheckers.add(blueChecker2);
        redCheckers.add(redChecker2);
        RedGods.add(new godClass(redChecker2));
        BlueGods.add(new godClass(blueChecker2));

        blueChecker3 = findViewById(R.id.blueCheck3);
        redChecker3 = findViewById(R.id.redCheck3);
        blueCheckers.add(blueChecker3);
        redCheckers.add(redChecker3);
        RedGods.add(new godClass(redChecker3));
        BlueGods.add(new godClass(blueChecker3));

        blueChecker4 = findViewById(R.id.blueCheck4);
        redChecker4 = findViewById(R.id.redCheck4);
        blueCheckers.add(blueChecker4);
        redCheckers.add(redChecker4);
        RedGods.add(new godClass(redChecker4));
        BlueGods.add(new godClass(blueChecker4));

        blueChecker5 = findViewById(R.id.blueCheck5);
        redChecker5 = findViewById(R.id.redCheck5);
        blueCheckers.add(blueChecker5);
        redCheckers.add(redChecker5);
        RedGods.add(new godClass(redChecker5));
        BlueGods.add(new godClass(blueChecker5));

        blueChecker6 = findViewById(R.id.blueCheck6);
        redChecker6 = findViewById(R.id.redCheck6);
        blueCheckers.add(blueChecker6);
        redCheckers.add(redChecker6);
        RedGods.add(new godClass(redChecker6));
        BlueGods.add(new godClass(blueChecker6));

        blueChecker7 = findViewById(R.id.blueCheck7);
        redChecker7 = findViewById(R.id.redCheck7);
        blueCheckers.add(blueChecker7);
        redCheckers.add(redChecker7);
        RedGods.add(new godClass(redChecker7));
        BlueGods.add(new godClass(blueChecker7));

        blueChecker8 = findViewById(R.id.blueCheck8);
        redChecker8 = findViewById(R.id.redCheck8);
        blueCheckers.add(blueChecker8);
        redCheckers.add(redChecker8);
        RedGods.add(new godClass(redChecker8));
        BlueGods.add(new godClass(blueChecker8));

        blueChecker9 = findViewById(R.id.blueCheck9);
        redChecker9 = findViewById(R.id.redCheck9);
        blueCheckers.add(blueChecker9);
        redCheckers.add(redChecker9);
        RedGods.add(new godClass(redChecker9));
        BlueGods.add(new godClass(blueChecker9));

    }

    @Override
    protected void onStart(){
        super.onStart();
        dir = getFilesDir();
        internalFile.loadData(dir, playerBlue, playerRed);
        for(int i: playerBlue.getMoves()) {
            Log.d("TAG", "PlayerBlue.getMoves.get: " + i);
        }
        for(int j: playerRed.getMoves()) {
            Log.d("TAG", "playerRed.getMoves().get: " + j);
        }

        replaceCheckers();

        Log.d("TAG", "playerBlue.getNrOfMarkersPlaced(): " + playerBlue.getNrOfMarkersPlaced());
        Log.d("TAG", "playerRed.getNrOfMarkersPlaced(): " + playerRed.getNrOfMarkersPlaced());
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

                if (rules.getState() != 1){ // Blå för placer en checker innan 9
                    if ((playerRed.getNrOfMarkersPlaced() < 9)
                            || (playerBlue.getNrOfMarkersPlaced() < 9)) {
                        if (rules.getTurn() == playerBlue.getColorID()) {
                            if (checkValid() && rules.legalMove(placeInBoard, playerBlue.getMovePieceTo(), playerBlue.getColorID())) {
                                playerBlue.getMoves().add(placeInBoard);
                                BlueGods.get(playerBlue.getNrOfMarkersPlaced()).setCurrentHitbox(placeInBoard);
                                playerBlue.setNrOfMarkersPlaced(playerBlue.getNrOfMarkersPlaced() + 1);
                                playerBlue.setNrOfMarkers(playerBlue.getNrOfMarkers() - 1);

                                AnimatorSet animationSet = new AnimatorSet();
                                animationSet.play(ObjectAnimator.ofFloat(getCurrentChecker(), "X", getCentrumXOfHitbox()))
                                        .with(ObjectAnimator.ofFloat(getCurrentChecker(), "Y", getCentrumYOfHitbox()));
                                animationSet.setDuration(1000);
                                animationSet.start();

                                v.invalidate();

                                if(rules.remove(placeInBoard)){
                                    rules.setState(1);
                                    rules.setTurn(playerBlue.getColorID());
                                }
                            }
                        } else { // röd för palcera en checker innan 9
                            if (checkValid() && rules.legalMove(placeInBoard, playerRed.getMovePieceTo(), playerRed.getColorID())) {
                                playerRed.getMoves().add(placeInBoard);
                                RedGods.get(playerRed.getNrOfMarkersPlaced()).setCurrentHitbox(placeInBoard);
                                playerRed.setNrOfMarkersPlaced(playerRed.getNrOfMarkersPlaced() + 1);
                                playerRed.setNrOfMarkers(playerRed.getNrOfMarkers() - 1);

                                AnimatorSet animationSet = new AnimatorSet();
                                animationSet.play(ObjectAnimator.ofFloat(getCurrentChecker(), "x", getCentrumXOfHitbox()))
                                        .with(ObjectAnimator.ofFloat(getCurrentChecker(), "y", getCentrumYOfHitbox()));
                                animationSet.setDuration(1000);
                                animationSet.start();

                                v.invalidate();
                                if(rules.remove(placeInBoard)){
                                    rules.setState(1);
                                    rules.setTurn(playerRed.getColorID());
                                }
                            }
                        }
                    } else { // blå får flytta en av sina checkers
                        if (rules.getTurn() == playerBlue.getColorID()) {
                            if (playerBlue.getMovePieceTo() != 0) {
                                if (checkValid() && rules.legalMove(placeInBoard, playerBlue.getMovePieceTo(), playerBlue.getColorID())) {
                                   // imageID = v.getId();
                                  //  Toast.makeText(this, "Image clicked: " + imageID, Toast.LENGTH_LONG).show();
                                    for (int i = 0; i < playerBlue.getMoves().size(); i++) {

                                        int tmp = playerBlue.getMoves().get(i);
                                        if (playerBlue.getMovePieceFrom() == tmp) {
                                            for (godClass e: BlueGods){
                                                if (e.getCurrentHitbox() == playerBlue.getMovePieceFrom()){
                                                    e.setCurrentHitbox(placeInBoard);
                                                    playerBlue.setMovePieceTo(placeInBoard);
                                                }
                                            }
                                            playerBlue.getMoves().remove(i);
                                            rules.remove(playerBlue.getMovePieceFrom(), 4);
                                        }
                                    }
                                    playerBlue.getMoves().add(placeInBoard);

                                    AnimatorSet animationSet = new AnimatorSet();
                                    animationSet.play(ObjectAnimator.ofFloat(getSelectedChecker(), "x", getCentrumXOfHitbox()))
                                            .with(ObjectAnimator.ofFloat(getSelectedChecker(), "y", getCentrumYOfHitbox()));
                                    animationSet.setDuration(1000);
                                    animationSet.start();

                                    playerBlue.setMovePieceTo(0); //moveto = 0
                                    playerBlue.setMovePieceFrom(0); //movefrom = 0

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
                        } else { //Röd får flytta en chercker
                            if (playerRed.getMovePieceTo() != 0) {
                                if (checkValid() && rules.legalMove(placeInBoard, playerRed.getMovePieceTo(), playerRed.getColorID())) {
                                   //imageID = v.getId();
                                    //Toast.makeText(this, "Image clicked: " + imageID, Toast.LENGTH_LONG).show();
                                    for (int i = 0; i < playerRed.getMoves().size(); i++) {
                                        int tmp = playerRed.getMoves().get(i);
                                        if (playerRed.getMovePieceFrom() == tmp) {
                                            for (godClass e: RedGods){
                                                if (e.getCurrentHitbox() == playerRed.getMovePieceFrom()){
                                                    e.setCurrentHitbox(placeInBoard);
                                                    Log.d("TAG", "onTouch: efrter: " + e.getCurrentHitbox());
                                                    playerRed.setMovePieceTo(placeInBoard);
                                                }
                                            }
                                            Log.d("TAG", "onTouch: " + rules.board(placeInBoard));
                                            playerRed.getMoves().remove(i);
                                            rules.remove(playerRed.getMovePieceFrom(), 5);
                                        }
                                    }
                                    playerRed.getMoves().add(placeInBoard);


                                    AnimatorSet animationSet = new AnimatorSet();
                                    animationSet.play(ObjectAnimator.ofFloat(getSelectedChecker(), "x", getCentrumXOfHitbox()))
                                            .with(ObjectAnimator.ofFloat(getSelectedChecker(), "y", getCentrumYOfHitbox()));
                                    animationSet.setDuration(1000);
                                    animationSet.start();

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

                else if(rules.getState() == 1){ // Blå får ta bort en av röds checkers
                    if(rules.remove(placeInBoard) && (rules.getTurn() == playerBlue.getColorID())){
                        if(checkValid() && (rules.board(placeInBoard) == 5)){

                            for(int i = 0; i < playerRed.getMoves().size(); i++) {
                                int tmp = playerRed.getMoves().get(i);
                                if(tmp == placeInBoard) {
                                    rules.setTurn(playerRed.getColorID());
                                    for (godClass e: RedGods){
                                        if(e.getCurrentHitbox() == tmp){
                                            e.setCurrentHitbox(0);
                                            e.setDeleted(true);
                                            e.getCheckerId().setVisibility(View.GONE);
                                        }
                                    }


                                    playerRed.getMoves().remove(i);
                                    rules.remove(placeInBoard, 5);
                                    rules.setState(0);
                                    v.invalidate();
                                }
                            }
                        }
                    } else if(rules.remove(placeInBoard) && (rules.getTurn() == playerRed.getColorID())){//Röd får ta bort en av blås cehckers

                        if(checkValid() && (rules.board(placeInBoard) == 4)){

                            for(int i = 0; i < playerBlue.getMoves().size(); i++) {
                                int tmp = playerBlue.getMoves().get(i);
                                if(tmp == placeInBoard) {
                                    for (godClass e: BlueGods){
                                        if(e.getCurrentHitbox() == tmp){
                                            e.setCurrentHitbox(0);
                                            e.setDeleted(true);
                                            e.getCheckerId().setVisibility(View.GONE);
                                            //e.setCheckerId(null);
                                        }
                                    }

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
                if (checkWin(playerRed)){
                   // gameFinished();
                    new AlertDialog.Builder(this).setTitle("Game finished").setMessage("Blue has won!").setNeutralButton("Return to menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }else if(checkWin(playerBlue)){
                    new AlertDialog.Builder(this).setTitle("Game finished").setMessage("Red has won!").setNeutralButton("Return to menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
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
            for (godClass checker: RedGods){
                //Log.d("TAG", "getSelectedChecker: " + checker.getCurrentHitbox() + " curfrom: " + playerRed.getMovePieceFrom());
                if(checker.getCurrentHitbox() == playerRed.getMovePieceTo()){
                  //  Log.d("TAG", "getSelectedChecker: " + checker.getCurrentHitbox() + " from: " + playerRed.getMovePieceFrom() + " ID: " + checker.getCheckerId());
                    return checker.getCheckerId();
                }
            }
        }else if (rules.getTurn() == 2){//blue
            for (godClass checker: BlueGods){
                if(checker.getCurrentHitbox() == playerBlue.getMovePieceTo()){
                    return checker.getCheckerId();
                }
            }
        }
        return null;
    }

    private float getCentrumHitboxXReload(Rect rect){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            float x = width - rect.left;
            float z = width - rect.right;

            return (width - (z + ((x-z)/2)) - redChecker1.getWidth()/2);
        }
        else{
            Log.d("TAG", "getCentrumXOfHitbox: " + gameView.getX());
            float x = gameView.getX() + rect.left;
            float z = gameView.getX() + rect.right;

            return ((z + ((x-z)/2)) - redChecker1.getWidth()/2);
        }
    }

    private float getCentrumHitboxYReload(Rect rect){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            float x = height - rect.bottom;
            float z = height - rect.top;

            return (height - (z + ((x - z) / 2)) - redChecker1.getHeight() / 2);
        }else{
            float x = height - rect.bottom;
            float z = height - rect.top;

            return (height - (z + ((x - z) / 2)) - redChecker1.getHeight() / 2);
        }
    }

    private void replaceCheckers(){
        for (int i= 0; i < playerBlue.getMoves().size(); i++){
            //Log.d("TAG", "replaceCheckers: " + validPlaces.get(playerBlue.getMoves().get(i)));
            Rect rect = validPlaces.get(playerBlue.getMoves().get(i));
            Log.d("TAG", "replaceCheckers: " + getCentrumHitboxYReload(rect) + " X; " + getCentrumHitboxXReload(rect));

            blueCheckers.get(i).setY(getCentrumHitboxYReload(rect));
            blueCheckers.get(i).setX(getCentrumHitboxXReload(rect));
            gameView.invalidate();

        }
        for (int i= 0; i < playerRed.getMoves().size(); i++){
            Rect rect = validPlaces.get(playerRed.getMoves().get(i));

            redCheckers.get(i).setX(getCentrumHitboxXReload(rect));
            redCheckers.get(i).setY(getCentrumHitboxYReload(rect));
            gameView.invalidate();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        new Thread(new Runnable(){
            @Override
            public void run() {
                dir = getFilesDir();
                internalFile.saveData(dir,
                        playerBlue.getMoves(),
                        playerRed.getMoves(),
                        playerBlue.getNrOfMarkersPlaced(),
                        playerRed.getNrOfMarkersPlaced());
                Log.d("TAG", "run: SAVED");
            }
        }).start();

    }

    private float getCentrumXOfHitbox(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            float x = width - validPlaces.get(placeInBoard-1).left;
            float z = width - validPlaces.get(placeInBoard-1).right;

            return (width - (z + ((x-z)/2)) - redChecker1.getWidth()/2);
        }
        else{
            Log.d("TAG", "getCentrumXOfHitbox: " + gameView.getX());
            float x = gameView.getX() + validPlaces.get(placeInBoard-1).left;
            float z = gameView.getX() + validPlaces.get(placeInBoard-1).right;

            return ((z + ((x-z)/2)) - redChecker1.getWidth()/2);
        }
    }

    private float getCentrumYOfHitbox(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            float x = height - validPlaces.get(placeInBoard - 1).bottom;
            float z = height - validPlaces.get(placeInBoard - 1).top;

            return (height - (z + ((x - z) / 2)) - redChecker1.getHeight() / 2);
        }else{
            float x = height - validPlaces.get(placeInBoard - 1).bottom;
            float z = height - validPlaces.get(placeInBoard - 1).top;

            return (height - (z + ((x - z) / 2)) - redChecker1.getHeight() / 2);
        }
    }

    private boolean checkWin(Player color){
        if(((color.getMoves().size() < 3) && (color.getNrOfMarkersPlaced() == 9))){
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

    private void initHitboxesPortrait(){
        Log.d("TAG", "initHitboxesPortrait: ");
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
    private void initHitboxesLandscape(){
        Log.d("TAG", "initHitboxesLandscape: " + validPlaces.size());

        //1
        Rect validplace = new Rect();
        validplace.left = width/4 -75;
        validplace.right = validplace.left + 100;
        validplace.top =(height/3) +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //2
        validplace = new Rect();
        validplace.left = width/8 -25;
        validplace.right = validplace.left + 100;
        validplace.top =((height/8) - (((height/8) - (height/8 + height/8 -50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //3
        validplace = new Rect();
        validplace.left =  25 ;
        validplace.right = validplace.left + 100;
        validplace.top = 25;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //4
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top =(height/3) +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //5
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top =((height/8) - (((height/8) - (height/8 + height/8 -50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //6
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = 25;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //7
        validplace = new Rect();
        validplace.left = (int)(width*0.44);
        validplace.right = validplace.left + 100;
        validplace.top =(height/3) +50;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //8
        validplace = new Rect();
        validplace.left = (int) (width*0.54);
        validplace.right = validplace.left + 100;
        validplace.top =((height/8) - (((height/8) - (height/8 + height/8 -50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlaces.add(validplace);

        //9
        validplace = new Rect();
        validplace.left = (int) (width*0.65);
        validplace.right = validplace.left + 100;
        validplace.top = 25;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //10
        validplace = new Rect();
        validplace.left = (int)(width*0.44);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //11
        validplace = new Rect();
        validplace.left = (int) (width*0.54);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //12
        validplace = new Rect();
        validplace.left = (int) (width*0.65);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //13
        validplace = new Rect();
        validplace.left = (int)(width*0.44);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.57);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //14
        validplace = new Rect();
        validplace.left = (int) (width*0.54);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.71);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //15
        validplace = new Rect();
        validplace.left = (int) (width*0.65);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.85);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //16
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.57);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //17
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.71);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //18
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.85);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //19
        validplace = new Rect();
        validplace.left = width/4 -75;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.57);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //20
        validplace = new Rect();
        validplace.left = width/8 -25;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.71);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //21
        validplace = new Rect();
        validplace.left =  25 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.85);
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //22
        validplace = new Rect();
        validplace.left = width/4 -75;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //23
        validplace = new Rect();
        validplace.left = width/8 -25;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

        //nr 24
        validplace = new Rect();
        validplace.left = 25;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlaces.add(validplace);

    }

}