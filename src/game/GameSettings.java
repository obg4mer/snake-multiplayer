package game;

import java.awt.*;

public class GameSettings {

    //region Game

    public static final String ip = "localhost";
    public static final int port = 33333;

    public static final int boardHeight = 15;
    public static final int boardWidth = 15;
    private static int millisPerFrame;

    //region Methods

    public static void setFramerate(int framesPerSecond) {
        millisPerFrame = Math.round(1000/(float)framesPerSecond);
    }

    public static int getMillisPerFrame() {
        return millisPerFrame;
    }

    //endregion

    //endregion

    //region Graphics

    public static final int cellSize = 40;
    public static final boolean enableStroke = true;

    //region Colors

    public static final Color colorBackground = new Color(49, 51, 54);
    public static final Color colorSelf = new Color(63, 132, 229);
    public static final Color colorSnakes = new Color(178, 13, 48);
    public static final Color colorFood = new Color(63, 120, 76);

    //endregion

    //endregion

}
