import java.util.ArrayList;

public class GarbageCollector implements Actor{

    private Channel input[];
    ArrayList<Integer> out = new ArrayList<Integer>();
    String print = "";


    GarbageCollector(){
        input = new Channel[1];
    }

    @Override
    public void connectIn(Channel c, int i) {
        input[i] = c;
    }

    @Override
    public void connectOut(Channel c, int i){
        //

    }
    
    @Override
    public Boolean call() {

        int i = 0;

            try{

                if(input[0].isEmpty() == false){

                    i = input[0].dequeue();
                    //System.out.println(i);

                    if(i == -1111){
                        return false;
                    }else{
                        return true;
                    } 

                }else{

                    return true;

                }

            }catch(InterruptedException e){


            }

            return true;


    }





    
}
