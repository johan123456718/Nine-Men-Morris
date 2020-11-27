package com.example.nine_men_morris.model;


import java.util.ArrayList;

public class Player {

    private int colorID;
    private ArrayList<Integer> moves;
    private int nrOfMarkersPlaced;
    private int movePieceTo;
    private int movePieceFrom;
    private int nrOfMarkers;

    public Player(int colorID){
        this.colorID = colorID;
        this.moves = new ArrayList<>();
        this.nrOfMarkersPlaced = 0;
        this.movePieceTo = 0;
        this.movePieceFrom = 0;
        this.nrOfMarkers = 9;
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

    public int getColorID() {
        return colorID;
    }

    public int getMovePieceFrom() {
        return movePieceFrom;
    }

    public int getMovePieceTo() {
        return movePieceTo;
    }

    public void setMovePieceFrom(int movePieceFrom) {
        this.movePieceFrom = movePieceFrom;
    }

    public void setMovePieceTo(int movePieceTo) {
        this.movePieceTo = movePieceTo;
    }

    public int getNrOfMarkers() {
        return nrOfMarkers;
    }

    public void setNrOfMarkers(int nrOfMarkers) {
        this.nrOfMarkers = nrOfMarkers;
    }
}
