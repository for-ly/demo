package socket.demo;

import java.io.*;
import java.net.Socket;

/**
 * Created by T440P on 2017/6/30.
 */
public class ClientDemo {
    public static void main(String [] args) throws IOException, InterruptedException {
        run();
    }

    public static void ClientRun() throws IOException{
        System.out.println("Client 已启动：");
        Socket s = new Socket("localhost",8000);
        PrintWriter pw = new PrintWriter(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while (br != null && (line=br.readLine()) != null)
        {
            System.out.println("Client send message:"+line);
            pw.println(line);
            pw.flush();
            if (line.equals("end"))
                break;
        }

        br.close();
        pw.close();
        s.close();
    }

    public static void run () throws IOException, InterruptedException {
        while (true) {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            Socket socket = new Socket("localhost", 8000);
            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.print("请输入: \t");
            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
            out.writeUTF(str);

            String ret = input.readUTF();
            System.out.println("服务器端返回过来的是: " + ret);
            // 如接收到 "OK" 则断开连接
            if ("OK".equals(ret)) {
                System.out.println("客户端将关闭连接");
                Thread.sleep(500);
                break;
            }

            out.close();
            input.close();
        }
    }
}
