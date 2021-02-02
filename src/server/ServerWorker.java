package server;

import connection.Frame;
import connection.TaggedConnection;
import game.Game;
import util.Vector2;

import java.io.IOException;

public class ServerWorker implements Runnable {

    private final TaggedConnection connection;

    private final Game game;

    private final int snakeID;

    private boolean running;

    public ServerWorker(TaggedConnection connection, Game game, int snakeID) {
        this.connection = connection;
        this.game = game;
        running = true;

        this.snakeID = snakeID;
    }

    @Override
    public void run() {

        while (running) {
            try {
                Frame frame = connection.receive();
                Vector2 newDir = Vector2.unserialize(frame);
                game.snakes.get(snakeID).setDirection(newDir);
            } catch (IOException e) {
                try {
                    close();
                } catch (IOException ignored) { }
            }
        }
    }

    private void close() throws IOException {
        running = false;
        connection.close();
    }

}
