import java.util.List;

//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

//
//* 1)Perform complex data analysis and reasoning on the symptoms provided by the monitor function.
//* 2)Influenced by stored knowledge data.
//* 3)If changes are required, a change request is logically passed to the plan function.
//*
@SuppressWarnings({"SameParameterValue", "InfiniteLoopStatement", "LoopConditionNotUpdatedInsideLoop", "SynchronizeOnNonFinalField"})
class Analyze {
    private String gw_current_RFC = "";

    void start() {
        Main.logger(this.getClass().getSimpleName(), "Start Analyzing");
        List<String> symptoms =Main.shared_knowledge.get_symptoms();
        List<String> rfcs = Main.shared_knowledge.get_rfc();

        while (Main.run) {
            //Symptom Receiver
            String current_symptom = Main.monitor.getsymptom();
            Main.logger(this.getClass().getSimpleName(), "Received Symptom : " + current_symptom);

            //Rule-based RFC Generator
            if (current_symptom.contentEquals(symptoms.get(0)) || current_symptom.contentEquals(symptoms.get(2))) {
                Main.logger(this.getClass().getSimpleName(), "RFC --> To plan : " + rfcs.get(0));
                update_rfc(rfcs.get(0));
            } else if (current_symptom.contentEquals(symptoms.get(1))) {
                Main.logger(this.getClass().getSimpleName(), "RFC --> To plan : " + rfcs.get(1));
                update_rfc(rfcs.get(1));
            }
        }
    }

    String get_rfc() {
        synchronized (gw_current_RFC) {
            try {
                gw_current_RFC.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gw_current_RFC;
    }

    private void update_rfc(String rfc) {

        synchronized (gw_current_RFC) {
            gw_current_RFC.notify();
            gw_current_RFC = rfc;

        }
    }

}
