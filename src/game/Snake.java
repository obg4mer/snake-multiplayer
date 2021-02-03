package game;

import connection.TaggedConnection;
import util.Vector2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Snake {

    private TaggedConnection connection;

    private final Lock lock;

    private static int nextID = 1;
    private final int id;

    private Vector2 direction;
    private final List<Vector2> body;

    public Snake(TaggedConnection connection, GameMap map) {
        this.connection = connection;

        lock = new ReentrantLock();

        id = nextID++;

        direction = Vector2.randomNormalized();

        body = new ArrayList<>();
        Vector2 head = map.randomEmptyPosition();
        body.add(head);
        map.setPosition(head, id);
    }

    // Returns true if the food was eaten.
    public boolean update(GameMap map, Vector2 foodPos, List<GameChange> saveChangesHere) {
        try {
            lock.lock();

            // Snake is dead.
            if (body.size() == 0) return false;

            // Move head
            Vector2 newHead = body.get(0).clone().add(direction);

            // Check borders
            int height = map.getHeight(), width = map.getWidth();
            if (newHead.x < 0) newHead.x = height - 1;
            else if (newHead.y < 0) newHead.y = width - 1;
            else if (newHead.x >= height) newHead.x = 0;
            else if (newHead.y >= width) newHead.y = 0;

            body.add(0, newHead);
            saveChangesHere.add(new GameChange(newHead, id));

            // Move tail
            if (!newHead.equals(foodPos)) {
                Vector2 oldEnd = body.remove(body.size() - 1);
                saveChangesHere.add(new GameChange(oldEnd, 0));
                return false;
            }

            return true;

        } finally {
            lock.unlock();
        }

    }

    public List<GameChange> kill() {
        List<GameChange> changes = new ArrayList<>();

        if (body.size() > 0) body.remove(0);

        while (body.size() > 0) {
            changes.add(new GameChange(body.remove(0), 0));
        }

        return changes;
    }

    //region Class Methods

    public int getID() {
        return id;
    }

    public Vector2 getDirection() {
        try {
            lock.lock();
            return direction;
        } finally {
            lock.unlock();
        }
    }

    public void setDirection(Vector2 direction) {
        try {
            lock.lock();
            if (body.size() < 2) this.direction = direction;
            else {
                Vector2 cantGoInDir = body.get(1).clone().subtract(body.get(0));
                if (!direction.equals(cantGoInDir)) this.direction = direction;
            }
        } finally {
            lock.unlock();
        }

    }

    //endregion

    //region Connection Methods

    public TaggedConnection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection = null;
    }

    //endregion

}
