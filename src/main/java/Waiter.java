public class Waiter extends Thread{
    public Waiter(ThreadGroup group, String name){
        super(group, name);
    }
    @Override
    public void run() {
        String order;
        int clientsServed = 0;
        while (clientsServed < Main.CLIENTS_TODAY){
            synchronized (Main.clientQueue) {
                if (Main.clientQueue.isEmpty()) continue;
                order = Main.clientQueue.poll();
                System.out.println("Waiter " + Thread.currentThread().getName() + " get order from " + order);
            }
            synchronized (Main.orders) {
                try {
                    Main.orders.add(order);
                    Main.orders.notify();
                    Main.orders.wait();
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            synchronized (Main.clientQueue) {
                System.out.println("Waiter delivered a dish for " + order);
                clientsServed++;
                Main.clientQueue.notify();
            }
        }
        System.out.println("Waiter " + Thread.currentThread().getName() + ": I'm leaving");

    }
}
