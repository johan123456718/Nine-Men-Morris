package com.example.nine_men_morris;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.nine_men_morris.model.NineMenMorrisRules;
import com.example.nine_men_morris.model.Player;

import java.util.ArrayList;

public class GameView extends View {

    NineMenMorrisRules rules;
    Bitmap bg, blueChecker, redChecker;
    Rect rect;
    ArrayList<Rect> validPlaces;

    int state;

    Player player1, player2;

    int height, width, placeInBoard, moveTo, moveFrom;

    float touchedX, touchedY;

    public GameView(Context context) {
        super(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        player1 = new Player(2); //RED
        player2 = new Player(1); //Blue

        state = 0;

        moveTo = 0;
        moveFrom = 0;

        validPlaces = new ArrayList<>();
        initHitboxes();

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



        if((player1.getMoves().size() > 0) && (player2.getMoves().size() >0)){
            Log.d("draw", "onDraw: player1: " + player1.getMoves().get(player1.getMoves().size()-1));
            Log.d("draw", "onDraw: player2: " + player2.getMoves().get(player2.getMoves().size()-1));
        }

        for(int i= 0; i < player1.getMoves().size(); i++){
            paint.setColor(Color.RED);
            canvas.drawRect(validPlaces.get(player1.getMoves().get(i)-1) , paint);

        }
        for(int i= 0; i< player2.getMoves().size(); i++){
            paint.setColor(Color.BLUE);
            canvas.drawRect(validPlaces.get(player2.getMoves().get(i)-1) , paint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchedX = event.getX();
        touchedY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            if (state != 1){
                if ((player1.getNrOfMarkersPlaced() < 9) || (player2.getNrOfMarkersPlaced() < 9)) {
                    if (rules.getTurn() == 1) {
                        if (checkValid() && rules.legalMove(placeInBoard, moveTo, 1)) {
                            player2.getMoves().add(placeInBoard);
                            player2.setNrOfMarkersPlaced(player2.getNrOfMarkersPlaced() + 1);
                            invalidate();
                            if(rules.remove(placeInBoard)){
                                state = 1;
                            }
                        }
                    } else {
                        if (checkValid() && rules.legalMove(placeInBoard, moveTo, 2)) {
                            player1.getMoves().add(placeInBoard);
                            player1.setNrOfMarkersPlaced(player1.getNrOfMarkersPlaced() + 1);
                            invalidate();
                            if(rules.remove(placeInBoard)){
                                state = 1;
                            }
                        }
                    }

                } else {
                    if (rules.getTurn() == 1) {
                        if (moveTo != 0) {
                            if (checkValid() && rules.legalMove(placeInBoard, moveTo, 1)) {
                                //Log.d("TAG", "onTouchEvent: movefrom: " + moveFrom);
                                for (int i = 0; i < player2.getMoves().size(); i++) {
                                    int tmp = player2.getMoves().get(i);
                                    if (moveFrom == tmp) {
                                        //Log.d("TAG", "onTouchEvent: jag kom in blå" + " movefrom: " + moveFrom);
                                        player2.getMoves().remove(i);
                                        rules.remove(moveFrom, 4);
                                    }
                                }
                                player2.getMoves().add(placeInBoard);
                              //  Log.d("TAG", "onTouchEventPart2Blue: " + placeInBoard);
                                moveTo = 0;
                                moveFrom = 0;
                                invalidate();
                                if(rules.remove(placeInBoard)){
                                    state = 1;
                                }
                            } else {
                                moveTo = 0;
                                moveFrom = 0;
                            }
                        } else {
                           // Log.d("tag", " test board: " + rules.board(placeInBoard));
                            if (checkValid() && (rules.board(placeInBoard) == 4)) {
                                moveTo = placeInBoard;
                                moveFrom = placeInBoard;
                                //Log.d("tag", "onTouchEvent: jag ssätterr moves " + moveFrom);
                            } else {
                                // Log.d("tag", "onTouchEvent: FAULTY MOVE MISTER");

                            }
                        }

                    } else if (state != 0) {
                      //  Log.d("tag", "onTouchEvent: röd spelare");
                        if (moveTo != 0) {
                            if (checkValid() && rules.legalMove(placeInBoard, moveTo, 2)) {
                             //   Log.d("TAG", "onTouchEvent: movefrom: " + moveFrom);
                                for (int i = 0; i < player1.getMoves().size(); i++) {
                                    int tmp = player1.getMoves().get(i);
                                    if (moveFrom == tmp) {
                                    //    Log.d("TAG", "onTouchEvent: jag kom in röd" + player1.getMoves().size() + " movefrom: " + moveFrom);
                                        player1.getMoves().remove(i);
                                        rules.remove(moveFrom, 5);
                                     //   Log.d("TAG", "onTouchEvent: jag kom in röd efter" + player1.getMoves().size());
                                    }
                                }
                              //  Log.d("MoveTo", "Move to before: " + moveTo);
                                player1.getMoves().add(placeInBoard);
                             //   Log.d("MoveTo", "Move to After: " + player1.getMoves().get(player1.getMoves().size() - 1));
                                // Log.d("TAG", "onTouchEventPart2Red: " + placeInBoard);
                                moveTo = 0;
                                moveFrom = 0;
                                invalidate();
                                if(rules.remove(placeInBoard)){
                                    state = 1;
                                }
                            } else {
                                moveTo = 0;
                                moveFrom = 0;
                            }
                        } else {
                          //  Log.d("TAG", "onTouchEvent: " + placeInBoard + " board: " + rules.board(placeInBoard) + " moveto: " + moveTo);
                            if (checkValid() && (rules.board(placeInBoard) == 5)) {
                                moveTo = placeInBoard;
                                moveFrom = placeInBoard;
                          //      Log.d("TAG", "onTouchEvent: jag ssätterr moves " + moveFrom);
                            } else {

                            }
                        }
                    }
                }
            }else{
                if(rules.remove(placeInBoard) && (rules.getTurn() == 2)){
                  //  Log.d("TAG", "hejhej");
                   // Log.d("TAG", "placeInBoard: " + placeInBoard);
                    rules.setTurn(1);
                    //Log.d("TAG", "Turn: " + rules.getTurn());
                    if(checkValid() && (rules.board(placeInBoard) == 5)){ // händer aldrig
                      //  Log.d("TAG", "onTouchEvent: Jag kan ta bort någotn");
                        rules.setTurn(2);
                        for(int i = 0; i < player1.getMoves().size(); i++) {
                            int tmp = player1.getMoves().get(i);
                        //    Log.d("TAG", "MoveFrom: " + placeInBoard);
                          //  Log.d("TAG", "TMP: " + tmp);
                            if(placeInBoard == tmp) {
                            //    Log.d("TAG", "onTouchEvent: Jag kan ta bort någotn part 2");
                                player1.getMoves().remove(i);
                                rules.remove(placeInBoard, 5);
                                state = 0;
                                rules.setTurn(2);
                                invalidate();
                            }
                        }
                    }
                } if(rules.remove(placeInBoard) && (rules.getTurn() == 1)){
                    //Log.d("TAG", "hejdå");
                    //Log.d("TAG", "placeInBoard2: " + rules.board(placeInBoard));
                    rules.setTurn(2);
                    //Log.d("TAG", "Turn: " + rules.getTurn());
                    if(checkValid() && (rules.board(placeInBoard) == 4)){ // händer aldrig
                        //Log.d("TAG", "onTouchEvent: Jag kan ta bort någotn2");
                        for(int i = 0; i < player2.getMoves().size(); i++) {
                            int tmp = player2.getMoves().get(i);
                            Log.d("TAG", "MoveFrom: " + placeInBoard);
                            Log.d("TAG", "TMP: " + tmp);
                            if(placeInBoard == tmp) {
                                Log.d("TAG", "onTouchEvent: Jag kan ta bort någotn part 2");
                                player2.getMoves().remove(i);
                                rules.remove(placeInBoard, 4);
                                state = 0;
                                rules.setTurn(1);
                                invalidate();
                            }
                        }
                    }
                }
            }


                return true; // event consumed (including ACTION_DOWN)
            default:
                return false;
        }
    }


    private boolean checkValid(){
        boolean validity = false;
      //  Log.d("tag", "checkValid: " + placeInBoard);
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
       // Log.d("tag", "checkValidUT: " + placeInBoard);
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
