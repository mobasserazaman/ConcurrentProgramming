public class Switch implements Actor {

    private Channel input[];
    private Channel output[];

    Switch(){
        input = new Channel[2];
        output = new Channel[2];
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
        int d = 0;

        try{

            if(input[0].isEmpty() == false && input[1].isEmpty() == false){

                i = input[0].dequeue();
                d = input[1].dequeue();
        
                if(i == -1111 || d == -1111) {
                    output[0].enqueue(i);
                    output[1].enqueue(i);
                    return false;
                }else{
                    if(i == 1) output[0].enqueue(d); //true
                    else output[1].enqueue(d);  //false
                    return true;
                }

            }else return true;

        }catch(InterruptedException e){

        }

        return true;
    }
}