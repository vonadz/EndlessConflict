package Model;

import Controller.BattleController;
import Controller.MasterController;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by William on 10/26/2015.
 */
public class Game {

    /**Difficulty chosen by the user**/
    private Difficulty difficulty;

    /**Mode chosen by the user**/
    private Mode mode;

    /**Commander chosen by the user**/
    private Commander commander;

    /**Fighters in the user's army**/
    private ArrayList<Fighter> army;

    /**Fighters residing in the barracks**/
    private ArrayList<Fighter> barracks;

    /**The current map being played on**/
    private Map map;

    /**the AI object used by Game**/
    private AI comp;

    /**the BattleController in charge of the map screen**/
    private BattleController battleController;

    /**
     * Constructor for Game
     */
    public Game() {
        difficulty = null;
        mode = null;
        commander = null;
        map = null;
        comp = null;
    }

    /**
     * Called when the user makes it past the config screens
     * Creates the Tutorial map and displays it on the screen
     */
    public void startGame() {
        MasterController.getInstance().setBattleScene();
        map = new Map(MapType.TUTORIAL);
        int startX = 1;
        int startY = 2;
        int obj1X = 8;
        int obj1Y = 3;
        int obj2X = 14;
        int obj2Y = 3;
        while (!map.hasPathBetween(startX, startY, obj1X, obj1Y) || !map.hasPathBetween(startX, startY, obj2X, obj2Y)) {
            map = new Map(MapType.TUTORIAL);
        }
        comp = new AI(this, map);
        battleController = MasterController.getInstance().getBattleController();
        System.out.println("battleController: " + battleController);
        battleController.constructMap(map);
        army = map.getAllies();
        barracks = new ArrayList<>();
        barracks.add(new Fighter(Hero.LIZARDKING));
    }

    /**
     * Called when a player's turn ends.
     * Calls the AI to do it's turn.
     */
    public void endTurn() {
        ArrayList<Fighter> allies = map.getAllies();
        for (Fighter f : allies) {
            f.setHasAttacked(false);
            f.setHasMoved(false);
        }
        comp.doTurn();
        MasterController.getInstance().getBattleController().startTurn();
    }

    /**
     * Called when one fighter attacks another fighter.
     * Damage = (att * 2 - def) + random(-5,5)
     * @param attacker Fighter attacking
     * @param defender Fighter defending
     * @return String of attack summary to put on terminal
     */
    public String attackFighter(Fighter attacker, Fighter defender) {
        Random r = new Random();
        int attack = attacker.getAtt();
        int defense = defender.getDef();
        int damage = (attack * 2 - defense) + r.nextInt(10) - 5;
        if (damage < 0) { damage = 0;}
        defender.setHp(defender.getHp() - damage);
        String s = (attacker.getName() + " dealt " + damage + " damage to " + defender.getName());
        if (defender.getHp() <= 0) {
            s += (" and killed " + defender.getName() + "!");
            killedFighter(defender);
        } else {
            s += (". (" + defender.getHp() + "/" + defender.getMaxHP() + ") health remaining");
        }
        return s;
    }

    /**
     * Called if a fighter dies.
     * @param f Fighter dying
     */
    public void killedFighter(Fighter f) {
        battleController.removeFighter(f);
        if (!f.isEnemy() && mode == Mode.HARDCORE) {
            army.remove(f);
        }
        if (map.getEnemies().size() == 0) {
            MasterController.getInstance().setHeadquartersScene();
        }
    }

    /**
     * gets the next fighter in army after f
     * @param fighter The current Fighter
     * @return Fighter the next Fighter
     */
    public Fighter nextFighter(Fighter fighter) {
        ArrayList<Fighter> fighters = new ArrayList<>();
        fighters.addAll(army);
        fighters.addAll(barracks);
        int idx = 0;
        Fighter f = null;
        while (f != fighter) {
            f = fighters.get(idx);
            idx++;
        }
        if (idx == fighters.size()) {
            idx = 0;
        }
        fighter = fighters.get(idx);
        return fighter;
    }

    /**
     * gets the previous fighter in army before fighter
     * @param fighter The current Fighter
     * @return Fighter the previous fighter
     */
    public Fighter prevFighter(Fighter fighter) {
        ArrayList<Fighter> fighters = new ArrayList<>();
        fighters.addAll(army);
        fighters.addAll(barracks);
        int idx = fighters.size() - 1;
        Fighter f = null;
        while (f != fighter) {
            f = fighters.get(idx);
            idx--;
        }
        if (idx == -1) {
            idx = fighters.size() - 1;
        }
        fighter = fighters.get(idx);
        return fighter;
    }

    /**Getters**/
    public Difficulty getDifficulty() {
        return difficulty;
    }
    public Mode getMode() {
        return mode;
    }
    public Map getMap() {
        return map;
    }
    public Commander getCommander() {
        return commander;
    }
    public ArrayList<Fighter> getArmy() {
        return army;
    }
    public ArrayList<Fighter> getBarracks() {
        return barracks;
    }

    /**Setters**/
    public void setDifficulty(Difficulty d) {
        difficulty = d;
    }
    public void setMode(Mode m) {
        mode = m;
    }
    public void setCommander(Commander c) {
        commander = c;
    }

    /**Adders**/
    public void addToArmy(Fighter f) {
        army.add(f);
    }
    public void addToBarracks(Fighter f) {
        barracks.add(f);
    }

    /**Removers**/
    public void removeFromArmy(Fighter f) {
        army.remove(f);
    }
    public void removeFromBarracks(Fighter f) {
        barracks.remove(f);
    }
}
