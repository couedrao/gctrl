import java.util.List;
import java.util.Scanner;

//
//* @author couedrao on 25/11/2019.
//* @project gctrl
//
@SuppressWarnings({"InfiniteLoopStatement", "LoopConditionNotUpdatedInsideLoop"})
class Tester {

    private static final Knowledge k = new Knowledge();

    public static void main(String[] args) throws Exception {
        k.start();
        List<String> worklow_lists = k.get_worklow_lists();
        while (Main.run) {
            logger("You are in test mode! The following actions can be performed : ");
            for (int i = 0; i < worklow_lists.size(); i++) {
                logger("[" + i + "] :" + worklow_lists.get(i));
            }
            logger("Select any number in [0-" + (worklow_lists.size() - 1) + "] to continue");
            int input = new Scanner(System.in).nextInt();
            if (input < worklow_lists.size() )
                logger("Execution of Action : [" + worklow_lists.get(input) + "]");
            else logger("(-_-)");
            /*TODO : */
        }
    }

    private static void logger(String msg) {
        if (true)
            System.out.println(msg);
    }
}
