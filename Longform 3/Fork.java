import java.util.ArrayList;

public class Fork implements Actor {

    private Channel input[];
    private ArrayList<Channel> output = new ArrayList<Channel>();

    Fork(){
        input = new Channel[1];
    }

    @Override
    public void connectIn(Channel c, int i) {
        input[i] = c;
    }

    @Override
    public void connectOut(Channel c, int i) {
        output.add(c);
    }

    @Override
    public Boolean call() {

        int i = 0;

            try{
                
                if(input[0].isEmpty() == false){

                    i = input[0].dequeue();
                    for(int k = 0 ; k < output.size(); k++) output.get(k).enqueue(i);

                    if(i == -1111) return false;
                    else return true;
        
                }else return true;

            }catch(InterruptedException e){
            }

            return true;
    }
}