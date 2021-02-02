package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable{
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final ReentrantLock receiveLock;
    private final ReentrantLock sendLock;

    /**
     * Constructor of TaggedConnection
     * @param s socket of the connection
     * @throws IOException if an I/O error occurs
     */
    public TaggedConnection(Socket s) throws IOException{
        this.socket = s;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.receiveLock = new ReentrantLock();
        this.sendLock = new ReentrantLock();
    }

    /**
     * Sends a frame through the socket
     * @param f frame to send
     * @throws IOException if an I/O error occurs
     */
    public void send(Frame f) throws IOException{
        this.sendLock.lock();
        try {
            out.writeUTF(f.getTag().name());
            byte[] data = f.getData();
            out.writeInt(data.length);
            out.write(data);
            this.out.flush();
        }finally {
            this.sendLock.unlock();
        }
    }

    /**
     * Receives a frame through the socket
     * @return the received frame
     * @throws IOException if an I/O error occurs
     */
    public Frame receive() throws IOException{
        this.receiveLock.lock();
        try{
            Frame.Tag tag = Frame.Tag.valueOf(in.readUTF());
            byte[] data = new byte[in.readInt()];
            in.readFully(data);
            return new Frame(tag, data);
        }finally {
            this.receiveLock.unlock();
        }
    }

    /**
     * Closes the connection.
     */
    public void close() throws IOException{
        this.sendLock.lock();
        try {
            this.in.close();
            this.out.close();
            socket.close();
        }finally {
            this.sendLock.unlock();
        }
    }

}
