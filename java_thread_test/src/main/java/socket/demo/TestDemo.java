package socket.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by T440P on 2017/7/3.
 */
public class TestDemo {
    public static void main(String [] args) throws IOException {
        runTest();
    }

    public static void runTest() throws IOException {
        List<ClientSimulator> clients = getClients();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        for (ClientSimulator client: clients
             ) {
            fixedThreadPool.execute(client);
        }
        fixedThreadPool.shutdown();
    }

    public static List<ClientSimulator> getClients() throws IOException {
        List<ClientSimulator> clients = new ArrayList<ClientSimulator>();
        for (int i=0;i<10;i++)
        {
            clients.add(new ClientSimulator());
        }
        return clients;
    }
}
