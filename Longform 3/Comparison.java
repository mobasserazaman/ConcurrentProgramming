public class Comparison implements Actor {

    private Channel input[];
    private Channel output[];

    private String operator = "";
    private int val;
    private boolean takeInNewVal = true;

    Comparison(String operator){
        this.input = new Channel[2];
        this.output = new Channel[1];
        this.operator = operator;
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

                if(takeInNewVal){
                    this.val = input[0].dequeue();
                    takeInNewVal = false;
                    output[0].enqueue(0); //enqueue false
                    if(this.val == -1111){
                        return false;
                    }else return true;
                }

                if(input[1].isEmpty() == false){

                    i = input[1].dequeue();

                    if(i == -1111) {

                        output[0].enqueue(i);
                        return false;

                    }else{

                        if(operator.equals("==")){

                            if(i == val) output[0].enqueue(1);
                            else {
                                this.val = input[0].dequeue();   
                                if(this.val == -1111){
                                    output[0].enqueue(-1111);
                                    return false;
                                }else{
                                    output[0].enqueue(0);
                                }
                            }

                        }else if(operator.equals("<")){
        
                            if(i < val){
                                output[0].enqueue(1);
                            } else {
                                this.val = input[0].dequeue();   
                                if(this.val == -1111){
                                    output[0].enqueue(-1111);
                                    return false;
                                }else{
                                    output[0].enqueue(0);
                                }
                            }

                        }
                        return true;
                    }
                }else return true;

            }catch(InterruptedException e){

            }
            return true;
    }
}