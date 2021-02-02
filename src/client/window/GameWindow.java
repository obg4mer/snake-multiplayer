package client.window;

import client.Client;
import client.InputListener;
import connection.TaggedConnection;
import game.GameChange;
import game.GameMap;
import game.GameSettings;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class GameWindow extends JFrame {

    private final GameCanvas canvas;

    public GameWindow(String title, GameMap map, int snakeID, TaggedConnection connection) throws IOException {
        super(title);

        Dimension dim = new Dimension(GameSettings.cellSize * map.getWidth(), GameSettings.cellSize * map.getHeight());
        getContentPane().setPreferredSize(dim);
        getContentPane().setMinimumSize(dim);
        getContentPane().setMaximumSize(dim);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    Client.close();
                } catch (IOException ignored) { }
            }
        });

        canvas = new GameCanvas(map, snakeID);
        add(canvas);

        addKeyListener(new InputListener(connection));

        setVisible(true);
        pack();
    }

    //region Class Methods

    public void applyChanges(List<GameChange> changes) {
        canvas.applyChanges(changes);
    }

    //endregion

}
