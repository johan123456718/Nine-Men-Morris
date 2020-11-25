package com.example.nine_men_morris.model;


import java.util.ArrayList;

public class Player {

    private int colorID;
    private ArrayList<Integer> moves;
    private int nrOfMarkersPlaced;

    public Player(int colorID){
        this.colorID = colorID;
        this.moves = new ArrayList<>();
        this.nrOfMarkersPlaced = 0;
    }

    public int getNrOfMarkersPlaced() {
        return nrOfMarkersPlaced;
    }

    public void setNrOfMarkersPlaced(int nrOfMarkersPlaced) {
        this.nrOfMarkersPlaced = nrOfMarkersPlaced;
    }

    public ArrayList<Integer> getMoves() {
        return moves;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getColorID() {
        return colorID;
    }

    public void setMoves(ArrayList<Integer> moves) {
        this.moves = moves;
    }

}
