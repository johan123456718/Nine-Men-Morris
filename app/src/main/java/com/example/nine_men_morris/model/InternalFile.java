package com.example.nine_men_morris.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Class to save and load gamedata
 */
public class InternalFile {
    private static InternalFile internalFile;

    private InternalFile(){

    }

    public static InternalFile getInstance(){
        if(internalFile == null){
            internalFile = new InternalFile();
        }
        return internalFile;
    }

    private static final String NAME_OF_FILE = "morrisFile";

    /**
     * Save necessary data to local file
     * @param dir local file directory
     * @param playerBlueMoves
     * @param playerRedMoves
     * @param nrOfBlueMarkersPlaced
     * @param nrOfRedMarkersPlaced
     * @param turn
     * @param state
     * @param nrOfRemovedBlueCheckers
     * @param nrOfRemovedRedCheckers
     */
    public void saveData(File dir,
                         ArrayList<Integer> playerBlueMoves,
                         ArrayList<Integer> playerRedMoves,
                         int nrOfBlueMarkersPlaced,
                         int nrOfRedMarkersPlaced,
                         int turn,
                         int state,
                         int nrOfRemovedBlueCheckers,
                         int nrOfRemovedRedCheckers){
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, NAME_OF_FILE));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024*8);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            objectOutputStream.writeObject(playerBlueMoves);
            objectOutputStream.writeObject(playerRedMoves);
            objectOutputStream.writeObject(nrOfBlueMarkersPlaced);
            objectOutputStream.writeObject(nrOfRedMarkersPlaced);
            objectOutputStream.writeObject(turn);
            objectOutputStream.writeObject(state);
            objectOutputStream.writeObject(nrOfRemovedBlueCheckers);
            objectOutputStream.writeObject(nrOfRemovedRedCheckers);
            objectOutputStream.reset();
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads all necessary data from file
     * @param dir local file directory
     * @param playerBlue
     * @param playerRed
     * @param rules current game
     */
    public void loadData(File dir, Player playerBlue, Player playerRed, NineMenMorrisRules rules){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(dir, NAME_OF_FILE));
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(fileInputStream));
            playerBlue.setMoves((ArrayList<Integer>)objectInputStream.readObject());
            playerRed.setMoves((ArrayList<Integer>)objectInputStream.readObject());
            playerBlue.setNrOfMarkersPlaced((int)objectInputStream.readObject());
            playerRed.setNrOfMarkersPlaced((int)objectInputStream.readObject());
            rules.setTurn((int) objectInputStream.readObject());
            rules.setState((int) objectInputStream.readObject());
            playerBlue.setNrOfRemovedCheckers((int) objectInputStream.readObject());
            playerRed.setNrOfRemovedCheckers((int) objectInputStream.readObject());
            objectInputStream.close();
            fileInputStream.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Clears the local file to begin a new game
     */
    public void clearData(){
        try {
            new FileOutputStream(new File("/data/user/0/com.example.nine_men_morris/files", NAME_OF_FILE)).close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


}
