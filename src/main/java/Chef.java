import java.util.LinkedList;
import java.util.Queue;

public class Chef extends Thread{
    protected Queue<String> orderList = new LinkedList<>();
    private Chef(String name) {
        super(name);
    }

    @Override
    public void run() {
        while (!isInterrupted()){

        }
    }
    public void addOrder(String order){
        orderList.add(order);
    }
}
