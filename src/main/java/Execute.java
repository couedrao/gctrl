import java.util.List;

//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

//
//* Changes the behavior of the managed resource using effectors Changes the behavior of the managed resource using effectors, based on the actions recommended by the plan function.
//*
@SuppressWarnings({"SameParameterValue", "InfiniteLoopStatement", "LoopConditionNotUpdatedInsideLoop", "SynchronizeOnNonFinalField"})
class Execute {
    private static List<String> workflow_lists;

    void start() {
        Main.logger(this.getClass().getSimpleName(), "Start Execution");
        workflow_lists = Main.shared_knowledge.get_worklow_lists();

        while (Main.run) {
            String current_plan = get_plan();
            // Main.logger(this.getClass().getSimpleName(), "Received Plan : " + current_plan);

            Main.logger(this.getClass().getSimpleName(), "Workflow : " + workflow_generator(current_plan));

        }
    }

    //Plan Receiver
    private String get_plan() {
        synchronized (Main.plan.gw_PLAN) {
            try {
                Main.plan.gw_PLAN.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Main.plan.gw_PLAN;
    }

    //Rule-based Workflow Generator
    private String workflow_generator(String plan) {
        List<String> plans = Main.shared_knowledge.get_plans();

        if (plan.contentEquals(plans.get(0))) {
            return workflow_0();
        } else if (plan.contentEquals(plans.get(1))) {
            return workflow_1();
        } else if (plan.contentEquals(plans.get(2))) {
            return workflow_2();
        } else
            return null;
    }

    private String workflow_0() {
        //Call Effectors
        /*TODO*/
        return workflow_lists.get(0);
    }

    private String workflow_1() {
        //Call Effectors
        /*TODO*/
        return workflow_lists.get(2);
    }

    private String workflow_2() {
        //Call Effectors
        /*TODO*/
        return workflow_lists.get(2);
    }

}
