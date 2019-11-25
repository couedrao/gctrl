import java.util.List;

//

// @author couedrao on 25/11/2019.

//* @project gctrl

//

//

//* 1)Structures the actions needed to achieve goals and objectives Structures the actions needed to achieve goals and objectives.

//* 2)The plan function creates or selects a procedure to enact a desired alteration in the managed resource.

//* 3)The plan function can take on many forms, ranging from a single command to a complex workflow.

//*

@SuppressWarnings({"InfiniteLoopStatement", "LoopConditionNotUpdatedInsideLoop", "SynchronizeOnNonFinalField"})
class Plan {
    private static int i = 0;
    private String gw_PLAN = "";

    void start() {
        Main.logger(this.getClass().getSimpleName(), "Start Planning");
        List<String> rfcs = Main.shared_knowledge.get_rfc();
        List<String> plans = Main.shared_knowledge.get_plans();
        while (Main.run) {
            String current_rfc = Main.analyze.get_rfc();
            Main.logger(this.getClass().getSimpleName(), "Received RFC : " + current_rfc);
            if (current_rfc.contentEquals(rfcs.get(0))) {
                Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(0));
                update_plan(plans.get(0));
                i--;
            } else if (current_rfc.contentEquals(rfcs.get(1))) {
                if (i < 2) {
                    Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(1));
                    update_plan(plans.get(1));
                } else {
                    Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(2));
                    update_plan(plans.get(2));
                }
                i++;
            }

        }
    }

    String get_plan() {
        synchronized (gw_PLAN) {
            try {
                gw_PLAN.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gw_PLAN;
    }

    private void update_plan(String plan) {
        synchronized (gw_PLAN) {
            gw_PLAN.notify();
            gw_PLAN = plan;
        }
    }
}

