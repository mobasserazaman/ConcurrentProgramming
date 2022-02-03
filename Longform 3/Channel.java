public interface Channel {

    void set(int i);
    void enqueue(int item) throws InterruptedException ;
    int dequeue() throws InterruptedException;
    boolean isEmpty() throws InterruptedException;
    void changeLimit(int limit);

}
