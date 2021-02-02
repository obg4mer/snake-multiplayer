package util;

import connection.Frame;

import java.util.Objects;
import java.util.Random;

public class Vector2 {

    public int x, y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() { this(0,0); }

    //region Class Methods

    public Vector2 add(Vector2 vec) {
        this.x += vec.x;
        this.y += vec.y;

        return this;
    }

    public Vector2 subtract(Vector2 vec) {
        this.x -= vec.x;
        this.y -= vec.y;

        return this;
    }

    public Vector2 invert() {
        this.x *= -1;
        this.y *= -1;

        return this;
    }

    public static Vector2 randomNormalized() {
        int x = 0, y = 0, dir = new Random().nextInt(4);

        switch (dir) {
            case 0: x = 1; break;
            case 1: x = -1; break;
            case 2: y = 1; break;
            case 3: y = -1; break;
        }

        return new Vector2(x, y);
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    //endregion

    //region Serialization

    public Frame serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(x).append(";");
        sb.append(y).append(";");

        return new Frame(Frame.Tag.PLAYER_INPUT, sb.toString().getBytes());
    }

    public static Vector2 unserialize(Frame frame) {
        String[] data = new String(frame.getData()).split(";");

        return new Vector2(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }

    //endregion

}
