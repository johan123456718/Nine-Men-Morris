package com.example.nine_men_morris.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class modeling a player
 */
public class Player implements Serializable {

    private int colorID;
    private ArrayList<Integer> moves;
    private int nrOfMarkersPlaced;
    private int movePieceTo;
    private int movePieceFrom;
    private int nrOfMarkers;
    private int nrOfRemovedCheckers;

    public Player(int colorID){
        this.colorID = colorID;
        this.moves = new ArrayList<>();
        this.nrOfMarkersPlaced = 0;
        this.movePieceTo = 0;
        this.movePieceFrom = 0;
        this.nrOfMarkers = 9;
        this.nrOfRemovedCheckers = 0;
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

    public void setMoves(ArrayList<Integer> moves) {
        this.moves.clear();
        this.moves.addAll(moves);
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

    public int getNrOfRemovedCheckers() {
        return nrOfRemovedCheckers;
    }

    public void setNrOfRemovedCheckers(int nrOfRemovedCheckers) {
        this.nrOfRemovedCheckers = nrOfRemovedCheckers;
    }
}
