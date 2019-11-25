import org.apache.log4j.Level;
import org.apache.log4j.Logger;

//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

class Main {
     static final boolean run = true;
    static final Monitor monitor = new Monitor();
    static final Analyze analyze = new Analyze();
    static final Plan plan = new Plan();
    private static final Execute execute = new Execute();
     static final Knowledge shared_knowledge = new Knowledge();
    private static final boolean log = true;

    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.ERROR);


        shared_knowledge.start();
        Thread.sleep(3000);

        Thread thread_m = new Thread(() -> {
            try {
                monitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        Thread thread_a = new Thread(() -> {
            try {
                analyze.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread thread_p = new Thread(() -> {
            try {
                plan.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread thread_e = new Thread(() -> {
            try {
                execute.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread_m.start();
        thread_a.start();
        thread_p.start();
        thread_e.start();

    }

    static void logger(String from, String msg) {
        if (log)
            System.out.println("[" + from + "] : " + msg);
    }
}