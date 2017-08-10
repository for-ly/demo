package thread.demo;

/**
 * Created by T440P on 2017/5/9.
 */

public class Runnable1 implements Runnable{
    @Override
    public void run(){
        for(int i=0;i<=5;i++)
        {
            System.out.println("Thread-----:"+i);
        }
    }
}