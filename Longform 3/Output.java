import java.util.ArrayList;

public class Output implements Actor{

    private Channel input[];
    ArrayList<Integer> out = new ArrayList<Integer>();
    String print = "";
    boolean newOut = false;


    Output(){
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
        int n = -1;

            try{

                if(input[0].isEmpty() == false){


                    i = input[0].dequeue();
                   // n = input[1].dequeue();


                    // if(n == 0){

                    //     for(int k = 0 ; k < out.size(); k++){
                    //         if(k != out.size()-1){

                    //             print = print + out.get(k) + ", ";

                    //         }else{

                    //             print = print + out.get(k);

                    //         }
                    //     }
                    //    if(print.length() != 0)   System.out.println(print);

                    //    print = "";
                    //    out.clear();

                    // }

                    if(i == -1111){
                        for(int k = 0 ; k < out.size(); k++){
                            if(k != out.size()-1){

                                print = print + out.get(k) + ", ";

                            }else{

                                print = print + out.get(k);

                            }

                        }
                        System.out.println(print);
                        return false;

                    }else{
                        out.add(i);
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
