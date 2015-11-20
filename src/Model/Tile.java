package Model;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by William on 10/26/2015.
 */
public enum Tile {
    /**Types of Tiles in Tutorial Map**/
    PLAIN(true, false, 1, "Plains"), FOREST(true, false, 2, "Forest"), ROCK(false, true, 1, "Rock");

    /**Name of this tile**/
    private String name;

    /**Map of all imagePaths**/
    private Map<String, String> imagePath;

    /**Attributes of this Tile**/
    private boolean moveable, blocking;

    /**Cost to move across this Tile**/
    private int moveCost;

    /**
     * Constructor for Tile
     * @param m boolean isMoveable
     * @param b boolean isBlocking
     * @param i int moveCost
     * @param n String name
     */
    Tile(boolean m, boolean b, int i, String n) {
        name = n;
        moveable = m;
        blocking = b;
        moveCost = i;
        imagePath = new HashMap<>();
        imagePath.put("PLAIN", "/View/Graphics/Tile/plainTile.png");
        imagePath.put("FOREST", "/View/Graphics/Tile/forestTile.png");
        imagePath.put("ROCK", "/View/Graphics/Tile/rockTile.png");
    }

    /**Getters**/
    public String imagePath() {return imagePath.get(this.name());}
    public String getName() {return name;}
    public boolean isMoveable() {return moveable;}
    public boolean isBlocking() {return blocking;}
    public int getMoveCost() {return moveCost;}
}
