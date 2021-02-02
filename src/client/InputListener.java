package client;

import connection.TaggedConnection;
import util.Vector2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class InputListener implements KeyListener {

    private final TaggedConnection connection;

    private Vector2 direction;

    private int keyDown;

    public InputListener(TaggedConnection connection) throws IOException {
        this.connection = connection;
        direction = Vector2.unserialize(connection.receive());

        keyDown = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keyDown != e.getKeyCode()) {
            keyDown = e.getKeyCode();

            Vector2 newDir = getNewDirection(e);
            if (newDir != null) {
                direction = newDir;
                try {
                    connection.send(direction.serialize());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //region Class Methods

    private Vector2 getNewDirection(KeyEvent e) {
        Vector2 newDir = new Vector2();

        switch (e.getKeyCode()) {
            case 'w':
            case 'W':
                newDir.x--;
                break;
            case 's':
            case 'S':
                newDir.x++;
                break;
            case 'a':
            case 'A':
                newDir.y--;
                break;
            case 'd':
            case 'D':
                newDir.y++;
                break;
            default:
                newDir = null;
        }

        return newDir;
    }

    //endregion

}
