package Model;

import java.util.*;


/**
 * Created by William on 10/26/2015.
 */
public class Map {

    Tile p = Tile.PLAIN;
    Tile f = Tile.FOREST;
    Tile r = Tile.ROCK;
    private int  width, height;
    private MapTile[][] board;
    private ArrayList<Fighter> fighters;
    private final Tile[][] tutorialBoard = {
            {p, p, p, p, p, p, p, p, p, p, f, f},
            {p, p, p, p, p, p, p, p, p, p, f, f},
            {p, p, p, r, p, r, p, p, p, p, f, f},
            {p, p, p, p, p, p, p, p, p, p, f, f},
            {p, p, p, r, p, r, p, p, p, p, f, f},
            {p, p, p, p, p, p, p, p, p, p, f, f},
            {f, f, f, f, f, f, p, p, p, f, f, f},
            {f, f, f, f, f, f, p, p, p, f, f, f}
    };

    public Map(MapType type) {
        board = generateBoard(type.getTileArr());
        height = board.length;
        width = board[0].length;
        fighters = new ArrayList<>();
    }

    public Map(int w, int h) {
        width = w;
        height = h;
        board = generateBoard(tutorialBoard);
        fighters = new ArrayList<>();
    }

    public MapTile[][] generateBoard(Tile[][] tiles) {
        MapTile[][] board = new MapTile[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                board[i][j] = new MapTile(tiles[i][j], j, i);
            }
        }
        return board;
    }

    //Uses a BFS to find all of the valid moves for a fighter f
    public boolean[][] getValidMoves(Fighter f) {
        boolean[][] valid = new boolean[height][width];

        MapTile startingTile = getMapTile(f.getxPos(), f.getyPos());
        HashMap<MapTile, Integer> costMap = new HashMap<>();
        PriorityQueue queue = new PriorityQueue<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                MapTile a = (MapTile)o1;
                MapTile b = (MapTile)o2;
                if (costMap.get(a) != null && costMap.get(b) != null) {
                    return (costMap.get(b) - costMap.get(a));
                } else {
                    return 0;
                }
            }
        });
        for (MapTile x : getMoves(startingTile)) {
            queue.add(x);
            costMap.put(x, x.getMoveCost());
        }
        while (!queue.isEmpty()) {
            MapTile tile = (MapTile)queue.poll();
            valid[tile.getyPos()][tile.getxPos()] = true;
            int currentCost = costMap.get(tile);
            if (currentCost < f.getMov()) {
                for (MapTile x : getMoves(tile)) {
                    if (!valid[x.getyPos()][x.getxPos()]) {
                        queue.add(x);
                        int nextCost = currentCost + x.getMoveCost();
                        if (costMap.containsKey(x)) {
                            costMap.put(x, Math.min(nextCost, costMap.get(x)));
                        } else {
                            costMap.put(x, nextCost);
                        }
                    }
                }
            }
        }
        for (Fighter a : fighters) {
            int x = a.getxPos();
            int y = a.getyPos();
            valid[y][x] = false;
        }
        return valid;
    }

    //helper function for getValidMoves
    //gets the adjacent, moveable tiles of tile.
    public List<MapTile> getMoves(MapTile tile) {
        int x = tile.getxPos();
        int y = tile.getyPos();
        List<MapTile> list = new ArrayList<>();
        MapTile left = getMapTile(x-1, y);
        if (left != null && left.isMoveable()) {
            list.add(left);
        }
        MapTile right = getMapTile(x+1, y);
        if (right != null && right.isMoveable()) {
            list.add(right);
        }
        MapTile up = getMapTile(x, y-1);
        if (up != null && up.isMoveable()) {
            list.add(up);
        }
        MapTile down = getMapTile(x, y+1);
        if (down != null && down.isMoveable()) {
            list.add(down);
        }
        return list;
    }

    //uses a BFS to find the valid attacks of f
    public boolean[][] getValidAttacks(Fighter f) { //TODO implement terrain blocking
        //finds the valid squares
        boolean[][] valid = new boolean[height][width];
        Queue<MapTile> queue = new LinkedList<>();
        MapTile startingTile = getMapTile(f.getxPos(), f.getyPos());
        HashMap<MapTile, Integer> rangeMap = new HashMap<>();
        for (MapTile x : getAttacks(startingTile)) {
            queue.add(x);
            rangeMap.put(x, 1);
        }
        while (!queue.isEmpty()) {
            MapTile tile = queue.poll();
            valid[tile.getyPos()][tile.getxPos()] = true;
            int currentRange = rangeMap.get(tile);
            if (currentRange < f.getRange()) {
                for (MapTile x : getAttacks(tile)) {
                    queue.add(x);
                    rangeMap.put(x, currentRange + 1);
                }
            }
        }
        //removes positions behind rocks
        for (int i = 0; i < valid.length; i++) {
            for (int j = 0; j < valid[0].length; j++) {
                if (valid[i][j]) {
                    if (getMapTile(j, i).isBlocking()) {
                        valid = fixObstacle(valid, j, i, f);
                    }
                }
            }
        }
        //finds the enemies in range
        boolean[][] inRange = new boolean[height][width];
        for (Fighter a : fighters) {
            if (a.isEnemy()) {
                int x = a.getxPos();
                int y = a.getyPos();
                if (valid[y][x]) {
                    inRange[y][x] = true;
                }
            }
        }
        return inRange;
    }

    public List<MapTile> getAttacks(MapTile tile) {
        int x = tile.getxPos();
        int y = tile.getyPos();
        List<MapTile> list = new ArrayList<>();
        MapTile left = getMapTile(x - 1, y);
        if (left != null) {
            list.add(left);
        }
        MapTile right = getMapTile(x + 1, y);
        if (right != null) {
            list.add(right);
        }
        MapTile up = getMapTile(x, y-1);
        if (up != null) {
            list.add(up);
        }
        MapTile down = getMapTile(x, y+1);
        if (down != null) {
            list.add(down);
        }
        return list;
    }

    public boolean[][] fixObstacle(boolean[][] valid, int x, int y, Fighter f) {
        /*
        int posX = f.getxPos();
        int posY = f.getyPos();
        int height = valid.length;
        int width = valid[0].length;
        if (x - posX == 0) {
            int p = (posY - y) / Math.abs(posY - y);
            int count = 0;
            for (int i = y; i < height && i >= 0; i += p) {
                count++;
                for (int j = count * -1; j <= count; j++) {
                    if ((x + j) < width && (x + j) >= 0) {
                        valid[i][x + j] = false;
                    }
                }
            }
        } else if (y - posY == 0) {
            int p = (posX - x) / Math.abs(posX - x);
            int count = 0;
            for (int i = x; i < width && i >= 0; i+= p) {
                count++;
                for (int j = count * -1; j <= count; j++) {
                    if ((y + j) < height && (y + j) >= 0) {
                        valid[y+j][i] = false;
                    }
                }
            }
        } else if (Math.abs(y - posY) == Math.abs(x - posX)) {
            int deltY = (posY - y) / Math.abs(posY - y);
            int deltX = (posX - x) / Math.abs(posX - x);
            int i = y;
            int j = x;
            while (i < height && i >= 0 && j < width && j >= 0) {
                i += deltY;
                j += deltX;
                valid[i][j] = false;
            }
        } else if (Math.abs(x - posX) > Math.abs(y - posY)) {

        } else if (Math.abs(x - posX) < Math.abs(y - posY)) {

        }
        */
        return valid;
    }

    public MapTile getMapTile(int x, int y) {
        if (x >= width || y >= height || x < 0 || y < 0) {
            return null;
        } else {
            return board[y][x];
        }
    }

    public Fighter getFighter(int x, int y) {
        for (Fighter f : fighters) {
            if (f.getxPos() == x && f.getyPos() == y) {
                return f;
            }
        }
        return null;
    }

    //Getters
    public int getHeight() { return height;}
    public int getWidth() { return width;}

    //Adders
    public void addFighter(Fighter f) {fighters.add(f);}
}
