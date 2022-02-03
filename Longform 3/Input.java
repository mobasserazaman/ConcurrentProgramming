import java.util.ArrayList;
import java.util.Scanner;


public class Input implements Actor{

    private Channel output[];
    ArrayList<Integer> in = new ArrayList<Integer>();

    Input(){
        output = new Channel[1];
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String numbers[] = str.split("\\s+");
           in.add(Integer.parseInt(numbers[0]));
    
    }

    @Override
    public void connectIn(Channel c, int i) {
        //
    }

    @Override
    public void connectOut(Channel c, int i){
        output[i] = c;
    }

    public int getSizeOfInputStream(){
        return in.size();
    }
    
    @Override
    public Boolean call() {

        int data = -1;
        try{
                if(in.size() == 1){
    
                    data = in.remove(0);
                    output[0].enqueue(data);
                    output[0].enqueue(-1111);  //ends simulation
                    return false;
    
                }else return true;
        }catch(InterruptedException e){

        }
        return true;
    }
}
