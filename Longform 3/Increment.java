public class Increment implements Actor {

    private Channel input[];
    private Channel output[];

    Increment(){
        input = new Channel[1];
        output = new Channel[1];
    }

    @Override
    public void connectIn(Channel c, int i) {
        input[i] = c;
    }

    @Override
    public void connectOut(Channel c, int i) {
        output[i] = c;
    }

    @Override
    public Boolean call() {

        int i = 0;

            try{

                if(input[0].isEmpty() == false){

                    i = input[0].dequeue();
                    if(i == -1111) {
                        output[0].enqueue(i);
                        return false;
                    }else{
                        output[0].enqueue(i+1);
                        return true;
                    }
        
                }else return true;

            }catch(InterruptedException e){
            }

            return true;
    }
}
