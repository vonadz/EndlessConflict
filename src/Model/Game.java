package Model;

import Controller.MapController;
import Controller.MasterController;

import java.util.ArrayList;

/**
 * Created by William on 10/26/2015.
 */
public class Game {

    private Difficulty difficulty;
    private Mode mode;
    private Commander commander;
    private ArrayList<Unit> army;

    //Constructor
    public Game() {
        difficulty = null;
        mode = null;
        commander = null;
        army = null;
    }

    public void startGame() {
        MasterController.getInstance().setMapScene();
        MasterController.getInstance().getMapController().generateMap(new Map(20, 10));

    }

    //Getters
    public Difficulty getDifficulty() {return difficulty;}
    public Mode getMode() {return mode;}
    public Commander getCommander() {return commander;}
    public ArrayList<Unit> getArmy() {return army;}
    //Setters
    public void setDifficulty(Difficulty d) {difficulty = d;}
    public void setMode(Mode m) {mode = m;}
    public void setCommander(Commander c) {commander = c;}
    public void setArmy(ArrayList<Unit> a) {army = a;}
    //Adders
    public void addToArmy(Unit u) {army.add(u);}

}
