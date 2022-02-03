public class Merge implements Actor {

    private Channel input[];
    private Channel output[];

    Merge(){

        input = new Channel[3];
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

        int j = -1;
        int i = -1;

        try{

                if(input[0].isEmpty() == false){

                    i = input[0].dequeue();
                    
                    if(i == -1111) {

                        output[0].enqueue(i);
                        return false;

                    }else if(i == 1){ //true line

                        if(input[1].isEmpty() == false){

                            j = input[1].dequeue();
                            output[0].enqueue(j);

                            if(j == -1111) return false;
                            else return true;
                        }
                    }else{  //false line
                        
                        if(input[2].isEmpty() == false){

                            j = input[2].dequeue();                    
                            output[0].enqueue(j);

                            if(j == -1111)return false;
                            else return true;
                        }
                    }  
                }
            }catch(InterruptedException e){

            }

            return true;
    }
}