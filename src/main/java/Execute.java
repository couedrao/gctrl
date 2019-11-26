import java.util.List;

//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

//
//* Changes the behavior of the managed resource using effectors Changes the behavior of the managed resource using effectors, based on the actions recommended by the plan function.
//*
@SuppressWarnings({"SameParameterValue", "InfiniteLoopStatement", "LoopConditionNotUpdatedInsideLoop"})
class Execute {
    private static List<String> workflow_lists;

    void start() {
        Main.logger(this.getClass().getSimpleName(), "Start Execution");
        List<String> plans = Main.shared_knowledge.get_plans();
        workflow_lists = Main.shared_knowledge.get_worklow_lists();

        while (Main.run) {
            //Plan Receiver
            String current_plan = Main.plan.get_plan();
            Main.logger(this.getClass().getSimpleName(), "Received Plan : " + current_plan);

            //Rule-based Workflow Generator
            if (current_plan.contentEquals(plans.get(0))) {
                workflow_0();
            } else if (current_plan.contentEquals(plans.get(1))) {
                workflow_1();
            } else if (current_plan.contentEquals(plans.get(2))) {
                workflow_2();
            }

        }
    }

    private void workflow_0() {
        Main.logger(this.getClass().getSimpleName(), "Workflow : " + workflow_lists.get(0));
        //Call Effectors
        /*TODO*/
    }

    private void workflow_1() {
        Main.logger(this.getClass().getSimpleName(), "Workflow : " + workflow_lists.get(1));
        //Call Effectors
        /*TODO*/
    }

    private void workflow_2() {
        Main.logger(this.getClass().getSimpleName(), "Workflow : " + workflow_lists.get(2));
        //Call Effectors
        /*TODO*/
    }

}
