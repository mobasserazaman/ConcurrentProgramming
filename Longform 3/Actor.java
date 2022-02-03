import java.util.concurrent.Callable;

public interface Actor extends Callable<Boolean>{

    void connectIn(Channel c, int i);
    void connectOut(Channel c, int i);

}