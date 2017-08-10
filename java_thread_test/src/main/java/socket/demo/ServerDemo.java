package socket.demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by T440P on 2017/6/30.
 */
public class ServerDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8000);
        while (true) {
            Socket socket = ss.accept();
            run(socket);
        }
    }

    public static void ServerRun() throws IOException {
        ServerSocket ss = new ServerSocket(8000);
        System.out.println("Server 已启动：");

        Socket s = ss.accept();
        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader instream = new BufferedReader(in);

        String line = null;
        while (true)//(line = instream.readLine())!= null
        {
            line = instream.readLine();
            if (line != null) {
                System.out.println("Server receive message:" + line);
            }
        }
    }

    public static void run(Socket socket) throws IOException {
        // 读取客户端数据
        DataInputStream input = new DataInputStream(socket.getInputStream());
        String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
        // 处理客户端数据
        System.out.println("客户端发过来的内容:" + clientInputStr);

        // 向客户端回复信息
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        //System.out.print("请输入:\t");
        // 发送键盘输入的一行
        //String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
        out.writeUTF(clientInputStr + " ！！！注册成功！！！");

        while (input.readUTF().equals("ok")) {
            //System.out.println("ok");
            out.close();
            input.close();
            socket.close();
            break;
        }
    }
}
