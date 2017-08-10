package socket.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by T440P on 2017/7/3.
 */
public class ClientSimulator implements Runnable {

    @Override
    public void run() {
        try {
            Socket socket = new SocketDemo().getSocketInstance();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Client-" + Thread.currentThread().getName());
            while (true) {
                String severResponse = input.readUTF();
                if (severResponse != null && !severResponse.equals("")) {
                    System.out.println(severResponse);
                    out.writeUTF("ok");
                    input.close();
                    out.close();
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
