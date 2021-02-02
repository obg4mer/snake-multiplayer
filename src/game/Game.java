package game;

import connection.Frame;
import connection.TaggedConnection;
import util.Vector2;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Game implements Runnable {

    public final GameMap map;
    public final Map<Integer, Snake> snakes;
    public Vector2 food;
    public final List<GameChange> changes;


    public Game() {
        map = new GameMap(GameSettings.boardHeight, GameSettings.boardWidth);
        snakes = new HashMap<>();
        changes = new ArrayList<>();

        GameSettings.setFramerate(2);
    }

    //region Game Methods

    public void run() {

        long t1 = System.nanoTime(), t2;
        long timeShift = 0;

        newFood();

        try {

            while (true) {
                Thread.sleep(GameSettings.getMillisPerFrame() + timeShift);

                update();
                sendChanges();

                t2 = System.nanoTime();
                timeShift = GameSettings.getMillisPerFrame() - (t2 - t1)/1000000;
                t1 = t2;
            }

        } catch (InterruptedException ignored) { }

    }



    private void update() {
        moveSnakes();

        checkOverlaps();

        map.applyChanges(changes);

        if (food == null) newFood();
    }

    private void moveSnakes() {
        for (Snake s : snakes.values()) {

            List<GameChange> snakeChanges = new ArrayList<>();
            boolean foodWasEaten = s.update(map, food, snakeChanges);

            if (foodWasEaten) food = null;

            changes.addAll(snakeChanges);
        }
    }

    private void checkOverlaps() {
        Map<Vector2, List<GameChange>> changesMap = new HashMap<>();

        for (GameChange c : changes) {
            if (!changesMap.containsKey(c.position)) changesMap.put(c.position, new ArrayList<>());
            changesMap.get(c.position).add(c);
        }

        // Kill snakes that collide.
        changesMap.forEach((k,v) -> {

            List<GameChange> heads = v.stream().filter(c -> c.value > 0).collect(Collectors.toList());
            if (heads.size() == 1) {
                GameChange c = heads.get(0);
                if (map.getPosition(c.position) > 0) {
                    boolean wasLeaving = changes.stream().anyMatch(x -> x.position.equals(c.position) && x.value <= 0);
                    if (!wasLeaving) {
                        killSnake(c.value);
                        v.remove(c);
                    }
                }
            } else if (heads.size() > 1){
                heads.forEach(c -> {
                    killSnake(c.value);
                    v.remove(c);
                });
            }
        });

        // Remove empty spaces that overlap with a snake's head.
        changesMap.forEach((k,v) -> {
            boolean hasHead = v.stream().anyMatch(c -> c.value > 0);
            if (hasHead) v.forEach(c -> { if (c.value == 0) changes.remove(c); });
        });
    }

    private void killSnake(int snakeID) {
        snakes.get(snakeID).closeConnection();

        changes.removeIf(c -> c.value == snakeID);
        changes.addAll(snakes.get(snakeID).kill());

        snakes.remove(snakeID);
    }

    private void newFood() {
        food = map.randomEmptyPosition();
        GameChange change = new GameChange(food, -1);

        map.applyChange(change);
        changes.add(change);
    }

    //endregion

    //region Class Methods

    public int newSnake(TaggedConnection connection) {
        Snake s = new Snake(connection, map);
        int id = s.getID();
        snakes.put(id, s);

        return id;
    }

    private void sendChanges() {
        if (changes.size() == 0) return;

        Frame frame = GameChange.serialize(changes);

        for (Snake s : snakes.values()) {
            try {
                TaggedConnection connection = s.getConnection();
                if (connection != null) connection.send(frame);
            } catch (IOException e) {
                s.closeConnection();
            }
        }

        changes.clear();
    }

    //endregion

}
