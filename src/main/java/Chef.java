public class Chef extends Thread{
    public Chef(ThreadGroup group, String name){
        super(group, name);
    }
   @Override
    public void run() {
       for (int i = 0; i < Main.CLIENTS_TODAY; i++) {
           synchronized (Main.orders) {
               try {
                   if (Main.orders.isEmpty()) {
                       Main.orders.wait();
                   }
                   System.out.println("Cooking a dish");
                   Thread.sleep(Main.COOKING_TIME);
                   System.out.println("Chef cook a dish for " + Main.orders.poll());
               } catch (InterruptedException ex) {
                   System.out.println(ex.getMessage());
               }
               Main.orders.notify();
           }
       }
       System.out.println("Chef " + Thread.currentThread().getName() + ": I'm leaving");
    }
}
