package connection;

/**
 * Frame class that retains a tag for a message.
 */
public class Frame{

    private final Tag tag;
    private final byte[] data;

    /**
     * Enum for the different types of messages.
     */
    public enum Tag{
        PLAYER_INPUT,

        MAP_SETUP,
        PLAYER_ID,
        GAME_UPDATE
    }

    /**
     * Constructor of Frame
     * @param tag type of message
     * @param data message
     */
    public Frame(Tag tag, byte[] data){
        this.tag = tag;
        this.data = data;
    }

    /**
     * Gets the Tag of the Frame
     * @return Tag of the Frame
     */
    public Tag getTag(){
        return this.tag;
    }

    /**
     * Gets the message of the Frame
     * @return message (data) of the message
     */
    public byte[] getData(){
        return this.data;
    }

}