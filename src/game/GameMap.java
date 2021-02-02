package game;

import connection.Frame;
import util.Vector2;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameMap {

    private final int[][] map;

    public GameMap(int height, int width) {
        map = new int[height][width];
    }

    //region Class Methods

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }

    public int[][] getPositions() {
        return map;
    }

    public void setPosition(Vector2 position, int value) {
        map[position.x][position.y] = value;
    }

    public int getPosition(Vector2 position) {
        return map[position.x][position.y];
    }

    public void applyChange(GameChange change) {
        setPosition(change.position, change.value);
    }

    public void applyChanges(List<GameChange> changes) {
        changes.forEach(this::applyChange);
    }

    public void show() {
        System.out.println("\n\n\n\n\n\n\n\n");
        for (int[] row : map) {
            for (int pos : row) {
                System.out.print(pos + " ");
            }
            System.out.println();
        }
    }

    public Vector2 randomEmptyPosition() {
        int emptySpaces = (int) Arrays.stream(map).flatMapToInt(Arrays::stream).filter(val -> val == 0).count();
        int count = new Random().nextInt(emptySpaces);

        int height = getHeight(), width = getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i][j] != 0) continue;
                if (count == 0) return new Vector2(i, j);
                else count--;
            }
        }

        return null;
    }

    //endregion

    //region Serialization

    public Frame serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(map.length).append(";").append(map[0].length).append(";");

        for (int[] row : map) {
            for (int pos : row) {
                sb.append(pos).append(";");
            }
        }

        return new Frame(Frame.Tag.MAP_SETUP, sb.toString().getBytes());
    }

    public static GameMap unserialize(Frame frame) {
        String[] data = new String(frame.getData()).split(";");

        int height = Integer.parseInt(data[0]), width = Integer.parseInt(data[1]);
        GameMap gameMap = new GameMap(height, width);

        int count = 2;

        for ( int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gameMap.map[i][j] = Integer.parseInt(data[count++]);
            }
        }

        return gameMap;
    }

    //endregion

}
