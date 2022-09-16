import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        final int CLIENTS_TODAY = 5;
        final int COOKING_TIME = 2500;
        final int CHOICE_TIME = 1000;

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
        };

        Runnable waiterLogic = () -> {
            String order;
            for (int i = 0; i < CLIENTS_TODAY; i++) {
                synchronized (clientQueue) {
                    if (clientQueue.isEmpty()) {
                        try {
                            clientQueue.wait();
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
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
                    clientQueue.notify();
                }
            }
        };

        Runnable clientLogic = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " in the cafe");
                synchronized (clientQueue) {
                    Thread.sleep(CHOICE_TIME);
                    clientQueue.add(Thread.currentThread().getName());

                    clientQueue.notify();
                    clientQueue.wait();
                    System.out.println(Thread.currentThread().getName() + ": Om-nom-nom");
                }
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        };
        ThreadGroup group = new ThreadGroup("Group");

        new Thread(group, chefLogic).start();
        new Thread(group, waiterLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
        new Thread(group, clientLogic).start();
    }
}
