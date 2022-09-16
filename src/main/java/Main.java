import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        final int CLIENTS_TODAY = 5;
        final int COOKING_TIME = 2500;
        final int CHOICE_TIME = 1000;
        final int EATING_TIME = 1500;

        Queue<String> orders = new LinkedList<>();
        Queue<String> clientQueue = new LinkedList<>();

        Runnable chefLogic = () -> {
            for (int i = 0; i < CLIENTS_TODAY; i++) {
                synchronized (orders) {
                    try {
                        if (orders.isEmpty()) {
                            orders.wait();
                        }
                        System.out.println("Cooking a dish");
                        Thread.sleep(COOKING_TIME);
                        System.out.println("Chef cook a dish for " + orders.poll());
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                    orders.notify();
                }
            }
            System.out.println("Chef " + Thread.currentThread().getName() + ": I'm leaving");
        };

        Runnable waiterLogic = () -> {
            String order;
            int clientsServed = 0;
            while (clientsServed < CLIENTS_TODAY){
                synchronized (clientQueue) {
                    if (clientQueue.isEmpty()) continue;
                    order = clientQueue.poll();
                    System.out.println("Waiter " + Thread.currentThread().getName() + " get order from " + order);
                }
                synchronized (orders) {
                    try {
                        orders.add(order);
                        orders.notify();
                        orders.wait();
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                synchronized (clientQueue) {
                    System.out.println("Waiter delivered a dish for " + order);
                    clientsServed++;
                    clientQueue.notify();
                }
            }
            System.out.println("Waiter " + Thread.currentThread().getName() + ": I'm leaving");
        };

        Runnable clientLogic = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " in the cafe");
                synchronized (clientQueue) {
                    Thread.sleep(CHOICE_TIME);
                    clientQueue.add(Thread.currentThread().getName());
                    clientQueue.wait();
                    System.out.println(Thread.currentThread().getName() + ": Om-nom-nom");
                    Thread.sleep(EATING_TIME);
                }
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(Thread.currentThread().getName() + ": I'm leaving");
        };
        ThreadGroup group = new ThreadGroup("Group");

        new Thread(group, chefLogic).start();
        new Thread(group, waiterLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();

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
