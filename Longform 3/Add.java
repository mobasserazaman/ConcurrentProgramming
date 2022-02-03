class Add implements Actor{

    Channel input[];
    Channel output[];

    Add(){
        input = new Channel[2];
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

    public Boolean call() {

        int i = 0;
        int j = 0;

            try{



                        //continue
                        if(input[0].isEmpty() == false && input[1].isEmpty() == false){

                            i = input[0].dequeue();         
                            j = input[1].dequeue();
        
                            if(i == -1111 || j == -1111) {
                                output[0].enqueue(-1111);
                                return false;
                            }else {
                                output[0].enqueue(i+j);
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
