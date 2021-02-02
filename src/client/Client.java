package client;

import client.window.GameWindow;
import connection.Frame;
import connection.TaggedConnection;
import game.GameChange;
import game.GameMap;
import game.GameSettings;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Client {

    private static TaggedConnection connection;
    private static GameWindow gameWindow;
    private static boolean running;



    public static void main(String[] args) throws IOException {
        connection = new TaggedConnection(new Socket(GameSettings.ip, GameSettings.port));
        GameMap map = GameMap.unserialize(connection.receive());
        int snakeID = Integer.parseInt(new String(connection.receive().getData()));
        gameWindow = new GameWindow("Snake", map, snakeID, connection);
        running = true;

        while (running) {
            try {
                Frame frame = connection.receive();
                List<GameChange> changes = GameChange.unserialize(frame);
                gameWindow.applyChanges(changes);
            } catch (IOException e) {
                running = false;
            }
        }

        close();
    }

    public static void close() throws IOException {
        running = false;
        gameWindow.setVisible(false);
        gameWindow.dispose();
        connection.close();
    }

}
