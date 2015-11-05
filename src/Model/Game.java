package Model;

import Controller.MasterController;

import java.util.ArrayList;

/**
 * Created by William on 10/26/2015.
 */
public class Game {

    private Difficulty difficulty;
    private Mode mode;
    private Commander commander;
    private ArrayList<Fighter> allies, enemies;
    private Fighter fighter;
    private Map map;

    //Constructor
    public Game() {
        difficulty = null;
        mode = null;
        commander = null;
        allies = new ArrayList<>();
        enemies = new ArrayList<>();
        fighter = null;
        map = null;
    }

    public void startGame() {
        MasterController.getInstance().setMapScene();
        map = new Map(MapType.TUTORIAL);
        MasterController.getInstance().getMapController().constructMap(map);
    }

    //sets fighter to the next fighter on allies, and then returns fighter
    public Fighter nextFighter() {
        int idx = 0;
        Fighter f = null;
        while (f != fighter) {
            f = allies.get(idx);
            idx++;
        }
        if (idx == allies.size()) {
            idx = 0;
        }
        fighter = allies.get(idx);
        return fighter;
    }

    //sets fighter to the prev fighter on allies, and then returns fighter
    public Fighter prevFighter() {
        int idx = allies.size() - 1;
        Fighter f = null;
        while (f != fighter) {
            f = allies.get(idx);
            idx--;
        }
        if (idx == -1) {
            idx = allies.size() - 1;
        }
        fighter = allies.get(idx);
        return fighter;
    }

    //Getters
    public Difficulty getDifficulty() {return difficulty;}
    public Mode getMode() {return mode;}
    public Commander getCommander() {return commander;}
    public ArrayList<Fighter> getAllies() {return allies;}
    public ArrayList<Fighter> getEnemies() {return enemies;}
    public Fighter getFighter() {return fighter;}
    //Setters
    public void setDifficulty(Difficulty d) {difficulty = d;}
    public void setMode(Mode m) {mode = m;}
    public void setCommander(Commander c) {commander = c;}
    public void setAllies(ArrayList<Fighter> a) {allies = a;}
    public void setEnemies(ArrayList<Fighter> e) {enemies = e;}
    public void setFighter(Fighter f) {fighter = f;}
    //Adders
    public void addToAllies(Fighter f) {allies.add(f);}
    public void addToEnemies(Fighter f) {enemies.add(f);}

}
