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
    private static int i;
    public String gw_PLAN = "";

    void start() {
        Main.logger(this.getClass().getSimpleName(), "Start Planning");

        while (Main.run) {
            String current_rfc = get_rfc();
            //Main.logger(this.getClass().getSimpleName(), "Received RFC : " + current_rfc);
            update_plan(plan_generator(current_rfc));

        }
    }

    //RFC Receiver
    private String get_rfc() {
        synchronized (Main.analyze.gw_current_RFC) {
            try {
                Main.analyze.gw_current_RFC.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Main.analyze.gw_current_RFC;
    }


    //Rule-based Plan Generator
    private String plan_generator(String rfc) {
        List<String> rfcs = Main.shared_knowledge.get_rfc();
        List<String> plans = Main.shared_knowledge.get_plans();

        if (rfc.contentEquals(rfcs.get(0))) {
            Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(0));
            i = 0;
            return plans.get(0);
        } else if (rfc.contentEquals(rfcs.get(1))) {
            if (i < 2) {
                Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(1));
                i++;
                return plans.get(1);
            } else {
                Main.logger(this.getClass().getSimpleName(), "Plan --> To Execute : " + plans.get(2));
                i++;
                return plans.get(2);
            }

        } else {
            return null;
        }
    }


    private void update_plan(String plan) {
        synchronized (gw_PLAN) {
            gw_PLAN.notify();
            gw_PLAN = plan;
        }
    }
}

