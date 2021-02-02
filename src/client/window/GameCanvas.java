package client.window;

import game.GameChange;
import game.GameMap;
import game.GameSettings;
import util.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameCanvas extends JPanel {

    private final GameMap map;
    private final int snakeID;

    public GameCanvas(GameMap map, int snakeID) {
        this.map = map;
        this.snakeID = snakeID;

        setBackground(GameSettings.colorBackground);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        int height = map.getHeight(), width = map.getWidth();
        int[][] positions = map.getPositions();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int val = positions[i][j];
                Color color;

                if (val == snakeID) color = GameSettings.colorSelf;
                else if (val == 0) color = GameSettings.colorBackground;
                else if (val == -1) color = GameSettings.colorFood;
                else color = GameSettings.colorSnakes;

                paintSquare(g2d, i, j, color);
            }
        }

    }

    private void paintChanges(List<GameChange> changes) {
        Graphics2D g2d = (Graphics2D) getGraphics();

        for (GameChange change : changes) {
            Vector2 pos = change.position;
            int val = change.value;
            Color color;

            if (val == snakeID) color = GameSettings.colorSelf;
            else if (val == 0) color = GameSettings.colorBackground;
            else if (val == -1) color = GameSettings.colorFood;
            else color = GameSettings.colorSnakes;

            paintSquare(g2d, pos, color);
        }
    }

    //region Class Methods

    private void paintSquare(Graphics2D g2d, Vector2 pos, Color color) {
        paintSquare(g2d, pos.x, pos.y, color);
    }

    private void paintSquare(Graphics2D g2d, int x, int y, Color color) {
        g2d.setColor(color);
        g2d.fillRect(GameSettings.cellSize * y,GameSettings.cellSize * x, GameSettings.cellSize, GameSettings.cellSize);
        if (GameSettings.enableStroke) {
            g2d.setColor(Color.BLACK);
            g2d.drawRect(GameSettings.cellSize * y,GameSettings.cellSize * x, GameSettings.cellSize, GameSettings.cellSize);
        }
    }

    public void applyChanges(List<GameChange> changes) {
        map.applyChanges(changes);
        paintChanges(changes);
    }

    //endregion

}
