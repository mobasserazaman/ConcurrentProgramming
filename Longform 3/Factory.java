public class Factory {

    private Factory(){
        
    }

    public static Actor createActor(String str){

        Actor a;

        if(str.equals("add")){
            a = new Add();
        }else if(str.equals("inc")){
            a = new Increment();
        }else if(str.equals("fork")){
            a = new Fork();
        }else if(str.equals("merge")){
            a = new Merge();
        }else if(str.equals("cdr")){
            a = new Cdr();
        }else if(str.equals("<")){
            a = new Comparison("<");
        }else if(str.equals("==")){
            a = new Comparison("==");
        }else if(str.equals("output")){
            a = new Output();
        }else if(str.equals("input")){
            a = new Input();
        }else if(str.equals("garbage")){
            a = new GarbageCollector();
        }else{
            a = new Switch();
        }
        return a;
    }

    public static Channel createChannel(){
        Channel c = new Edge(5);
        return c;
    }
    
}
