import java.util.concurrent.*;
import java.util.Timer;
import java.util.TimerTask;
import java.time.*;

class myRunnable implements Runnable{

    volatile int zombiesEliminated = 0;
    int zombieCount = 0;

    volatile boolean flag = true;
    
    @Override
    public void run(){

        if(Thread.currentThread().getName().equals("me")){

            // timer will check zombieCount every 1 second
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    accessZombieCount(0, true);
                }      
            }, 0, 1000);


            while(!zombie.stop){

            //40% chance of killing a zombie
            if(Math.random() < 0.4){
                accessZombieCount(-1, false);
            }
            //wait for 10 milliseconds
            try
            { 
                Thread.sleep(10); 
            } 
            catch (Exception e) 
            { 
                System.out.println("Thread interrupted."); 
            } 

            }

            if(zombie.stop) timer.cancel();   //stop timer and print throughput
            System.out.println((int)Math.round((zombiesEliminated/180.0)) + (" zombies/second"));
   
        }else{

            //FRIEND THREAD
            while(!zombie.stop){

            //10% chance of letting in a zombie
            if(Math.random() < 0.1){
                if(flag) accessZombieCount(1, false);    //when flag = false, don't let zombies in until zombieCount drops below n/2
            }
            //wait for 10 milliseconds
            try
            { 
                Thread.sleep(10); 
            } 
            catch (Exception e) 
            { 
                System.out.println("Thread interrupted."); 
            } 

            }
        }
    }

    synchronized void accessZombieCount(int val, boolean check){
        //check = true every one second
        if(check){
            //if zombieCount is greater than threshold, set flag = false (signal friends to keep doors shut)
            if(zombieCount >= zombie.n && flag){
                flag = false;
            }else if(zombieCount < zombie.n/2.0 && !flag){      //when zombieCount < n/2 and flag = false, set flag = true (signal to let zombies in) 
                flag = true;
            } 
        }else{
            if(val == -1){
                if(zombieCount > 0){
                    zombieCount--;
                    zombiesEliminated++;
                }
            }else{
                zombieCount++;
            }
        }
    } 
}

public class zombie{

    public static int n;
    volatile public static boolean stop = false;

    public static void main(String[] args){

        long start = System.currentTimeMillis();
        long end = start + 3*60*1000;  //running for 3 mins
        int k = Integer.parseInt(args[0]);
        n = Integer.parseInt(args[1]);

        if(k < 1 || n < 2){
            System.out.println("Please make sure k ≥ 1 and n ≥ 2.\nTry running the program again with appropriate command-line parameters.");
            System.exit(0);
        }

        myRunnable myRunnableObj = new myRunnable();

        //my thread
        Thread t = new Thread(myRunnableObj);
        t.setName("me");
        t.start();

        //k friend threads
        for(int i = 0; i < k; i++){
            t = new Thread(myRunnableObj);
            t.start();
        }

        while(System.currentTimeMillis() < end){

        }

        stop = true;
    }

}

