import java.util.LinkedList;
import java.util.Queue;

public class Main {
    static final int CLIENTS_TODAY = 5;
    static final int COOKING_TIME = 2500;
    static final int CHOICE_TIME = 1000;
    static final int EATING_TIME = 1500;

    static Queue<String> orders = new LinkedList<>();
    static Queue<String> clientQueue = new LinkedList<>();
    public static void main(String[] args) {


        ThreadGroup group = new ThreadGroup("Group");

        new Chef(group, "Chef-1").start();
        new Waiter(group, "Waiter-1").start();
        new Client(group, "Client-1").start();
        new Client(group, "Client-2").start();
        new Client(group, "Client-3").start();
        new Client(group, "Client-4").start();
        new Client(group, "Client-5").start();

        waitAndFinish(group);
    }
    public static void waitAndFinish (ThreadGroup group){
        while (group.activeCount() > 0){
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }
        System.out.println(group.getName() + " is end");
        group.interrupt();
    }
}
