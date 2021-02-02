package game;

import connection.Frame;
import util.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameChange {

    public final Vector2 position;
    public final int value;

    public GameChange(Vector2 position, int value) {
        this.position = position;
        this.value = value;
    }

    //region Serialization

    public static Frame serialize(List<GameChange> changes) {
        StringBuilder sb = new StringBuilder();
        sb.append(changes.size()).append(";;");
        changes.forEach(c -> sb.append(c.serializeToString()).append(";;"));

        return new Frame(Frame.Tag.GAME_UPDATE, sb.toString().getBytes());
    }

    public static List<GameChange> unserialize(Frame frame) {
        String[] split = new String(frame.getData()).split(";;");

        List<GameChange> changes = new ArrayList<>();
        for (int i = 1; i < split.length; i++) {
            changes.add(GameChange.unserializeFromString(split[i]));
        }

        return changes;
    }

    public String serializeToString() {
        StringBuilder sb = new StringBuilder();

        sb.append(position.x).append(";");
        sb.append(position.y).append(";");
        sb.append(value);

        return sb.toString();
    }

    public static GameChange unserializeFromString(String string) {
        String[] data = string.split(";");

        //System.out.println(string);

        Vector2 pos = new Vector2(Integer.parseInt(data[0]),Integer.parseInt(data[1]));
        int value = Integer.parseInt(data[2]);

        return new GameChange(pos, value);
    }

    //endregion

}
