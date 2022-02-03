import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.List;
import java.util.ArrayList;

public class Simulation {

    public static boolean stopSimulation = false;
    public static boolean checkSimulation(){
       return stopSimulation;
    }
    
    static void start(int t, List<Actor> tasks){

        long start = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(t);

        while(checkSimulation() == false){
            for(int i = 0; i < tasks.size(); i++){
                try{
                    if(i == tasks.size()-1){
                        Future<Boolean> res1 = pool.submit(tasks.get(i));
                        try{
                            if(res1.get() == false){
                                System.out.println((System.currentTimeMillis() - start) + " ms ");
                                pool.shutdownNow(); 
                                break;
                            }                      
                        }catch(Exception e){
    
                        }
                    }else{
                        pool.submit(tasks.get(i));
                    }
                }catch(RejectedExecutionException e){
                    return;
                } 
            }
        }

        pool.shutdownNow();
        return;
    }


    public static void main(String[] args) {

        int numberOfThreads = Integer.parseInt(args[0]);

        //INPUT
        Actor input = Factory.createActor("input");
        Channel inputOut = Factory.createChannel();
        input.connectOut(inputOut, 0);

        //COMPARISON
        Actor compare = Factory.createActor("<");
        Channel compareOut = Factory.createChannel();
        compare.connectIn(inputOut, 0);     //set new value
        compare.connectOut(compareOut, 0);

        //FORK1
        Actor fork1 = Factory.createActor("fork");
        Channel fork1Out1 = Factory.createChannel();
        Channel fork1Out2 = Factory.createChannel();
        Channel fork1Out3 = Factory.createChannel();
        Channel fork1Out4 = Factory.createChannel(); 
        Channel fork1Out5 = Factory.createChannel();
        fork1.connectIn(compareOut, 0);
        fork1.connectOut(fork1Out1, 0);  //goes to merge1 boolean input line 
        fork1.connectOut(fork1Out2, 1);  //goes to merge2 boolean input line 
        fork1.connectOut(fork1Out3, 2);  //goes to merge3 boolean input line 
        fork1.connectOut(fork1Out4, 3);  //goes to switch control
        fork1.connectOut(fork1Out5, 4);  //goes to switch data

        //SWITCH
        Actor swi = Factory.createActor("switch");
        swi.connectIn(fork1Out4, 0);  //boolean input
        swi.connectIn(fork1Out5, 1);  //data
        Channel swiOut1 = Factory.createChannel();
        Channel swiOut2 = Factory.createChannel();
        swi.connectOut(swiOut1, 0);  //true line (garbage collector)
        swi.connectOut(swiOut2, 1);  //false line (goes to fork2)

        //GARBAGECOLLECTOR
        Actor garbageCollector = Factory.createActor("garbage");
        garbageCollector.connectIn(swiOut1, 0);

        //FORK2
        Actor fork2 = Factory.createActor("fork");
        Channel fork2Out1 = Factory.createChannel();
        Channel fork2Out2 = Factory.createChannel();
        Channel fork2Out3 = Factory.createChannel();
        fork2.connectIn(swiOut2, 0);
        fork2.connectOut(fork2Out1, 0);  //goes to merge1 false line
        fork2.connectOut(fork2Out2, 1);  //goes to merge2 false line
        fork2.connectOut(fork2Out3, 2);  //goes to merge3 false line

        //MERGE1
        Actor merge1 = Factory.createActor("merge");
        Channel merge1Out = Factory.createChannel();  //goes to increment
        merge1.connectIn(fork1Out1, 0);  
        merge1.connectIn(fork2Out1, 2);  //false line
        merge1.connectOut(merge1Out, 0);  

        //MERGE2
        Actor merge2 = Factory.createActor("merge");
        Channel merge2Out = Factory.createChannel();  //goes to add1
        merge2.connectIn(fork1Out2, 0);
        merge2.connectIn(fork2Out2, 2);  //false line
        merge2.connectOut(merge2Out, 0);  

        //MERGE3
        Actor merge3 = Factory.createActor("merge");
        Channel merge3Out = Factory.createChannel();  //goes to add2
        merge3.connectIn(fork1Out3, 0);
        merge3.connectIn(fork2Out3, 2);  //false line
        merge3.connectOut(merge3Out, 0);  


        //INCREMENT
        Actor increment = Factory.createActor("inc");
        Channel incOut = Factory.createChannel();
        increment.connectIn(merge1Out, 0);
        increment.connectOut(incOut, 0);  

        //FORK3
        Actor fork3 = Factory.createActor("fork");
        Channel fork3Out1 = Factory.createChannel();
        Channel fork3Out2 = Factory.createChannel();
        Channel fork3Out3 = Factory.createChannel();
        fork3.connectIn(incOut, 0);
        fork3.connectOut(fork3Out1, 0);  //goes to merge1 true line
        fork3.connectOut(fork3Out2, 1);  //goes to add1 
        fork3.connectOut(fork3Out3, 2);  //goes to comparison
        compare.connectIn(fork3Out3, 1);    
        merge1.connectIn(fork3Out1, 1);  //true line

        //ADD1
        Actor add1 = Factory.createActor("add");
        add1.connectIn(fork3Out2, 0);
        add1.connectIn(merge2Out, 1);
        Channel add1Out = Factory.createChannel();   //goes to fork4
        add1.connectOut(add1Out, 0);

        //FORK4
        Actor fork4 = Factory.createActor("fork");
        Channel fork4Out1 = Factory.createChannel();
        Channel fork4Out2 = Factory.createChannel();
        Channel fork4Out3 = Factory.createChannel();
        fork4.connectIn(add1Out, 0);
        fork4.connectOut(fork4Out1, 0);  //goes to merge2 true line
        fork4.connectOut(fork4Out2, 1);  //goes to merge3 true line
        fork4.connectOut(fork4Out3, 2);  //goes to add2
        merge2.connectIn(fork4Out1, 1);  //true line
        merge3.connectIn(fork4Out2, 1);  //true line

        //ADD2
        Actor add2 = Factory.createActor("add");
        add2.connectIn(fork4Out3, 0);
        add2.connectIn(merge3Out, 1);
        Channel add2Out = Factory.createChannel();   //goes to out
        add2.connectOut(add2Out, 0);

        //OUT
        Actor out = Factory.createActor("output");
        out.connectIn(add2Out, 0);



        //-------------------****** Next : Add Tasks to TaskList ******----------------------

        //Add Tasks to List (add INPUT at first & OUTPUT at last)
        List<Actor> taskList = new ArrayList<Actor>();
        taskList.add(input);
        taskList.add(compare);
        taskList.add(fork1);
        taskList.add(swi);
        taskList.add(garbageCollector);
        taskList.add(fork2);
        taskList.add(merge1);
        taskList.add(increment);
        taskList.add(fork3);
        taskList.add(merge2);
        taskList.add(add1);
        taskList.add(fork4);
        taskList.add(merge3);
        taskList.add(add2);
        taskList.add(out);

        //pass taskList and number of threads as params
        start(numberOfThreads, taskList);

        return;
        
    }
}
