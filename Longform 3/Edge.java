import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Edge implements Channel{

    private List<Integer> queue = new LinkedList<Integer>();
    private int limit = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    Edge(int limit){
        this.limit = limit;
    }

    @Override
    public void set(int i) {
        this.queue.add(i);
    }

    public void enqueue(int item) throws InterruptedException  {
        lock.lock();
        try{
            while(this.queue.size() == this.limit) {
                if(notFull.awaitNanos(1000000) < 0) {
                    System.out.println("NO");
                    return; 
                }
            }
            this.queue.add(item);
            if(this.queue.size() == 1) {
                notEmpty.signal();
            }
        }finally{
            lock.unlock();
        }
    }

    public boolean isEmpty() throws InterruptedException{
        lock.lock();
        try{
            while(this.queue.size() == 0){
                if(notEmpty.awaitNanos(1000000) <= 0){
                    return true;
                }else return false;
            }
            return false;
        }finally{
            lock.unlock();
        }
    }

    public int dequeue() throws InterruptedException{
        lock.lock();
        try{
            while(this.queue.size() == 0){
                notEmpty.await();
            }
            if(this.queue.size() == this.limit){
                notFull.signal();
            }
            return this.queue.remove(0);
        }finally{
            lock.unlock();
        }
    }

    @Override
    public void changeLimit(int limit) {
        this.limit = limit;
    }
}
