package thread.demo;

/**
 * Created by T440P on 2017/5/9.
 */
public class Thread_demo {
    public static void main(String arg[]){
        Runnable1 r = new Runnable1();
        Thread t = new Thread(r);
        Runnable1 r1 = new Runnable1();
        Thread t1 = new Thread(r1);
        t.start();
        t1.start();

        for(int i=0;i<=5;i++)
        {
            System.out.println("main:"+i);
        }

    }
}
