import com.github.signaflo.math.operations.DoubleFunctions;
import com.github.signaflo.timeseries.TimeSeries;
import com.github.signaflo.timeseries.forecast.Forecast;
import com.github.signaflo.timeseries.model.arima.Arima;
import com.github.signaflo.timeseries.model.arima.ArimaOrder;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import de.vandermeer.asciithemes.a7.A7_Grids;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

//


//* 1)Collects the details from the managed resources e g topology Collects the details from the managed resources e.g.  topology information, metrics (e.g. offered capacity and throughput), configuration property settings and so on.


//* 2)The monitor function aggregates,correlates and filters these details until it determines a symptom that needs to be analyzed.


//*


@SuppressWarnings({"InfiniteLoopStatement", "LoopConditionNotUpdatedInsideLoop", "SynchronizeOnNonFinalField"})
class Monitor {
    private static List<String> symptom;
    private static final int period = 1000;
    private static int i = 0;
    private String gw_current_SYMP = "N/A";

    void start() {
        Main.logger(this.getClass().getSimpleName(), "Start monitoring of " + Knowledge.gw);
        symptom = Main.shared_knowledge.get_symptoms();
        Main.shared_knowledge.create_lat_tab();
        fill_tab();
        process_lat();
    }

    //wait and process the latencies in the db
    private void process_lat() {
        while (Main.run)
            try {
                Thread.sleep(period);
                ResultSet rs = Main.shared_knowledge.select_from_tab();
                //print_rs(rs, k);
                double[] prediction = predict_next_lat(rs);
                boolean isOk = true;
                for (int j = 0; j < Knowledge.horizon; j++) {
                    if (prediction[j] > Knowledge.gw_lat_threshold) {
                        Main.logger(this.getClass().getSimpleName(), "Symptom --> To Analyse : " + symptom.get(1));
                        update_symptom(symptom.get(1));
                        isOk = false;
                        break;
                    } else if (prediction[j] < .0) {
                        Main.logger(this.getClass().getSimpleName(), " Symptom --> To Analyse : " + symptom.get(0));
                        update_symptom(symptom.get(0));
                        isOk = false;
                        break;
                    }
                }
                if (isOk) {
                    Main.logger(this.getClass().getSimpleName(), "Symptom --> To Analyse : " + symptom.get(2));
                    update_symptom(symptom.get(2));
                }
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
    }

    private void fill_tab() {
        new Thread(() -> {
            Main.logger(this.getClass().getSimpleName(), "Filling db with latencies");
            while (true)
                try {
                    //TODO: Remove this
                    Thread.sleep(period / 5);

                    Main.shared_knowledge.insert_in_tab(new java.sql.Timestamp(new java.util.Date().getTime()), get_fake_data());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }

        ).start();
    }

    private int get_data() {
        /*TODO*/
        return 0;
    }

    private int get_fake_data() {
        //return new Random().nextInt();
        return i++;
    }

    private double[] predict_next_lat(ResultSet rs) throws SQLException {
        double[] history = new double[Knowledge.moving_wind];
        double[] p = new double[Knowledge.horizon];
        int j = Knowledge.moving_wind - 1;
        while (rs.next()) {
            history[j] = Double.parseDouble(rs.getString("latency"));
            j--;
        }
        TimeSeries timeSeries = TimeSeries.from(DoubleFunctions.arrayFrom(history));
        ArimaOrder modelOrder = ArimaOrder.order(0, 1, 1, 0, 1, 1);
        //ArimaOrder modelOrder = ArimaOrder.order(0, 0, 0, 1, 1, 1);
        Arima model = Arima.model(timeSeries, modelOrder);
        Forecast forecast = model.forecast(Knowledge.moving_wind);
        for (int k = 0; k < Knowledge.horizon; k++)
            p[k] = forecast.pointEstimates().at(k);

        return p;
    }

    private void print_rs(ResultSet rs, Knowledge k) throws SQLException {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Timestamp", "Latency_in_" + Knowledge.gw);
        at.addRule();
        while (rs.next()) {
            at.addRow(rs.getTimestamp("id").getTime(), rs.getString("latency"));
            at.addRule();
        }
        at.getContext().setGrid(A7_Grids.minusBarPlusEquals());
        at.getRenderer().setCWC(new CWC_LongestWord());
        Main.logger(this.getClass().getSimpleName(), at.render());

    }

    String getsymptom() {
        synchronized (gw_current_SYMP) {
            try {
                gw_current_SYMP.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return gw_current_SYMP;
    }

    private void update_symptom(String symptom) {

        synchronized (gw_current_SYMP) {
            gw_current_SYMP.notify();
            gw_current_SYMP = symptom;

        }
    }


}