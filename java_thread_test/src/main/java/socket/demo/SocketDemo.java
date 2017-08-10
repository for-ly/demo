package socket.demo;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by T440P on 2017/7/3.
 */
public class SocketDemo {
    public static Socket socket;

    static {
        try {
            socket = new Socket("localhost", 8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Socket getSocketInstance() throws IOException {
        return socket;
    }

    public void close() throws IOException {
        socket.close();
    }
}
