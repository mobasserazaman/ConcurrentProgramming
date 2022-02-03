public class Cdr implements Actor {

    private Channel input[];
    private Channel output[];
    private boolean firstVal = true;

    Cdr(){
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
                        firstVal = true;
                        return false;

                    }else{

                        if(firstVal){

                            firstVal = false;

                        }else{
                            output[0].enqueue(i);
                        }

                        return true;

                    }

                }else return true;


            }catch(InterruptedException e){

            }

            return true;
    }
}