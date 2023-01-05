package ip91.bohdan.vakaliuk.service.chart;

import ip91.bohdan.vakaliuk.config.Config;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LineChartEx extends JFrame {

  private static final String WINDOWS_TITLE = "IP-91 Chui Oleh Lab_4";
  private static final int CHART_BORDER = 15;
  private static final String XYSeriesKey = "2016";
  private static final String TITLE_KEY = "title";
  private static final String X_AXIS_KEY = "x-axis";
  private static final String Y_AXIS_KEY = "y-axis";

  private static final String CHART_EXP_TITLE = "Eкспериментальна оцінка складності алгоритму імітації мережі масового обслуговування";
  private static final String CHART_EXP_X_AXIS_LABEL = "Складність моделі (кількість подій)";
  private static final String CHART_EXP_Y_AXIS_LABEL = "Час виконання (секунд)";

  private static final String CHART_THEORY_TITLE = "Теоретична оцінка складності алгоритму імітації мережі масового обслуговування";
  private static final String CHART_THEORY_X_AXIS_LABEL = "Складність моделі (кількість подій)";
  private static final String CHART_THEORY_Y_AXIS_LABEL = "Кількість операцій";

  public void draw(Map<Integer, Long> resultData, String chartFor) {
    XYDataset dataset = createDataset(resultData);
    JFreeChart chart = createChart(dataset, chartFor);

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setBorder(BorderFactory.createEmptyBorder(CHART_BORDER, CHART_BORDER, CHART_BORDER, CHART_BORDER));
    chartPanel.setBackground(Color.white);
    add(chartPanel);

    pack();
    setTitle(WINDOWS_TITLE);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private XYDataset createDataset(Map<Integer, Long> resultData) {

    var series = new XYSeries(XYSeriesKey);

    resultData.forEach(series::add);

    var dataset = new XYSeriesCollection();
    dataset.addSeries(series);

    return dataset;
  }

  private JFreeChart createChart(XYDataset dataset, String chartFor) {
    Map<String, String> chartDesc = getChartDesc(chartFor);
    JFreeChart chart = ChartFactory.createXYLineChart(
        chartDesc.get(TITLE_KEY),
        chartDesc.get(X_AXIS_KEY),
        chartDesc.get(Y_AXIS_KEY),
        dataset,
        PlotOrientation.VERTICAL,
        true,
        true,
        false
    );

    XYPlot plot = chart.getXYPlot();

    var renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesPaint(0, Color.RED);
    renderer.setSeriesStroke(0, new BasicStroke(2.0f));

    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.white);

    plot.setRangeGridlinesVisible(true);
    plot.setRangeGridlinePaint(Color.BLACK);

    plot.setDomainGridlinesVisible(true);
    plot.setDomainGridlinePaint(Color.BLACK);

    chart.getLegend().setFrame(BlockBorder.NONE);

    return chart;
  }

  private Map<String, String> getChartDesc(String chartFor) {
    Map<String, String> chartDesc = new HashMap<>();

    if (chartFor.equals(Config.CHART_EXPERIMENT)) {
      chartDesc.put(TITLE_KEY, CHART_EXP_TITLE);
      chartDesc.put(X_AXIS_KEY, CHART_EXP_X_AXIS_LABEL);
      chartDesc.put(Y_AXIS_KEY, CHART_EXP_Y_AXIS_LABEL);
    } else if (chartFor.equals(Config.CHART_THEORY)) {
      chartDesc.put(TITLE_KEY, CHART_THEORY_TITLE);
      chartDesc.put(X_AXIS_KEY, CHART_THEORY_X_AXIS_LABEL);
      chartDesc.put(Y_AXIS_KEY, CHART_THEORY_Y_AXIS_LABEL);
    }

    return chartDesc;
  }

}
