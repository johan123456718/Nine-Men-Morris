package com.example.nine_men_morris;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nine_men_morris.model.InternalFile;
import com.example.nine_men_morris.model.NineMenMorrisRules;
import com.example.nine_men_morris.model.Player;
import com.example.nine_men_morris.model.checkerViewModel;

import java.io.File;
import java.util.ArrayList;

/**
 * https://www.vogella.com/tutorials/AndroidDragAndDrop/article.html
 */
public class GameActivity extends AppCompatActivity implements View.OnTouchListener{

    private BoardView boardView;
    private ImageView blueChecker1, redChecker1,
                        blueChecker2, redChecker2,
                        blueChecker3, redChecker3,
                        blueChecker4, redChecker4,
                        blueChecker5, redChecker5,
                        blueChecker6, redChecker6,
                        blueChecker7, redChecker7,
                        blueChecker8, redChecker8,
                        blueChecker9, redChecker9;
    private TextView playerTurnText;
    private TextView playerRemoveText;
    private NineMenMorrisRules rules;
    private ArrayList<Rect> validPlacesInGameboard;
    private Player playerRed, playerBlue;
    private int height, width, placeClickedInBoard;
    private float touchedX, touchedY;
    private ArrayList<checkerViewModel> redCheckersViewModel, blueCheckersViewModel;
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
        validPlacesInGameboard = new ArrayList<>();
        playerRed = new Player(2); //RED
        playerBlue = new Player(1); //Blue
        playerTurnText = findViewById(R.id.playerTurn);
        playerRemoveText = findViewById(R.id.playerRemove);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            initHitboxesPortrait();
        }else {
            initHitboxesLandscape();
        }
        boardView = findViewById(R.id.drawView);
    }

    @Override
    protected void onStart(){
        super.onStart();
        dir = getFilesDir();
        internalFile.loadData(dir, playerBlue, playerRed, rules);

        boardView.setOnTouchListener(this);
        //if om värden != null
        rules.initAfterReload(playerRed, playerBlue);
        setCheckerStates();
        replaceCheckers();
        printPlayerTurn();
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
                        playerRed.getNrOfMarkersPlaced(),
                        rules.getTurn(),
                        rules.getState(),
                        playerBlue.getNrOfRemovedCheckers(),
                        playerRed.getNrOfRemovedCheckers());
            }
        }).start();
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
                            if (checkValid() && rules.legalMove(placeClickedInBoard, playerBlue.getMovePieceTo(), playerBlue.getColorID())) {
                                playerBlue.getMoves().add(placeClickedInBoard);
                                blueCheckersViewModel.get(playerBlue.getNrOfMarkersPlaced()).setCurrentHitbox(placeClickedInBoard);
                                playerBlue.setNrOfMarkersPlaced(playerBlue.getNrOfMarkersPlaced() + 1);
                                playerBlue.setNrOfMarkers(playerBlue.getNrOfMarkers() - 1);

                                AnimatorSet animationSet = new AnimatorSet();
                                animationSet.play(ObjectAnimator.ofFloat(getCurrentChecker(), "X", getCentrumXOfHitbox()))
                                        .with(ObjectAnimator.ofFloat(getCurrentChecker(), "Y", getCentrumYOfHitbox()));
                                animationSet.setDuration(1000);
                                animationSet.start();

                                v.invalidate();

                                if(rules.remove(placeClickedInBoard)){
                                    rules.setState(1);
                                    rules.setTurn(playerBlue.getColorID());
                                    playerRemoveText.setText("Blue can remove");
                                }
                            }
                        } else { // röd för palcera en checker innan 9
                            if (checkValid() && rules.legalMove(placeClickedInBoard, playerRed.getMovePieceTo(), playerRed.getColorID())) {
                                playerRed.getMoves().add(placeClickedInBoard);
                                redCheckersViewModel.get(playerRed.getNrOfMarkersPlaced()).setCurrentHitbox(placeClickedInBoard);
                                playerRed.setNrOfMarkersPlaced(playerRed.getNrOfMarkersPlaced() + 1);
                                playerRed.setNrOfMarkers(playerRed.getNrOfMarkers() - 1);

                                AnimatorSet animationSet = new AnimatorSet();
                                animationSet.play(ObjectAnimator.ofFloat(getCurrentChecker(), "x", getCentrumXOfHitbox()))
                                        .with(ObjectAnimator.ofFloat(getCurrentChecker(), "y", getCentrumYOfHitbox()));
                                animationSet.setDuration(1000);
                                animationSet.start();

                                v.invalidate();
                                if(rules.remove(placeClickedInBoard)){
                                    rules.setState(1);
                                    rules.setTurn(playerRed.getColorID());
                                    playerRemoveText.setText("Red can remove");
                                }
                            }
                        }
                    } else { // blå får flytta en av sina checkers
                        if (rules.getTurn() == playerBlue.getColorID()) {
                            if (playerBlue.getMovePieceTo() != 0) {
                                if (checkValid() && rules.legalMove(placeClickedInBoard, playerBlue.getMovePieceTo(), playerBlue.getColorID())) {
                                    for (int i = 0; i < playerBlue.getMoves().size(); i++) {

                                        int tmp = playerBlue.getMoves().get(i);
                                        if (playerBlue.getMovePieceFrom() == tmp) {
                                            for (checkerViewModel e: blueCheckersViewModel){
                                                if (e.getCurrentHitbox() == playerBlue.getMovePieceFrom()){
                                                    e.setCurrentHitbox(placeClickedInBoard);
                                                    playerBlue.setMovePieceTo(placeClickedInBoard);
                                                }
                                            }
                                            playerBlue.getMoves().remove(i);
                                            rules.remove(playerBlue.getMovePieceFrom(), 4);
                                        }
                                    }
                                    playerBlue.getMoves().add(placeClickedInBoard);

                                    AnimatorSet animationSet = new AnimatorSet();
                                    animationSet.play(ObjectAnimator.ofFloat(getSelectedChecker(), "x", getCentrumXOfHitbox()))
                                            .with(ObjectAnimator.ofFloat(getSelectedChecker(), "y", getCentrumYOfHitbox()));
                                    animationSet.setDuration(1000);
                                    animationSet.start();

                                    playerBlue.setMovePieceTo(0); //moveto = 0
                                    playerBlue.setMovePieceFrom(0); //movefrom = 0

                                    v.invalidate();

                                    if(rules.remove(placeClickedInBoard)){
                                        rules.setState(1);
                                        rules.setTurn(playerBlue.getColorID());
                                        playerRemoveText.setText("Blue can remove");
                                    }
                                }
                                else {
                                    playerBlue.setMovePieceTo(0);
                                    playerBlue.setMovePieceFrom(0);
                                }
                            } else {

                                if (checkValid() && (rules.board(placeClickedInBoard) == 4)) {
                                    playerBlue.setMovePieceTo(placeClickedInBoard);
                                    playerBlue.setMovePieceFrom(placeClickedInBoard);
                                }
                            }
                        } else { //Röd får flytta en chercker
                            if (playerRed.getMovePieceTo() != 0) {
                                if (checkValid() && rules.legalMove(placeClickedInBoard, playerRed.getMovePieceTo(), playerRed.getColorID())) {
                                    for (int i = 0; i < playerRed.getMoves().size(); i++) {
                                        int tmp = playerRed.getMoves().get(i);
                                        if (playerRed.getMovePieceFrom() == tmp) {
                                            for (checkerViewModel e: redCheckersViewModel){
                                                if (e.getCurrentHitbox() == playerRed.getMovePieceFrom()){
                                                    e.setCurrentHitbox(placeClickedInBoard);
                                                    playerRed.setMovePieceTo(placeClickedInBoard);
                                                }
                                            }
                                            playerRed.getMoves().remove(i);
                                            rules.remove(playerRed.getMovePieceFrom(), 5);
                                        }
                                    }
                                    playerRed.getMoves().add(placeClickedInBoard);

                                    AnimatorSet animationSet = new AnimatorSet();
                                    animationSet.play(ObjectAnimator.ofFloat(getSelectedChecker(), "x", getCentrumXOfHitbox()))
                                            .with(ObjectAnimator.ofFloat(getSelectedChecker(), "y", getCentrumYOfHitbox()));
                                    animationSet.setDuration(1000);
                                    animationSet.start();

                                    playerRed.setMovePieceTo(0);
                                    playerRed.setMovePieceFrom(0);
                                    v.invalidate();
                                    if(rules.remove(placeClickedInBoard)){
                                        rules.setState(1);
                                        rules.setTurn(playerRed.getColorID());
                                        playerRemoveText.setText("Red can remove");
                                    }
                                } else {
                                    playerRed.setMovePieceTo(0);
                                    playerRed.setMovePieceFrom(0);
                                }
                            } else if (checkValid() && (rules.board(placeClickedInBoard) == 5)) {
                                playerRed.setMovePieceTo(placeClickedInBoard);
                                playerRed.setMovePieceFrom(placeClickedInBoard);
                            }
                        }
                    }
                }

                else if(rules.getState() == 1){ // Blå får ta bort en av röds checkers
                    if(rules.remove(placeClickedInBoard) && (rules.getTurn() == playerBlue.getColorID())){
                        if(checkValid() && (rules.board(placeClickedInBoard) == 5)){
                            for(int i = 0; i < playerRed.getMoves().size(); i++) {
                                int tmp = playerRed.getMoves().get(i);
                                if(tmp == placeClickedInBoard) {
                                    rules.setTurn(playerRed.getColorID());
                                    for (checkerViewModel e: redCheckersViewModel){
                                        if(e.getCurrentHitbox() == tmp){
                                            e.setCurrentHitbox(0);
                                            e.setDeleted(true);
                                            e.getCheckerId().setVisibility(View.INVISIBLE);
                                            playerRed.setNrOfRemovedCheckers(playerRed.getNrOfRemovedCheckers()+1);
                                            playerRemoveText.setText("");
                                        }
                                    }
                                    playerRed.getMoves().remove(i);
                                    rules.remove(placeClickedInBoard, 5);
                                    rules.setState(0);
                                    v.invalidate();
                                }
                            }
                        }
                    } else if(rules.remove(placeClickedInBoard) && (rules.getTurn() == playerRed.getColorID())){//Röd får ta bort en av blås cehckers
                        if(checkValid() && (rules.board(placeClickedInBoard) == 4)){

                            for(int i = 0; i < playerBlue.getMoves().size(); i++) {
                                int tmp = playerBlue.getMoves().get(i);
                                if(tmp == placeClickedInBoard) {
                                    for (checkerViewModel e: blueCheckersViewModel){
                                        if(e.getCurrentHitbox() == tmp){
                                            e.setCurrentHitbox(0);
                                            e.setDeleted(true);
                                            e.getCheckerId().setVisibility(View.INVISIBLE);
                                            playerBlue.setNrOfRemovedCheckers(playerBlue.getNrOfRemovedCheckers()+1);
                                            playerRemoveText.setText("");
                                        }
                                    }
                                    playerBlue.getMoves().remove(i);

                                    rules.remove(placeClickedInBoard, 4);
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
                            internalFile.clearData();
                            finish();
                        }
                    }).show();
                }
                printPlayerTurn();
                return true;

            default:
                return false;
        }
    }


    /**
     * Helper method to initiate all the ImageViews for the checkers.
     */
    private void initCheckers(){

        redCheckersViewModel = new ArrayList<>();
        blueCheckersViewModel = new ArrayList<>();

        blueChecker1 = (ImageView) findViewById(R.id.blueCheck1);
        redChecker1 = (ImageView) findViewById(R.id.redCheck1);
        redCheckersViewModel.add(new checkerViewModel(redChecker1));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker1));

        blueChecker2 = findViewById(R.id.blueCheck2);
        redChecker2 = findViewById(R.id.redCheck2);
        redCheckersViewModel.add(new checkerViewModel(redChecker2));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker2));

        blueChecker3 = findViewById(R.id.blueCheck3);
        redChecker3 = findViewById(R.id.redCheck3);
        redCheckersViewModel.add(new checkerViewModel(redChecker3));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker3));

        blueChecker4 = findViewById(R.id.blueCheck4);
        redChecker4 = findViewById(R.id.redCheck4);
        redCheckersViewModel.add(new checkerViewModel(redChecker4));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker4));

        blueChecker5 = findViewById(R.id.blueCheck5);
        redChecker5 = findViewById(R.id.redCheck5);
        redCheckersViewModel.add(new checkerViewModel(redChecker5));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker5));

        blueChecker6 = findViewById(R.id.blueCheck6);
        redChecker6 = findViewById(R.id.redCheck6);
        redCheckersViewModel.add(new checkerViewModel(redChecker6));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker6));

        blueChecker7 = findViewById(R.id.blueCheck7);
        redChecker7 = findViewById(R.id.redCheck7);
        redCheckersViewModel.add(new checkerViewModel(redChecker7));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker7));

        blueChecker8 = findViewById(R.id.blueCheck8);
        redChecker8 = findViewById(R.id.redCheck8);
        redCheckersViewModel.add(new checkerViewModel(redChecker8));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker8));

        blueChecker9 = findViewById(R.id.blueCheck9);
        redChecker9 = findViewById(R.id.redCheck9);
        redCheckersViewModel.add(new checkerViewModel(redChecker9));
        blueCheckersViewModel.add(new checkerViewModel(blueChecker9));
    }

    /**
     * Helper method to make removed checkers not show up on screen.
     */
    private void setCheckerStates() {
        if(playerRed.getNrOfRemovedCheckers() > 0){
            for (int i = 1; i < playerRed.getNrOfRemovedCheckers() +1  ; i++ ){
                redCheckersViewModel.get(playerRed.getNrOfMarkersPlaced()-i).getCheckerId().setVisibility(View.INVISIBLE);
            }
        }
        if (playerBlue.getNrOfRemovedCheckers() > 0){
            for (int i = 1; i < playerBlue.getNrOfRemovedCheckers() + 1 ; i++ ){
                blueCheckersViewModel.get(playerBlue.getNrOfMarkersPlaced()-i).getCheckerId().setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Helper method to get the current checker before 9 are placed
     * @return the current checker to be placed
     */
    private ImageView getCurrentChecker()
    {
       if (rules.getTurn() == 1){//Red
           return redCheckersViewModel.get(playerRed.getNrOfMarkersPlaced()-1).getCheckerId();

       }else if (rules.getTurn() == 2){//blue
           return blueCheckersViewModel.get(playerBlue.getNrOfMarkersPlaced()-1).getCheckerId();
       }
       return null;
    }

    /**
     * Helper method to get the correct ImageView located at the selected cooridnates
     * @return the selected ImageView
     */
    private ImageView getSelectedChecker(){
        if (rules.getTurn() == 1){//Red
            for (checkerViewModel checker: redCheckersViewModel){
                if(checker.getCurrentHitbox() == playerRed.getMovePieceTo()){
                    return checker.getCheckerId();
                }
            }
        }else if (rules.getTurn() == 2){//blue
            for (checkerViewModel checker: blueCheckersViewModel){
                if(checker.getCurrentHitbox() == playerBlue.getMovePieceTo()){
                    return checker.getCheckerId();
                }
            }
        }
        return null;
    }

    /**
     * Calculates the centrum of a hitbox.
     * @return center x-value of a hitbox
     */
    public float getCentrumXOfHitbox(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            return (validPlacesInGameboard.get(placeClickedInBoard -1).centerX() - getResources().getDimension(R.dimen.checker_size)/2);
        }
        else{
            return (getResources().getDimension(R.dimen.left_boarder) + (validPlacesInGameboard.get(placeClickedInBoard -1).centerX() - getResources().getDimension(R.dimen.checker_size)/2));
        }
    }

    /**
     * Calculates the centrum of a hitbox.
     * @return center y-value of a hitbox
     */
    private float getCentrumYOfHitbox(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (validPlacesInGameboard.get(placeClickedInBoard -1).centerY() - getResources().getDimension(R.dimen.checker_size)/2);
        }else{
            return (validPlacesInGameboard.get(placeClickedInBoard -1).centerY() - getResources().getDimension(R.dimen.checker_size)/2);
        }
    }

    /**
     * Helper method to place checker at the appropriate coordinate when re-entering the app.
     */
    private void replaceCheckers(){
        for (int i= 0; i < playerBlue.getMoves().size(); i++){
            blueCheckersViewModel.get(i).setCurrentHitbox(playerBlue.getMoves().get(i));
            placeClickedInBoard = blueCheckersViewModel.get(i).getCurrentHitbox();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                AnimatorSet animationSet = new AnimatorSet();
                animationSet.play(ObjectAnimator.ofFloat(blueCheckersViewModel.get(i).getCheckerId(), "x", (float) (getCentrumXOfHitbox() )))
                        .with(ObjectAnimator.ofFloat(blueCheckersViewModel.get(i).getCheckerId(), "y", (float) (getCentrumYOfHitbox() )));
                animationSet.setDuration(2000);
                animationSet.start();
            }else{
                AnimatorSet animationSet = new AnimatorSet();
                animationSet.play(ObjectAnimator.ofFloat(blueCheckersViewModel.get(i).getCheckerId(), "x", (getCentrumXOfHitbox() )))
                        .with(ObjectAnimator.ofFloat(blueCheckersViewModel.get(i).getCheckerId(), "y", (getCentrumYOfHitbox() )) );
                animationSet.setDuration(2000);
                animationSet.start();
            }

        }
        for (int i= 0; i < playerRed.getMoves().size(); i++){
            redCheckersViewModel.get(i).setCurrentHitbox(playerRed.getMoves().get(i));
            placeClickedInBoard = redCheckersViewModel.get(i).getCurrentHitbox();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                AnimatorSet animationSet = new AnimatorSet();
                animationSet.play(ObjectAnimator.ofFloat(redCheckersViewModel.get(i).getCheckerId(), "x", getCentrumXOfHitbox()))
                        .with(ObjectAnimator.ofFloat(redCheckersViewModel.get(i).getCheckerId(), "y", getCentrumYOfHitbox()));
                animationSet.setDuration(2000);
                animationSet.start();
            }else{
                AnimatorSet animationSet = new AnimatorSet();
                animationSet.play(ObjectAnimator.ofFloat(redCheckersViewModel.get(i).getCheckerId(), "x", getCentrumXOfHitbox()))
                        .with(ObjectAnimator.ofFloat(redCheckersViewModel.get(i).getCheckerId(), "y", getCentrumYOfHitbox()));
                animationSet.setDuration(2000);
                animationSet.start();
            }
        }
        boardView.invalidate();
    }

    /**
     * Helper method to check if a player has won
     * @param color the playercolor
     * @return true if color has won, false if not
     */
    private boolean checkWin(Player color){
        if(((color.getMoves().size() < 3) && (color.getNrOfMarkersPlaced() == 9))){
            internalFile.clearData();
            return true;
        }
        else return false;
    }

    /**
     * Helper method to print out who turn it's
     */
    private void printPlayerTurn(){
        if(rules.getTurn() == playerRed.getColorID()) {
            playerTurnText.setText("Red's turn");
        }else if(rules.getTurn() == playerBlue.getColorID()){
            playerTurnText.setText("Blue's turn");
        }
    }

    /**
     * Helper method to check if a press on screen is in a valid location
     * @return true if valid, false if invalid
     */
    private boolean checkValid(){
        boolean validity = false;
        for(int i = 0; i < validPlacesInGameboard.size(); i++){
            if ((validPlacesInGameboard.get(i).left <= touchedX ) && (validPlacesInGameboard.get(i).top >= touchedY) && (validPlacesInGameboard.get(i).right >= touchedX) && (validPlacesInGameboard.get(i).bottom <= touchedY)){
                validity = true;
                placeClickedInBoard = i+1;
            } else if ((validPlacesInGameboard.get(i).left <= touchedX ) && (validPlacesInGameboard.get(i).top <= touchedY) && (validPlacesInGameboard.get(i).right >= touchedX) && (validPlacesInGameboard.get(i).bottom >= touchedY)){
                validity = true;
                placeClickedInBoard = i+1;
            } else if ((validPlacesInGameboard.get(i).left >= touchedX ) && (validPlacesInGameboard.get(i).top >= touchedY) && (validPlacesInGameboard.get(i).right <= touchedX) && (validPlacesInGameboard.get(i).bottom <= touchedY)){
                validity = true;
                placeClickedInBoard = i+1;
            }
        }
        return validity;
    }

    /**
     * Initiates all the hitboxes for the gameboard in portrait mode
     */
    private void initHitboxesPortrait(){

        //1
        Rect validplace = new Rect();
        validplace.left =  ((width/6)*3)/2 + 100 ;
        validplace.right = validplace.left + 100;
        validplace.top =((((height/4) - (((height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //2
        validplace = new Rect();
        validplace.right = width/6;
        validplace.left = validplace.right + 100;
        validplace.top =((height/4) - (((height/4) - (height/4 + height/4 +50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //nr 3
        validplace = new Rect();
        validplace.left = 25;
        validplace.top = height/4;
        validplace.right = validplace.left + 100;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);
        //4
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =((((height/4) - (((height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //5
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =((height/4) - (((height/4) - (height/4 + height/4 +50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //nr 6
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.top = height/4;
        validplace.right = validplace.left + 100;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //7
        validplace = new Rect();
        validplace.left =  width - (((width/6)*3)/2 + 200) ;
        validplace.right = validplace.left + 100;
        validplace.top =((((height/4) - (((height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //8
        validplace = new Rect();
        validplace.right = ((width) - (width/6));
        validplace.left = validplace.right -100;
        validplace.top =((height/4) - (((height/4) - (height/4 + height/4 +50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //nr 9
        validplace = new Rect();
        validplace.right = width - 25;
        validplace.left = validplace.right -100;
        validplace.top = height/4;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        // 10
        validplace = new Rect();
        validplace.left =  width - (((width/6)*3)/2 + 200) ;
        validplace.right = validplace.left + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //11
        validplace = new Rect();
        validplace.right = ((width) - (width/6));
        validplace.left = validplace.right -100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //nr 12
        validplace = new Rect();
        validplace.right = width - 25;
        validplace.left = validplace.right -100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //13
        validplace = new Rect();
        validplace.left =  width - (((width/6)*3)/2 + 200) ;
        validplace.right = validplace.left + 100;
        validplace.top =(( ((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+25;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //14
        validplace = new Rect();
        validplace.right = ((width) - (width/6));
        validplace.left = validplace.right -100;
        validplace.top =((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3));
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //15
        validplace = new Rect();
        validplace.right = width - 25;
        validplace.left = validplace.right -100;
        validplace.top = height - height/4;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //16
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =(( ((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+25;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //17
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top =((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3));
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //18
        validplace = new Rect();
        validplace.left = width/2 -50;
        validplace.right = validplace.left + 100;
        validplace.top = height - height/4;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //19
        validplace = new Rect();
        validplace.left =  ((width/6)*3)/2 + 100 ;
        validplace.right = validplace.left + 100;
        validplace.top =((((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3) ) + height/2) /2)+25;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //20
        validplace = new Rect();
        validplace.right = width/6;
        validplace.left = validplace.right + 100;
        validplace.top =((height - height/4) - (((height - height/4) - (height/4 + height/4 +50))/3));
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //21
        validplace = new Rect();
        validplace.left = 25;
        validplace.right = validplace.left + 100;
        validplace.top = height - height/4;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //22
        validplace = new Rect();
        validplace.left =  ((width/6)*3)/2 + 100 ;
        validplace.right = validplace.left + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //23
        validplace = new Rect();
        validplace.right = width/6;
        validplace.left = validplace.right + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //24
        validplace = new Rect();
        validplace.left = 25;
        validplace.right = validplace.left + 100;
        validplace.top = height/4 + height/4 +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

    }

    /**
     * Initiates all the hitboxes for the gameboard in landscape mode
     */
    private void initHitboxesLandscape(){

        //1
        Rect validplace = new Rect();
        validplace.left = width/4 -75;
        validplace.right = validplace.left + 100;
        validplace.top =(height/3) +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);


        //2
        validplace = new Rect();
        validplace.left = width/8 -25;
        validplace.right = validplace.left + 100;
        validplace.top =((height/8) - (((height/8) - (height/8 + height/8 -50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //3
        validplace = new Rect();
        validplace.left =  25 ;
        validplace.right = validplace.left + 100;
        validplace.top = 25;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //4
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top =(height/3) +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //5
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top =((height/8) - (((height/8) - (height/8 + height/8 -50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //6
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = 25;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //7
        validplace = new Rect();
        validplace.left = (int)(width*0.44);
        validplace.right = validplace.left + 100;
        validplace.top =(height/3) +50;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //8
        validplace = new Rect();
        validplace.left = (int) (width*0.54);
        validplace.right = validplace.left + 100;
        validplace.top =((height/8) - (((height/8) - (height/8 + height/8 -50))/3))+75;
        validplace.bottom = validplace.top - 100;
        validPlacesInGameboard.add(validplace);

        //9
        validplace = new Rect();
        validplace.left = (int) (width*0.65);
        validplace.right = validplace.left + 100;
        validplace.top = 25;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //10
        validplace = new Rect();
        validplace.left = (int)(width*0.44);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //11
        validplace = new Rect();
        validplace.left = (int) (width*0.54);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //12
        validplace = new Rect();
        validplace.left = (int) (width*0.65);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //13
        validplace = new Rect();
        validplace.left = (int)(width*0.44);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.57);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //14
        validplace = new Rect();
        validplace.left = (int) (width*0.54);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.71);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //15
        validplace = new Rect();
        validplace.left = (int) (width*0.65);
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.85);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //16
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.57);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //17
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.71);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //18
        validplace = new Rect();
        validplace.left = width/3 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.85);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //19
        validplace = new Rect();
        validplace.left = width/4 -75;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.57);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //20
        validplace = new Rect();
        validplace.left = width/8 -25;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.71);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //21
        validplace = new Rect();
        validplace.left =  25 ;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.85);
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //22
        validplace = new Rect();
        validplace.left = width/4 -75;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //23
        validplace = new Rect();
        validplace.left = width/8 -25;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

        //nr 24
        validplace = new Rect();
        validplace.left = 25;
        validplace.right = validplace.left + 100;
        validplace.top = (int) (height*0.4) + 50;
        validplace.bottom = validplace.top + 100;
        validPlacesInGameboard.add(validplace);

    }

}