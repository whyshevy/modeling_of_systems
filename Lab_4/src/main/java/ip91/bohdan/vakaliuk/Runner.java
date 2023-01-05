package ip91.bohdan.vakaliuk;

import ip91.bohdan.vakaliuk.config.Config;
import ip91.bohdan.vakaliuk.service.chart.LineChartEx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {

  private static final int K = 20;

  public static void main(String[] args) {
    List<Integer> SMO_N_LIST = List.of(499, 999, 1999, 2999, 3999, 4999);

    LineChartEx lineChartEx = new LineChartEx();

//    experimentEvaluation(SMO_N_LIST, lineChartEx);
    theoreticalEvaluation(SMO_N_LIST, lineChartEx);
  }

  private static void experimentEvaluation(List<Integer> SMO_N_LIST, LineChartEx lineChartEx) {
    Map<Integer, Long> resultData = new SimModel().start(SMO_N_LIST);

    lineChartEx.draw(resultData, Config.CHART_EXPERIMENT);
  }

  private static void theoreticalEvaluation(List<Integer> SMO_N_LIST, LineChartEx lineChartEx) {
    Map<Integer, Long> resultData = new HashMap<>();

    SMO_N_LIST.forEach(smo_n -> {
      double intensity = 1 / Config.START_MEAN_DELAY + smo_n * (0.98 / Config.DEVICE_MEAN_DELAY);
      long actionCount = (long) (intensity * Config.IMITATION_TIME * K);

      System.out.println("SMO_N: " + smo_n + " ActionCount: " + actionCount);

      resultData.put(smo_n, actionCount);
    });

    lineChartEx.draw(resultData, Config.CHART_THEORY);
  }

}
