package server;

import connection.Frame;
import connection.TaggedConnection;
import game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        new Thread(game).start();

        ServerSocket ss = new ServerSocket(33333);

        while (true) {
            Socket socket = ss.accept();
            TaggedConnection connection = new TaggedConnection(socket);
            int snakeID = game.newSnake(connection);
            new Thread(new ServerWorker(connection, game, snakeID)).start();
            connection.send(game.map.serialize());
            connection.send(new Frame(Frame.Tag.PLAYER_ID, ("" + snakeID).getBytes()));
            connection.send(game.snakes.get(snakeID).getDirection().serialize());
        }
    }

}
