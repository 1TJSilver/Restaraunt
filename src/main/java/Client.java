public class Client extends Thread{
    public Client(ThreadGroup group, String name){
        super(group, name);
    }
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " in the cafe");
            synchronized (Main.clientQueue) {
                Thread.sleep(Main.CHOICE_TIME);
                Main.clientQueue.add(Thread.currentThread().getName());
                Main.clientQueue.wait();
                System.out.println(Thread.currentThread().getName() + ": Om-nom-nom");
                Thread.sleep(Main.EATING_TIME);
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(Thread.currentThread().getName() + ": I'm leaving");
    }
}
